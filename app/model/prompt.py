class Prompt:
  """
  A class that encapsulates the prompt to OpenAI.

  Attributes:
    system_prompt (str or None): The system user role prompt. Can be None.
    user_prompt (str or None): The user role prompt. Can be None.
    temperature (float): The temperature parameter for the prompt. Default is 1.0.
  """

  def __init__(self, system_prompt=None, user_prompt=None, functions=None,
               function_call=None, temperature=1.0):
    """
    Initializes a new Prompt object with user and system prompts.

    Args:
      system_prompt (str or None): The system user role prompt. Can be None.
      user_prompt (str or None): The user role prompt. Can be None.
      temperature (float): The temperature parameter for the prompt. Default is 1.0.
    """
    self.system_prompt = system_prompt
    self.user_prompt = user_prompt
    self.functions = functions
    self.function_call = function_call
    self.temperature = temperature  # New attribute for temperature

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
