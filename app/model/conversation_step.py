from app.model.completion import EvaluatedCompletion
from app.model.prompt import Prompt


class ConversationStep:
  """
  A class that represents a step in a conversation, consisting of a prompt and its evaluated content.

  Attributes:
    prompt (Prompt): The user and system prompts of the conversation step.
    completion (EvaluatedCompletion): The evaluated content generated in response to the prompt.
  """

  def __init__(self, prompt: Prompt, completion: EvaluatedCompletion):
    """
    Initializes a new ConversationStep object.

    Args:
      prompt (Prompt): The user and system prompts of the conversation step.
      completion (EvaluatedCompletion): The evaluated content generated in response to the prompt.
    """
    self.prompt = prompt
    self.completion = completion

  def to_messages(self):
    """
    Converts the step into a list of messages.

    Returns:
      list: A list of messages (prompt(s) + content).
    """
    prompt_messages = self.prompt.to_messages()
    completion_messages = self.completion.to_messages()
    return prompt_messages + completion_messages

  def completion_has_issues(self):
    """
    Checks if the content has any issues.

    Returns:
      bool: True if there are issues with the content, False otherwise.
    """
    return len(self.completion.issues) > 0
