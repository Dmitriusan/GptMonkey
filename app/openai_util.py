import os
import openai

from app.prompt import Prompt


# Function to set the OpenAI API key from the environment variable
def set_api_key():
  api_key = os.getenv("OPENAI_API_KEY")
  if not api_key:
    raise ValueError("Please set the OPENAI_API_KEY environment variable "
                     "with your API key.")
  openai.api_key = api_key


# Function to generate code using OpenAI
def get_completion(prompt: Prompt):
  # https://help.openai.com/en/articles/7042661-chatgpt-api-transition-guide
  completion = openai.ChatCompletion.create(
    model="gpt-3.5-turbo-instruct",
    messages=prompt.to_messages(),
    temperature=1,
    max_tokens=512,
    top_p=1,
    frequency_penalty=0,
    presence_penalty=0
  )

  print(f"Completion: {completion}")
  return completion
