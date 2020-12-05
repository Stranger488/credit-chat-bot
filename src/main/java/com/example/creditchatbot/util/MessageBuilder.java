package com.example.creditchatbot.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MessageBuilder {
    private final HashMap<ChatBot.State, String> chatBotPhrases = new HashMap<>();

    public MessageBuilder() {
        chatBotPhrases.put(ChatBot.State.IS_BANK_CLIENT,
                "Вас приветствует чат-бот для оформления заявки на потребительский кредит! " +
                "Пожалуйста, ответьте на ряд вопросов для оформления заявки. " +
                "Являетесь ли вы клиентом нашего банка?");

        chatBotPhrases.put(ChatBot.State.IS_CITIZEN,
                "Являетесь ли вы гражданином Российской Федерации?");

        chatBotPhrases.put(ChatBot.State.CITY,
                "В каком городе вы проживаете?");

        chatBotPhrases.put(ChatBot.State.JOB,
                "Работете официально более 3-х месяцев?");

        chatBotPhrases.put(ChatBot.State.INCOME,
                "Ваш доход более 30 000 рублей и вы можете подтвердить его по форме 2-НДФЛ или выпиской из ПФР?");

        chatBotPhrases.put(ChatBot.State.AMOUNT_TERM,
                "Кредит на какую сумму и на какой срок вы хотели бы оформить?");

        chatBotPhrases.put(ChatBot.State.RATE,
                "Укажите предполагаемую ставку по кредиту.");

        chatBotPhrases.put(ChatBot.State.AGE,
                "Вам более 21 года (но будет не более 67 лет на момент погашения кредита)?");
    }

    public String buildMessageFromState(ChatBot.State state) {
        return chatBotPhrases.get(state);
    }

}
