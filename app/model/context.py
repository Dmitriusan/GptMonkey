from app.model.completion import EvaluatedCompletion
from app.model.prompt import Prompt


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
    self.prompt_tokens_used = 0
    self.completion_tokens_used = 0
    self.total_tokens_used = 0

    # List of pairs (Prompt, completion str, completion_issue_msg str)
    self.conversation = []
    # The completion produced some invalid result on a previous step
    self.completion_error_message = None

  def write_down(self, prompt: Prompt, completion: EvaluatedCompletion):
    self.conversation.append((prompt, completion))

  def write_down_completion_error(self, completion_err_str: str):
    self.conversation[-1][1] = completion_err_str
