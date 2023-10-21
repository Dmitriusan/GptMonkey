import json

import jinja2

from app.model.prompt import Prompt


def get_jinja_env():
  return jinja2.Environment(loader=jinja2.FileSystemLoader(
    'prompts/vulnerabilities'))


def vuln_search_prompt(target_file, code_fragment):
  jinja_env = get_jinja_env()

  system_prompt_templ = jinja_env.get_template('analyze/system_prompt.txt.j2')
  system_prompt = system_prompt_templ.render()

  user_prompt_templ = jinja_env.get_template('analyze/user_prompt.txt.j2')
  user_prompt = user_prompt_templ.render(
    file_path=target_file,
    code_fragment=code_fragment,
  )

  functions_file = jinja_env.get_template('analyze/functions.json').filename
  with open(functions_file,  'r') as ff:
    functions = json.load(ff)

  return Prompt(system_prompt, user_prompt, functions, {"name": "store_results"})
