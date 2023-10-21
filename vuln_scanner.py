import os
import argparse

import uploader
from app import strategic_iterator
from app.openai_util import set_api_key
from app.model.context import Context


def main():
  # Change the script working directory to the main file directory
  main_file_directory = os.path.dirname(os.path.abspath(__file__))
  os.chdir(main_file_directory)

  set_api_key()  # Set the OpenAI API key from the environment variable

  parser = argparse.ArgumentParser(description="Vulnerability scanner Tool")
  parser.add_argument("--project_path", type=str,
                      help="Path to the existing project on disk")

  args = parser.parse_args()
  uploader.upload_code(args.project_path)


if __name__ == "__main__":
  main()
