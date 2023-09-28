import os


def list_files_and_directories_in_dir(directory_path):
  # List files and directories in the directory (non-recursively)
  dir_entries = os.listdir(directory_path)

  # Add a "/" suffix to directory names and join them with '\n'
  file_entries = [
    entry + "/" if os.path.isdir(os.path.join(directory_path, entry)) else entry
    for entry in dir_entries]
  file_entries.sort()
  entries_merged = '\n'.join(file_entries)

  return entries_merged
