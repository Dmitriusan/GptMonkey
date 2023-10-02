from app.model.conversation_step import ConversationStep


class Prompt:
  """
  A class that encapsulates the prompt to OpenAI.

  Attributes:
    system_prompt (str or None): The system user role prompt. Can be None.
    user_prompt (str or None): The user role prompt. Can be None.
  """

  def __init__(self, user_prompt=None, system_prompt=None, functions=None,
      function_call=None):
    """
    Initializes a new Prompt object with user and system prompts.

    Args:
      system_prompt (str or None): The system user role prompt. Can be None.
      user_prompt (str or None): The user role prompt. Can be None.
    """
    self.system_prompt = system_prompt
    self.user_prompt = user_prompt
    self.functions = functions
    self.function_call = function_call

  def __str__(self):
    """
    Returns a string representation of the Prompt object.

    Returns:
      str: A string representation of the object.
    """
    system_prompt_str = (f"System Prompt: {self.system_prompt}\n"
                         if self.system_prompt is not None else "")
    user_prompt_str = (f"User Prompt: {self.user_prompt}\n"
                       if self.user_prompt is not None
                       else "")
    functions_str = f"FUN/{self.function_call}\n" if self.functions else ""

    return f"{system_prompt_str}{user_prompt_str}{functions_str}"

  def to_messages(self):
    """
    Converts the prompt into a list of messages.

    Returns:
      list: A list of messages as dictionaries.
    """
    messages = []
    if self.system_prompt is not None:
        messages.append({"role": "system", "content": self.system_prompt})
    if self.user_prompt is not None:
        messages.append({"role": "user", "content": self.user_prompt})
    return messages
