import jinja2

from app.model.prompt import Prompt

# TODO: read from project or from user input
technology_stack = "Python 3, Jinja2"


def get_jinja_env():
  return jinja2.Environment(loader=jinja2.FileSystemLoader('prompts/'))


def kickstart_prompt(context):
  """
  Generate a kickstart prompt from a given context

  :param context: The context object
  :type context: Context

  :return: A prompt for code generation encapsulated in a Prompt object.
  :rtype: Prompt
  """
  jinja_env = get_jinja_env()

  coding_actions_template = jinja_env.get_template(CODING_ACTIONS_TEMPLATE)
  coding_actions = coding_actions_template.render()

  system_template = jinja_env.get_template('kickstart/system_prompt.txt.j2')
  system_prompt = system_template.render(
    technology_stack=technology_stack,
    coding_actions=coding_actions
  )

  return Prompt(context.high_level_prompt, system_prompt)


def step_prompt(context):
  return None

def yaml_parsing_error_prompt(context, completion_error_message):
  jinja_env = get_jinja_env()

  system_template = jinja_env.get_template(
    'yaml_parsing_error/system_prompt.txt.j2')
  system_prompt = system_template.render(
    error_message=error_message
  )

  return Prompt(None, system_prompt)
