from app.model.completion import EvaluatedCompletion
from app.model.conversation import Conversation
from app.model.conversation_step import ConversationStep
from app.model.prompt import Prompt


class Context:
  """
    A class to represent the current context for code generation. It follows
    the "Context Object" pattern

    Attributes:
        project_path (str): The path to the existing project on disk.
        prompt_file_path (str): The path to the text file with a change request
          from the user (high-level prompt) for code generation.
        programming_task (str): The high-level prompt content as a string.
    """
  def __init__(self, project_path, prompt_file_path, programming_task):
    self.project_path = project_path
    self.prompt_file_path = prompt_file_path
    self.programming_task = programming_task

    # Auto populated during execution
    self.goal_reached = False
    self.iteration_count = 0
    self.current_status = "Kickstarting..."
    self.prompt_tokens_used = 0
    self.completion_tokens_used = 0
    self.total_tokens_used = 0

    # List of pairs (Prompt, content str, completion_issue_msg str)
    self.conversation = Conversation()

    self.completion_error_message = None

  def write_down(self, prompt: Prompt, completion: EvaluatedCompletion):
    self.conversation.append(ConversationStep(prompt, completion))

  def write_down_completion_issue(self, completion_err_str: str):
    self.conversation.history[-1].completion.issues.append(completion_err_str)
