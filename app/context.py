class Context:
  """
    A class to represent the current context for code generation. It follows
    the "Context Object" pattern

    Attributes:
        project_path (str): The path to the existing project on disk.
        prompt_file_path (str): The path to the text file with a change request
          from the user (high-level prompt) for code generation.
        high_level_prompt (str): The high-level prompt content as a string.
    """
  def __init__(self, project_path, prompt_file_path, high_level_prompt):
    self.project_path = project_path
    self.prompt_file_path = prompt_file_path
    self.high_level_prompt = high_level_prompt

    # Auto populated during execution
    self.goal_reached = False
    self.iteration_count = 0
    self.current_status = "Kickstarting..."
    self.input_tokens_used = 0
    self.output_tokens_used = 0

