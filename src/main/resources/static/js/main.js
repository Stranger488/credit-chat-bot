const connectingContainer = document.querySelector('.connecting');
const chatMessages = document.querySelector('.chat-messages');
const messageInput = document.querySelector('.chat-message-input');
const sendMessageButton = document.getElementById('chat-message-form');
const chatContent = document.querySelector('.chat-content');
const errorColor = '#d23e3e';

let stompClient = null;

const start = () => {
    sendMessageButton.addEventListener('submit', sendMessageHandler);
}

const makeConnection = () => {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, onConnection, onError);
}

const onConnection = () => {
    connectingContainer.classList.add('hidden');

    stompClient.subscribe('/channel', onMessageReceived);
}

const onError = () => {
    connectingContainer.innerHTML = 'Нет соединения с сервером.';

    connectingContainer.classList.remove('hidden');
    connectingContainer.style.color = errorColor;
    chatContent.scrollTop = chatContent.scrollHeight;
}

const onMessageReceived = (message) => {
    const msg = JSON.parse(message.body);

    const msgContainer = document.createElement('div');
    msgContainer.className = 'chat-message';
    msgContainer.innerHTML = msg.content;
    chatMessages.appendChild(msgContainer)

    if (msg.sender === 'server') {
        msgContainer.classList.add('chat-message-left');
    } else {
        msgContainer.classList.add('chat-message-right');
    }

    chatContent.scrollTop = chatContent.scrollHeight;
}

const sendMessageHandler = (event) => {
    const messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        const chatMessage = {
            sender: 'client',
            content: messageContent
        };
        stompClient.send('/credit-chat-bot/send', {}, JSON.stringify(chatMessage));

        messageInput.value = '';
    }

    event.preventDefault();
}

document.addEventListener('DOMContentLoaded', () => {
    makeConnection();
    start();
});
