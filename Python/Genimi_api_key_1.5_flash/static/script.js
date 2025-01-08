const chatbox = document.getElementById("chatbox");
const userInput = document.getElementById("userInput");
const sendButton = document.getElementById("sendButton");

let conversationHistory = [];

function addMessageToChatbox(message, sender) {
    const messageDiv = document.createElement("div");
    messageDiv.classList.add("message");

    if (sender === "user") {
        messageDiv.classList.add("user-message");
    } else {
        messageDiv.classList.add("gemini-message");
    }
    const content = document.createElement("div");
    content.classList.add("content");
    content.textContent = message;
    messageDiv.appendChild(content);

    chatbox.appendChild(messageDiv);
    chatbox.scrollTop = chatbox.scrollHeight;
}

function sendMessage() {
    const question = userInput.value;
    if (question.trim() === "") return;

    addMessageToChatbox(question, "user");
    userInput.value = "";

    fetch("/chat", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ question: question, conversation_history: conversationHistory })
    })
    .then(response => response.json())
    .then(data => {
        addMessageToChatbox(data.answer, "gemini");
        conversationHistory = data.conversation_history;
    })
    .catch(error => {
        console.error("Error:", error);
        addMessageToChatbox("Error: Could not get a response.", "gemini");
    });
}

sendButton.addEventListener("click", sendMessage);
userInput.addEventListener("keyup", (event) => {
    if (event.key === "Enter") {
        sendMessage();
    }
});

const darkModeToggle = document.getElementById("darkModeToggle");

// light or night
function toggleDarkMode() {
    document.body.classList.toggle("dark-mode");

    if (document.body.classList.contains("dark-mode")) {
        localStorage.setItem("darkMode", "enabled");
    } else {
        localStorage.setItem("darkMode", "disabled");
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const darkModeSetting = localStorage.getItem("darkMode");
    if (darkModeSetting === "enabled") {
        document.body.classList.add("dark-mode");
    }
});

// Gắn sự kiện click cho nút chuyển đổi
darkModeToggle.addEventListener("click", toggleDarkMode);