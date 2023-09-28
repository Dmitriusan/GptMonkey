class Prompt:
  """
  A class that encapsulates the prompt to OpenAI.

  Attributes:
      user_prompt (str or None): The user role prompt. Can be None.
      system_prompt (str or None): The system user role prompt. Can be None.
  """

  def __init__(self, user_prompt=None, system_prompt=None):
    """
    Initializes a new Prompt object with user and system prompts.

    Args:
        user_prompt (str or None): The user role prompt. Can be None.
        system_prompt (str or None): The system user role prompt. Can be None.
    """
    self.user_prompt = user_prompt
    self.system_prompt = system_prompt

  def __str__(self):
    """
    Returns a string representation of the Prompt object.

    Returns:
        str: A string representation of the object.
    """
    user_prompt_str = f"User Prompt: {self.user_prompt}" if self.user_prompt is not None else "User Prompt: None"
    system_prompt_str = f"System Prompt: {self.system_prompt}" if self.system_prompt is not None else "System Prompt: None"

    return f"{user_prompt_str}\n{system_prompt_str}"

  def to_messages(self):
    """
    Converts the prompt into a list of messages.

    Returns:
        list: A list of messages as dictionaries.
    """
    messages = []
    if self.user_prompt is not None:
        messages.append({"role": "user", "content": self.user_prompt})
    if self.system_prompt is not None:
        messages.append({"role": "system", "content": self.system_prompt})
    return messages
