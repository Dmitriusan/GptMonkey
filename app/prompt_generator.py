import json

import jinja2

from app.model.prompt import Prompt

# TODO: read from project or from user input
technology_stack = "Python 3, Jinja2"


def get_jinja_env():
  return jinja2.Environment(loader=jinja2.FileSystemLoader('prompts/'))


def decompose_prompt(context):
  """
  Generate a decompose prompt from a given context

  :param context: The context object
  :type context: Context

  :return: A prompt for code generation encapsulated in a Prompt object.
  :rtype: Prompt
  """
  jinja_env = get_jinja_env()

  system_prompt_templ = jinja_env.get_template('decompose/system_prompt.txt.j2')
  system_prompt = system_prompt_templ.render()

  user_prompt_templ = jinja_env.get_template('decompose/user_prompt.txt.j2')
  user_prompt = user_prompt_templ.render(
    technology_stack=technology_stack,
    high_level_programming_task=context.programming_task,
  )

  functions_file = jinja_env.get_template('decompose/functions.json').filename
  with open(functions_file,  'r') as ff:
    functions = json.load(ff)

  return Prompt(system_prompt, user_prompt, functions, "auto")


def kickstart_prompt(context):
  """
  Generate a kickstart prompt from a given context

  :param context: The context object
  :type context: Context

  :return: A prompt for code generation encapsulated in a Prompt object.
  :rtype: Prompt
  """
  jinja_env = get_jinja_env()

  # coding_actions_template = jinja_env.get_template(CODING_ACTIONS_TEMPLATE)
  # coding_actions = coding_actions_template.render()

  system_template = jinja_env.get_template('kickstart/system_prompt.txt.j2')
  system_prompt = system_template.render(
    technology_stack=technology_stack,
    # coding_actions=coding_actions
  )

  return Prompt(system_prompt, context.programming_task)


def step_prompt(context):
  return None

def yaml_parsing_error_prompt(context, completion_error_message):
  jinja_env = get_jinja_env()

  system_template = jinja_env.get_template(
    'yaml_parsing_error/system_prompt.txt.j2')
  system_prompt = system_template.render(
    error_message=error_message
  )

  return Prompt(system_prompt, None)
