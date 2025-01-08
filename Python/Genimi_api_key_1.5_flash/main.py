import requests
import json

def ask_gemini(question, api_key, conversation_history):
    """
    Sends a question to the Gemini API, maintains conversation history, and returns the answer.

    Args:
        question: The current question string.
        api_key: Your Gemini API key.
        conversation_history: A list of dictionaries representing the conversation history.

    Returns:
        A tuple: (answer string from Gemini, updated conversation history) or (error message, original conversation history).
    """

    url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key={}".format(api_key)

    headers = {
        'Content-Type': 'application/json'
    }

    # Build the conversation history in the required format
    contents = []
    for turn in conversation_history:
        contents.append({"role": turn["role"], "parts": [{"text": turn["text"]}]})

    # Add the current question
    contents.append({"role": "user", "parts": [{"text": question}]})

    data = {
        "contents": contents
    }

    try:
        response = requests.post(url, headers=headers, data=json.dumps(data))
        response.raise_for_status()

        response_json = response.json()

        if response_json and response_json.get('candidates') and response_json['candidates'][0].get('content') and response_json['candidates'][0]['content'].get('parts'):
            answer = response_json['candidates'][0]['content']['parts'][0].get('text', 'No answer found')

            # Update conversation history
            conversation_history.append({"role": "user", "text": question})
            conversation_history.append({"role": "model", "text": answer})

            return answer, conversation_history
        else:
            if response_json.get('promptFeedback'):
                block_reason = response_json['promptFeedback']['blockReason']
                return f"Prompt was blocked for the following reason: {block_reason}", conversation_history
            return "No answer found in the response or no 'candidates' in response.", conversation_history

    except requests.exceptions.RequestException as e:
        return f"Request error: {e}", conversation_history
    except (KeyError, IndexError, json.JSONDecodeError) as e:
        return f"Response processing error: {e}", conversation_history

# --- Main execution ---
api_key = "YOUR_ACTUAL_API_KEY"  # **REPLACE THIS WITH YOUR REAL API KEY**

# Initialize conversation history
conversation_history = []

while True:
    question = input("You: ")

    if question.lower() == "exit":
        break

    answer, conversation_history = ask_gemini(question, api_key, conversation_history)
    print(f"Gemini: {answer}")
