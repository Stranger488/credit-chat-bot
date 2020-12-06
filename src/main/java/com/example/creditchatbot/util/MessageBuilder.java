package com.example.creditchatbot.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class MessageBuilder {
    private final HashMap<State, String> chatBotPhrases = new HashMap<>();
    private final HashMap<State, String> chatBotDeclinePhrases = new HashMap<>();

    public MessageBuilder() {
        chatBotPhrases.put(State.JOIN,
                "Вас приветствует чат-бот для оформления заявки на потребительский кредит! ");

        chatBotPhrases.put(State.INIT,
                "Начат новый сеанс. " +
                "Пожалуйста, ответьте на ряд вопросов для оформления заявки. ");

        chatBotPhrases.put(State.ERROR,
                "Неправильный формат введенных данных. Пожалуйста, повторите ввод в соответствии с примером. ");

        chatBotPhrases.put(State.DECLINE,
                "Вы не проходите по программе кредитования. " +
                "Текущий сеанс завершается. " +
                "Чтобы начать новый сеанс, пришлите любое сообщение. ");

        chatBotPhrases.put(State.IS_BANK_CLIENT,
                "Являетесь ли вы клиентом нашего банка? (В качестве ответа принимаются фразы \"Да\" или \"Нет\"). ");

        chatBotPhrases.put(State.IS_CITIZEN,
                "Являетесь ли вы гражданином Российской Федерации? (В качестве ответа принимаются фразы \"Да\" или \"Нет\"). ");

        chatBotPhrases.put(State.CITY,
                "В каком городе вы проживаете? (Мы предоставляем кредит в городах, где есть наше обслуживание. " +
                "Полный список таких городов можно получить на главном сайте). ");

        chatBotPhrases.put(State.JOB,
                "Работете официально более 3-х месяцев? (В качестве ответа принимаются фразы \"Да\" или \"Нет\"). ");

        chatBotPhrases.put(State.INCOME,
                "Ваш доход более 30 000 рублей и вы можете подтвердить его по форме 2-НДФЛ или выпиской из ПФР? " +
                        "(В качестве ответа принимаются фразы \"Да\" или \"Нет\"). ");

        chatBotPhrases.put(State.AMOUNT_TERM,
                "Кредит на какую сумму и на какой срок вы хотели бы оформить? " +
                "(Перечислите через пробел в виде чисел сумму в рублях и количество месяцев. " +
                "Например: 50000 12). " +
                "Мы принимаем кредиты на сумму от " + State.AMOUNT_LOWER_BOUND +
                " до " + State.AMOUNT_HIGHER_BOUND + " рублей и сроком от " + State.TERM_LOWER_BOUND +
                " до " + State.TERM_HIGHER_BOUND + " месяцев. ");

        chatBotPhrases.put(State.RATE,
                "Укажите предполагаемую ставку по кредиту в год. " +
                "(Укажите число в процентах. Например: 15.3% или 15.3). " +
                "Мы принимаем кредиты под проценты  в год от " + State.RATE_LOWER_BOUND +
                " до " + State.RATE_HIGHER_BOUND + " процентов. ");

        chatBotPhrases.put(State.AGE,
                "Вам более 21 года (но будет не более 67 лет на момент погашения кредита)? " +
                "(В качестве ответа принимаются фразы \"Да\" или \"Нет\"). ");

        chatBotPhrases.put(State.SUCCESS,
                "Спасибо за заявку! Через некоторое время с Вами свяжется наш оператор для подтверждения данных и дальнейшего оформления заявки. ");


        chatBotDeclinePhrases.put(State.IS_CITIZEN,
                "Вы должны быть гражданином Российской Федерации, чтобы получить кредит. ");

        chatBotDeclinePhrases.put(State.CITY,
                "Указанный Вами город не обслуживается нашим банком. " +
                "Полный список городов, которые обслуживаются нашим банком можно получить на главном сайте. ");

        chatBotDeclinePhrases.put(State.JOB,
                "Вы должны официально работать более 3-х месяцев, чтобы иметь возможность получить кредит. ");

        chatBotDeclinePhrases.put(State.INCOME,
                "Для получения кредита вы должны иметь официальный доход более 30 000 рублей и возможность подтверить его. ");

        chatBotDeclinePhrases.put(State.AMOUNT_TERM,
                "Мы принимаем кредиты на сумму от " + State.AMOUNT_LOWER_BOUND +
                " до " + State.AMOUNT_HIGHER_BOUND + " рублей и сроком от " + State.TERM_LOWER_BOUND +
                " до " + State.TERM_HIGHER_BOUND + " месяцев. ");

        chatBotDeclinePhrases.put(State.RATE,
                "Мы принимаем кредиты под проценты  в год от " + State.RATE_LOWER_BOUND +
                " до " + State.RATE_HIGHER_BOUND + " процентов. ");

        chatBotDeclinePhrases.put(State.AGE,
                "Ваш возраст должен быть более 21 года (и не более 67 лет на момент погашения кредита). ");
    }

    public String buildMessageFromState(State state) {
        return chatBotPhrases.get(state);
    }

    public String buildDeclineMessageFromState(State state) {
        return chatBotDeclinePhrases.get(state);
    }

}
