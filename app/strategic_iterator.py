# tqdm library makes the loops show a smart progress meter
from tqdm import tqdm

from app.context import Context

tqdm.pandas()

"""
How many iterations can pass before iteration is forcefully stopped 
"""
hard_stop_iteration_limit = 5


def highlevel_iteration(context: Context):
  """
  Iterates conversation steps with model till user high-level prompt is
  completed

  context: the current context
  """
  # Initialize the progress bar
  progress_bar = tqdm(total=hard_stop_iteration_limit)
  # Start the loop
  while (not context.goal_reached and
         context.iteration_count < hard_stop_iteration_limit):
    # Update the progress bar description with current status
    progress_bar.set_description(f"Status: {context.current_status}")

    # Update the progress bar with additional information
    progress_bar.set_postfix(
      InputTokensUsed=context.input_tokens_used,
      OutputTokensUsed=context.output_tokens_used,
      TotalTokensUsed=context.input_tokens_used + context.output_tokens_used
    )

    step(context)

    # Update the progress bar
    progress_bar.update(1)
  if context.goal_reached:
    print(f"Goal reached in {context.iteration_count} steps")
  else:
    print("Goal not reached; hard stopped")


def step(context: Context):
  # List files in the project
  # project_files = list_files_in_dir(project_path)

  # Generate code based on the user high_level_prompt and project files
  generated_code = generate_code_with_prompt(high_level_prompt, project_files)
  pass