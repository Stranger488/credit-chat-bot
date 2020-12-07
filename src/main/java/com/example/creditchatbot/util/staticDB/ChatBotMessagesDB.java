package com.example.creditchatbot.util.staticDB;

import com.example.creditchatbot.util.ChatBotState;

import java.util.HashMap;

public class ChatBotMessagesDB {
    public static final double RATE_HIGHER_BOUND = 50.5;
    public static final double RATE_LOWER_BOUND = 10.2;
    public static final int TERM_HIGHER_BOUND = 36;
    public static final int TERM_LOWER_BOUND = 6;
    public static final int AMOUNT_HIGHER_BOUND = 100000;
    public static final int AMOUNT_LOWER_BOUND = 20000;

    private ChatBotMessagesDB() {

    }

    private static final HashMap<ChatBotState, String> chatBotPhrases = new HashMap<ChatBotState, String>() {{
        put(
            ChatBotState.JOIN,
            "Вас приветствует чат-бот для оформления заявки на потребительский кредит!<br> "
        );
        put(
            ChatBotState.END_SESSION,
            "Текущий сеанс завершается.<br> " +
            "Чтобы начать новый сеанс, пришлите любое сообщение.<br> "
        );
        put(
            ChatBotState.INIT,
            "Начат новый сеанс.<br> " +
            "Пожалуйста, ответьте на ряд вопросов для оформления заявки.<br> "
        );
        put(
            ChatBotState.ERROR,
            "Неправильный формат введенных данных. Пожалуйста, повторите ввод в соответствии с примером.<br> " +
            "(Если же Вы хотите завершить текущий сеанс, то отправьте \"/end\").<br> "
        );
        put(
            ChatBotState.DECLINE,
            "В таком случае, мы не можем предоставить Вам кредит.<br> "
        );
        put(
            ChatBotState.CHECK_CLIENT,
            "1. Являетесь ли вы гражданином Российской Федерации?<br> " +
            "2. Работаете официально более 3-х месяцев?<br> " +
            "3. Вам более 21 года?<br> " +
            "4. Ваш доход более 30 000 рублей и вы можете подтвердить его по форме 2-НДФЛ или выпиской из ПФР?<br> " +
            "Если вы готовы ответить на все 4 вопроса положительно, то напишите \"Да\", иначе напишите \"Нет\".<br> "
        );
        put(
            ChatBotState.CITY,
            "В каком городе вы проживаете? (Мы предоставляем кредит в городах, где есть наше обслуживание.<br> " +
            "Полный список таких городов можно получить на главном сайте).<br> "
        );
        put(
            ChatBotState.AMOUNT_TERM,
            "Кредит на какую сумму и на какой срок вы хотели бы оформить?<br> " +
            "(Перечислите через пробел в виде чисел сумму в рублях и количество месяцев.<br> " +
            "Например: 50000 12).<br> " +
            "Мы принимаем кредиты на сумму от " + AMOUNT_LOWER_BOUND +
            " до " + AMOUNT_HIGHER_BOUND + " рублей и сроком от " + TERM_LOWER_BOUND +
            " до " + TERM_HIGHER_BOUND + " месяцев. "
        );
        put(
            ChatBotState.RATE,
            "Укажите предполагаемую ставку по кредиту в год.<br> " +
            "(Укажите число в процентах. Например: 15.3).<br> " +
            "Мы принимаем кредиты под проценты  в год от " + RATE_LOWER_BOUND +
            " до " + RATE_HIGHER_BOUND + " процентов. "
        );
        put(
            ChatBotState.USER_AGREE,
            "Для дальнейшего заполнения заявки мне необходимо Ваше согласие на обработку персональных данных.<br> " +
            "Полный текст согласия доступен по ссылке https://localhost:8080<br> " +
            "Напишите \"Да\", если вы согласны. Это действие будет подтверждать Ваше согласие с указанныеми условиями.<br> " +
            "Любое другое сообщение будет воспринято как отказ. "
        );
        put(
            ChatBotState.NAME_DATA,
            "Введите Ваши фамилию, имя, отчество через пробел.<br> " +
            "(Например: Иванов Иван Иванович). "
        );
        put(
            ChatBotState.PASSPORT_DATA,
            "Введите Ваши паспортные данные: серию, номер, дату выдачи паспорта и отделение, выдавшее его через знак \";\" c пробелом после него.<br> " +
            "(Например: 0000000000; 01.01.1970; Организация, выдавшая паспорт).<br> "
        );
        put(
            ChatBotState.BIRTH_DATE,
            "Введите Вашу дату рождения в формате ДД.ММ.ГГГГ.<br> " +
            "(Например: 01.01.1970).<br> "
        );
        put(
            ChatBotState.PHONE,
            "Введите Ваш мобильный номер телефона для связи с кодом страны без дополнительных знаков.<br> " +
            "(Например: 70000000000 или 30000000000).<br> "
        );
        put(
            ChatBotState.ADDRESS,
            "Введите Ваш полный адрес. "
        );
        put(
            ChatBotState.JOB_INFO,
            "Введите информацию о Вашем месте работы: место, должность, стаж.<br> "
        );

        put(
            ChatBotState.SUCCESS,
            "Спасибо за заявку! Через некоторое время с Вами свяжется наш оператор для подтверждения данных и дальнейшего оформления заявки.<br> " +
            "Чтобы подать еще одну заявку, необходимо перезагрузить страницу.<br> "
        );
    }};


    private static final HashMap<ChatBotState, String> chatBotDeclinePhrases = new HashMap<ChatBotState, String>() {{
        put(
            ChatBotState.CHECK_CLIENT,
            "Необходимо выполнение всех 4-х условий.<br> "
        );
        put(
            ChatBotState.CITY,
            "Указанный Вами город не обслуживается нашим банком.<br> " +
            "Полный список городов, которые обслуживаются нашим банком можно получить на главном сайте.<br> ");
        put(
            ChatBotState.AMOUNT_TERM,
            "Мы принимаем кредиты на сумму от " + AMOUNT_LOWER_BOUND +
            " до " + AMOUNT_HIGHER_BOUND + " рублей и сроком от " + TERM_LOWER_BOUND +
            " до " + TERM_HIGHER_BOUND + " месяцев.<br> ");
        put(
            ChatBotState.RATE,
            "Мы принимаем кредиты под проценты  в год от " + RATE_LOWER_BOUND +
            " до " + RATE_HIGHER_BOUND + " процентов.<br> ");
        put(
            ChatBotState.USER_AGREE,
            "Без Вашего согласия на обработку персональных данных мы не можем дальше продолжать работу.<br> "
        );
        put(
            ChatBotState.BIRTH_DATE,
            "Ваш возраст не удовлетворяет требованиям (он должен быть от 21 года).<br> "
        );
    }};

    public static String buildMessageFromState(ChatBotState chatBotState) {
        return chatBotPhrases.get(chatBotState);
    }

    public static String buildDeclineMessageFromState(ChatBotState chatBotState) {
        return chatBotDeclinePhrases.get(chatBotState);
    }
}
