import json


class FunctionCall:
  """
  A class that encapsulates function call at model completion

  Attributes:
  """

  def __init__(self, function_call_object):
    self.name = function_call_object["name"]
    self.arguments = json.loads(function_call_object["arguments"])


  def to_payload(self):
    return {'name': self.name,
            'arguments': json.dumps(self.arguments, indent=None)}

