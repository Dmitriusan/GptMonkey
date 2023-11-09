import os
import argparse

import uploader
from code_generator import strategic_iterator
from code_generator.openai_util import set_api_key
from code_generator.model.context import Context


def main():
  # Change the script working directory to the main file directory
  main_file_directory = os.path.dirname(os.path.abspath(__file__))
  os.chdir(main_file_directory)

  set_api_key()  # Set the OpenAI API key from the environment variable

  parser = argparse.ArgumentParser(description="Vulnerability scanner Tool")
  parser.add_argument("--project_path", type=str,
                      help="Path to the existing project on disk")
  parser.add_argument("--samples", type=int,
                      help="if specified, process only N source files")

  args = parser.parse_args()
  uploader.upload_code(args)


if __name__ == "__main__":
  main()
