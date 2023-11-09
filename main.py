import os
import argparse

from code_generator import strategic_iterator
from code_generator.openai_util import set_api_key
from code_generator.model.context import Context


def main():
  # Change the script working directory to the main file directory
  main_file_directory = os.path.dirname(os.path.abspath(__file__))
  os.chdir(main_file_directory)

  set_api_key()  # Set the OpenAI API key from the environment variable

  parser = argparse.ArgumentParser(description="Code Generation Tool")
  parser.add_argument("--project_path", type=str,
                      help="Path to the existing project on disk")
  parser.add_argument("--prompt_file_path", type=str,
                      help="with a change request from the user "
                           "(high-level programming_task) for code generation.")

  args = parser.parse_args()

  prompt_file_path = args.prompt_file_path

  # Read the programming_task from the file
  with open(prompt_file_path, "r") as prompt_file:
    high_level_prompt = prompt_file.read()

  context = Context(args.project_path, args.prompt_file_path, high_level_prompt)
  strategic_iterator.highlevel_processing(context)


if __name__ == "__main__":
  main()
