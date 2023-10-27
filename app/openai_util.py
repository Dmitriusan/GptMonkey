import os
import openai
import tiktoken
from tenacity import retry, wait_random_exponential, stop_after_attempt
from termcolor import colored
import pprint

from app.model.conversation import Conversation
from app.model.prompt import Prompt


# Function to set the OpenAI API key from the environment variable
def set_api_key():
  api_key = os.getenv("OPENAI_API_KEY")
  if not api_key:
    raise ValueError("Please set the OPENAI_API_KEY environment variable "
                     "with your API key.")
  openai.api_key = api_key


# Function to generate code using OpenAI
# @retry(wait=wait_random_exponential(multiplier=1, max=40),
#        stop=stop_after_attempt(3), retry=retry_if_not_exception_type(openai.InvalidRequestError)))
def get_completion(conversation: Conversation, temperature=1.0):
  # https://help.openai.com/en/articles/7042661-chatgpt-api-transition-guide
  completion = openai.ChatCompletion.create(
    model="gpt-3.5-turbo",
    messages=conversation.to_messages(),
    functions=conversation.history[-1].prompt.functions,
    function_call=conversation.history[-1].prompt.function_call,
    temperature=temperature,
    max_tokens=1300,
    top_p=1,
    frequency_penalty=0,
    presence_penalty=0
  )

  # print(f"Completion: {completion}")
  return completion


# as described here https://platform.openai.com/docs/guides/gpt/managing-tokens
def num_tokens_from_messages(messages, model="gpt-3.5-turbo-0613"):
  """Returns the number of tokens used by a list of messages."""
  try:
    encoding = tiktoken.encoding_for_model(model)
  except KeyError:
    encoding = tiktoken.get_encoding("cl100k_base")
  if model == "gpt-3.5-turbo-0613":  # note: future models may deviate from this
    num_tokens = 0
    for message in messages:
      num_tokens += 4  # every message follows <im_start>{role/name}\n{content}<im_end>\n
      for key, value in message.items():
        num_tokens += len(encoding.encode(value))
        if key == "name":  # if there's a name, the role is omitted
          num_tokens += -1  # role is always required and always 1 token
    num_tokens += 2  # every reply is primed with <im_start>assistant
    return num_tokens
  else:
    raise NotImplementedError(f"""num_tokens_from_messages() is not presently implemented for model {model}.
  See https://github.com/openai/openai-python/blob/main/chatml.md for information on how messages are converted to tokens.""")


# From https://cookbook.openai.com/examples/how_to_call_functions_with_chat_models
def pretty_print_conversation(messages, functions=None, function_call=None):
  """Pretty-print a conversation to the console."""
  role_to_color = {
    "system": "red",
    "user": "green",
    "assistant": "blue",
    "function": "magenta",
    "func_def": "yellow",
  }

  for message in messages:
    if message["role"] == "system":
      print(colored(f"system: {message['content']}\n", role_to_color[
        message["role"]]))
    elif message["role"] == "user":
      print(colored(f"user: {message['content']}\n", role_to_color[message["role"]]))
    elif message["role"] == "assistant" and message.get("function_call"):
      print(colored(f"assistant: {message['function_call']}\n", role_to_color[message["role"]]))
    elif message["role"] == "assistant" and not message.get("function_call"):
      print(colored(f"assistant: {message['content']}\n", role_to_color[message["role"]]))
    elif message["role"] == "function":
      print(colored(f"function ({message['name']}): {message['content']}\n", role_to_color[message["role"]]))

  if functions is not None:
    print(colored(f"functions: {pprint.pformat(functions)}\n", role_to_color[
      "func_def"]))
  if function_call is not None:
    print(colored(f"function_call: {function_call}\n", role_to_color[
      "func_def"]))
