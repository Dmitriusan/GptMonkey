import json


class FunctionCall:
  """
  A class that encapsulates function call at model completion

  Attributes:
  """

  def __init__(self, completion_response):
    if completion_response["function"]:
      self.name = completion_response["function"]["name"]
      self.arguments = json.loads(completion_response["function"]["arguments"])


  def to_payload(self):
    return {'name': self.name,
            'arguments': json.dumps(self.arguments, indent=None)}

