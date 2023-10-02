from typing import List

from app.model.conversation_step import ConversationStep


class Conversation:
  """
    A class that encapsulates a conversation history.

  Attributes:
    history (List[ConversationStep]): A list of conversation steps.
  """

  def __init__(self):
    """
      Initializes a new Conversation object.
    """
    self.history: List[ConversationStep] = []

  def __str__(self):
    """
    Returns a string representation of the Conversation object.

    Returns:
        str: A string representation of the object.
    """
    return f"Conversation history: {len(self.history)} steps"

  def append(self, conversation_step: ConversationStep):
    """
    Appends a conversation step to the conversation history.

    Args:
      conversation_step (ConversationStep): The conversation step to be
      appended to the history.

    Returns:
      None
    """
    self.history.append(conversation_step)

  def to_messages(self):
    """
    Converts the Conversation object into a list of messages.

    Returns:
        list: A list of messages as dictionaries.
    """
    messages = []
    for conversation_step in self.history:
      messages.extend(conversation_step.to_messages())
    return messages
