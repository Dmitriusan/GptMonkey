import os


def list_files(at_path):
  """
  Recursively list all files within a directory (excluding subdirectories).

  Args:
      at_path (str): The path to the directory.

  Returns:
      List[str]: A list of file paths relative to project_path.
  """
  file_list = []

  # Ensure the given path exists
  if not os.path.exists(at_path):
    return file_list

  # Iterate through items in the directory
  for item in os.listdir(at_path):
    item_path = os.path.join(at_path, item)

    # Check if it's a file
    if os.path.isfile(item_path):
      # Make the file path relative to project_path
      relative_path = os.path.relpath(item_path, at_path)
      file_list.append(relative_path)
    # If it's a directory, recursively call the function
    elif os.path.isdir(item_path):
      file_list.extend(list_files(item_path))

  return file_list


def print_files(project_location, path_within_project):
  """
  Lists files and directories in the specified directory or a project
  (non-recursively).

  Args:
    project_location (str): The absolute path to the root of the project
    path_within_project (str): path within a project directory,
      with imaginary "project/" directory specified as a content root.


  Returns:
    str: A string containing the names of files and directories in the
    specified directory, separated by '\n' and with a "/" suffix for
    directory names.
  """
  abs_path = os.path.join(project_location, path_within_project)
  dir_entries = os.listdir(abs_path)

  # Add a "/" suffix to directory names and join them with '\n'
  file_entries = [
    entry + "/" if os.path.isdir(os.path.join(abs_path, entry)) else entry
    for entry in dir_entries]
  file_entries.sort()
  entries_merged = '\n'.join(file_entries)

  return f"# ls project/{path_within_project}\n{entries_merged}"
