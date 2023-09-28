import os


def list_files(directory_path):
  """
  Lists files and directories in the specified directory (non-recursively).

  Args:
      directory_path (str): The path to the directory for which to list files
      and directories.

  Returns:
      str: A string containing the names of files and directories in the
      specified directory, separated by '\n' and with a "/" suffix for
      directory names.
  """
  dir_entries = os.listdir(directory_path)

  # Add a "/" suffix to directory names and join them with '\n'
  file_entries = [
    entry + "/" if os.path.isdir(os.path.join(directory_path, entry)) else entry
    for entry in dir_entries]
  file_entries.sort()
  entries_merged = '\n'.join(file_entries)

  return entries_merged
