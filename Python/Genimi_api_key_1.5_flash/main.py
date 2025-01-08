from flask import Flask, render_template, request, jsonify
import requests
import json
app = Flask(__name__)
api_key = "Your_API"
def ask_gemini(question, conversation_history):
    url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key={}".format(api_key)
    headers = {
        'Content-Type': 'application/json'
    }
    contents = []
    for turn in conversation_history:
        contents.append({"role": turn["role"], "parts": [{"text": turn["text"]}]})
    contents.append({"role": "user", "parts": [{"text": question}]})
    data = {
        "contents": contents
    }
    try:
        response = requests.post(url, headers=headers, data=json.dumps(data))
        response.raise_for_status()
        response_json = response.json()
        if response_json and response_json.get('candidates') and response_json['candidates'][0].get('content') and response_json['candidates'][0]['content'].get('parts'):
            answer = response_json['candidates'][0]['content']['parts'][0].get('text', 'Không tìm thấy câu trả lời')
            conversation_history.append({"role": "user", "text": question})
            conversation_history.append({"role": "model", "text": answer})
            return answer, conversation_history
        else:
            if response_json.get('promptFeedback'):
                block_reason = response_json['promptFeedback']['blockReason']
                return f"Lời nhắc đã bị chặn vì lý do sau: {block_reason}", conversation_history
            return "Không tìm thấy câu trả lời trong phản hồi.", conversation_history

    except requests.exceptions.RequestException as e:
        return f"Lỗi yêu cầu: {e}", conversation_history
    except (KeyError, IndexError, json.JSONDecodeError) as e:
        return f"Lỗi xử lý phản hồi: {e}", conversation_history
@app.route("/")
def index():
    return render_template("index.html")
@app.route("/chat", methods=["POST"])
def chat():
    question = request.json["question"]
    conversation_history = request.json.get("conversation_history", [])
    answer, updated_history = ask_gemini(question, conversation_history)
    return jsonify({"answer": answer, "conversation_history": updated_history})
if __name__ == "__main__":
    app.run(debug=True)