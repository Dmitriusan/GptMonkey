This repository is an incubator of multiple OpenAI GPT-based utilities for working with source code. As of now, it 
consists of:
- [Batch vulnerability scanner](#batch-vulnerability-scanner)
- [Code Generation Tool leveraging OpenAI GPT APIs](#code-generation-tool-leveraging-openai-gpt-apis)

# Batch vulnerability scanner
The script splits source code files into chunks (trying to split between methods), uploads these files recursively to 
GPT and asks it to scan the code for vulnerabilities. Findings are then grouped and pretty-printed
## Usage 
```shell
export OPENAI_API_KEY=<your API key>
GptMonkey/vuln_scanner.py --project_path /tmp/WordPress/wp-admin
```
## Example of output:
```text
----------------------------------------
File Path: edit-form-comment.php
Finding: Possible XSS vulnerability
Code:
    <div class="misc-pub-section misc-pub-comment-status" id="comment-status">
    <?php _e( 'Status:' ); ?> <span id="comment-status-display">
    <?php
    switch ( $comment->comment_approved ) {
    	case '1':
    		_e( 'Approved' );
    		break;
    	case '0':
    		_e( 'Pending' );
    		break;
    	case 'spam':
    		_e( 'Spam' );
    		break;
    }
    ?>
    </span>
----------------------------------------
File Path: edit-form-comment.php
Finding: Potential Cross-Site Scripting (XSS) vulnerability in the 'comment_status' field
Code:
    <label><input type="radio"<?php checked( $comment->comment_approved, '1' ); ?> name="comment_status" value="1" /><?php _ex( 'Approved', 'comment status' ); ?></label><br />
```

# Code Generation Tool leveraging OpenAI GPT APIs

Welcome to the language-agnostic Code Generation Tool, powered by OpenAI GPT (Generative Pre-trained Transformer) APIs. 
This tool harnesses the capabilities of GPT LLM to iteratively generate code based on user prompts. Think of it as 
of giving a GPT model the "vim" access to your project, allowing it to assist in code creation based on freeform text
user requests.

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