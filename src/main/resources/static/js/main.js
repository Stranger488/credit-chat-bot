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

    stompClient.subscribe('/user/channel', onMessageReceived);

    const chatMessage = {
        sender: 'client',
        messageType: 'JOIN',
        content: 'join'
    };
    stompClient.send('/credit-chat-bot/join', {}, JSON.stringify(chatMessage));
}

const onError = () => {
    connectingContainer.innerHTML = 'Нет соединения с сервером.';

    connectingContainer.classList.remove('hidden');
    connectingContainer.style.color = errorColor;
    chatContent.scrollTop = chatContent.scrollHeight;
}

const onMessageReceived = (message) => {
    const msg = JSON.parse(message.body);

    if (msg.messageType === 'JOIN') {
        console.log('Joined to the server.');
    }

    const msgContainer = document.createElement('div');
    msgContainer.className = 'chat-message-container';
    chatMessages.appendChild(msgContainer);

    const nameSpan = document.createElement('span');
    nameSpan.className = 'chat-message-username';
    msgContainer.appendChild(nameSpan);

    const msgDiv = document.createElement('div');
    msgDiv.className = 'chat-message';
    msgDiv.innerHTML = msg.content;
    msgContainer.appendChild(msgDiv);

    if (msg.sender === 'server') {
        nameSpan.innerHTML = 'Чат-бот';
        msgContainer.classList.add('chat-message-container-left');
        msgDiv.classList.add('chat-message-left');
    } else if (msg.sender === 'client') {
        nameSpan.innerHTML = 'Вы';
        msgContainer.classList.add('chat-message-container-right');
        msgDiv.classList.add('chat-message-right');
    } else {
        nameSpan.innerHTML = 'Неизвестный';
        msgContainer.classList.add('chat-message-container-left');
        msgDiv.classList.add('chat-message-left');
    }

    chatContent.scrollTop = chatContent.scrollHeight;
}

const sendMessageHandler = (event) => {
    const messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        const chatMessage = {
            sender: 'client',
            content: messageContent,
            messageType: 'SEND'
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
