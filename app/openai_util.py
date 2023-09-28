import os
import openai

# Function to set the OpenAI API key from the environment variable
def set_api_key():
  api_key = os.getenv("OPENAI_API_KEY")
  if not api_key:
    raise ValueError("Please set the OPENAI_API_KEY environment variable with your API key.")
  openai.api_key = api_key

# Function to generate code using OpenAI
def generate_code_with_prompt(user_prompt, project_files):
  # Combine user prompt and list of project files
  combined_prompt = user_prompt + "\nProject Files:\n" + "\n".join(project_files)

  response = openai.ChatCompletion.create(
    model="gpt-3.5-turbo",
    messages=[
      {
        "role": "user",
        "content": combined_prompt
      }
    ],
    temperature=1,
    max_tokens=512,
    top_p=1,
    frequency_penalty=0,
    presence_penalty=0
  )

  # Extract and return the generated code
  generated_code = response['choices'][0]['message']['content']
  return generated_code