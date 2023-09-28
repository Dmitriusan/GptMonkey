class EvaluatedCompletion:
  """
  A class that encapsulates the parsed and processed completion, along with
  information about errors or issues it produces

    Attributes:
        completion (str): Completion from a model.
        issues (List[str]): Information about issues related to this completion.
    """

  def __init__(self, completion, issues):
    """
    Initializes a new EvaluatedCompletion object.

        Args:
            completion (str): Completion from a model.
            issues (List[str]): Information about issues related to this completion.
        """
    self.completion = completion
    self.issues = issues or []

  def __str__(self):
    """
    Returns a string representation of the EvaluatedCompletion object.

    Returns:
        str: A string representation of the object.
    """
    completion_str = f"Completion: {self.completion}" if self.completion is not None else "Completion: None"
    issues_str = "\n".join(
      [f"Issue {i + 1}: {issue}" for i, issue in enumerate(self.issues)])

    return f"{completion_str}\n{issues_str}"

  def to_message(self):
    """
    Converts the EvaluatedCompletion into a list of messages.

    Returns:
        list: A singleton list of assistant message.
    """
    return [{"role": "assistant", "content": self.completion}]
