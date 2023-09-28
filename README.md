# Code Generation Tool leveraging OpenAI GPT APIs

Welcome to the Code Generation Tool, powered by GPT (Generative Pre-trained Transformer) Language Model. This tool 
harnesses the capabilities of GPT to iteratively generate code based on user prompts. Think of it as giving a 
GPT model "vim" access to your project, allowing it to assist in code creation based on freeform text user requests.

## Disclaimer
**⚠️ Disclaimer: This README contains information on features and functionality that have not been implemented yet. 
Almost everything described in this README is rather a placeholder, "vision" or "concept" than an existing 
production-ready thing. Please wait until the first release, or feel free to contribute and suggest ideas. 
Use this document only as a reference for planned features and future developments. ⚠️**

## Key Features

This code generator employs state-of-the-art  techniques to maximize the use of the context window effectively 
by loading the most relevant parts of existing code and requirements
It leverages multiple Language Model (LLM) prompts to ensure that the generated code aligns with the context 
of your existing codebase and adheres to freeform requirements.

## Technologies and Approaches

This tool is built on a foundation of advanced methodologies, including:

- **Chain of Thought:** This technique ensures that the code generation process is a coherent sequence of
  logical steps, increasing the quality and relevance of resulting code

- **ReAct (React and Act):** s at the heart of the code generation process. It begins with a prompt, adapts to the 
 project's context, and continuously learns from the existing codebase to produce the relevant code. It allows the model
 to maintain a "train of thought" across multiple interactions.

- **PAL: Program-aided Language Models** in scope of current project, PAL approach looks like giving the model the 
programmatic access to project files and documentation

For more in-depth information on these approaches, you can refer to the following articles:

- [Chain-of-Thought Prompting Elicits Reasoning in Large Language Models](https://arxiv.org/abs/2201.11903)
- [ReAct: Synergizing Reasoning and Acting in Language Models](https://arxiv.org/abs/2210.03629)
- [PAL: Program-aided Language Models](https://arxiv.org/abs/2211.10435)

## Getting Started

To start using the Code Generation Tool, follow these steps:

1. TBD

## Support and Feedback

If you encounter any issues or have suggestions for improvement, please feel free to 
[open an issue](https://github.com/Dmitriusan/GptMonkey/issues) on the GitHub repository. Your feedback is valuable, 
and the aim is to make this tool as effective as possible for your code generation needs.

Happy coding!