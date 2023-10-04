import os


def list_files(project_location, path_within_project):
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
