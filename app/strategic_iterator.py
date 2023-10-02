# tqdm library makes the loops show a smart progress meter
from tqdm import tqdm
import yaml

from app import openai_util
from app.model.completion import EvaluatedCompletion
from app.model.context import Context
from app.openai_util import pretty_print_conversation
from app.prompt_generator import kickstart_prompt

tqdm.pandas()

"""
How many iterations can pass before iteration is forcefully stopped 
"""
hard_stop_iteration_limit = 5


def highlevel_iteration(context: Context):
  """
  Iterates conversation steps with model till user high-level prompt is
  completed

  Args:
    context(Context): the current context
  """
  # Initialize the progress bar
  progress_bar = tqdm(total=hard_stop_iteration_limit)
  # Start the loop
  while (not context.goal_reached and
         context.iteration_count < hard_stop_iteration_limit):
    # Update the progress bar description with current status
    progress_bar.set_description(f"Status: {context.current_status}")

    # Update the progress bar with additional information
    progress_bar.set_postfix(
      PromptTUsed=context.prompt_tokens_used,
      CompletTUsed=context.completion_tokens_used,
      TotalTUsed=context.prompt_tokens_used + context.completion_tokens_used
    )

    perform_step(context)

    # Update the progress bar
    progress_bar.update(1)
  if context.goal_reached:
    print(f"Goal reached in {context.iteration_count} steps")
  else:
    print("Goal not reached; hard stopped")


def perform_step(context: Context):

  if context.conversation.history[-1].completion_has_issues():
    prompt =
  if context.iteration_count == 0:
    prompt = kickstart_prompt(context)
  else:
    prompt = None # TODO: update
    context.current_status = "Coding..."

  response = complete(context, prompt)

  try:
    parsed_response = yaml.safe_load(response)
  except yaml.YAMLError as e:
    # Handle the YAML parsing error here
    print(f"Error parsing YAML: {e}")
    context.write_down_completion_issue(str(e))
    parsed_response = None  # You can set parsed_response to a default value or handle it accordingly

  if parsed_response:
    print(parsed_response)
    # TODO: parse actions
    pass



def complete(context, prompt):
  print(f"Prompt: {prompt}")
  pretty_print_conversation(prompt.to_messages())

  completion_response = openai_util.get_completion(prompt)

  context.prompt_tokens_used += completion_response['usage']["prompt_tokens"]
  context.completion_tokens_used += completion_response['usage']["completion_tokens"]
  context.total_tokens_used += completion_response['usage']["total_tokens"]
  if (len(completion_response['choices'])) > 1:
    print("!!! MULTIPLE CHOICES !!!")


  completion_str = EvaluatedCompletion(completion_response)
  context.write_down(prompt, completion_str)
  pretty_print_conversation(completion_response.to_messages())
  return completion_str


def extract_completion_str(completion):
  return
