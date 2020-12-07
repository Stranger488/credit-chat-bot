package com.example.creditchatbot.util.staticDB;

import com.example.creditchatbot.util.State;

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

    private static final HashMap<State, String> chatBotPhrases = new HashMap<State, String>() {{
        put(
            State.JOIN,
            "Вас приветствует чат-бот для оформления заявки на потребительский кредит! "
        );
        put(
            State.INIT,
            "Начат новый сеанс. " +
            "Пожалуйста, ответьте на ряд вопросов для оформления заявки. "
        );
        put(
            State.ERROR,
            "Неправильный формат введенных данных. Пожалуйста, повторите ввод в соответствии с примером. "
        );
        put(
            State.DECLINE,
            "В таком случае, мы не можем предоставить Вам кредит. " +
            "Текущий сеанс завершается. " +
            "Чтобы начать новый сеанс, пришлите любое сообщение. "
        );
        put(
            State.CHECK_CLIENT,
            "1. Являетесь ли вы гражданином Российской Федерации? " +
            "2. Работаете официально более 3-х месяцев? " +
            "3. Вам более 21 года? " +
            "4. Ваш доход более 30 000 рублей и вы можете подтвердить его по форме 2-НДФЛ или выпиской из ПФР? " +
            "Если вы готовы ответить на все 4 вопроса положительно, то напишите \"Да\", иначе напишите \"Нет\". "
        );
        put(
            State.CITY,
            "В каком городе вы проживаете? (Мы предоставляем кредит в городах, где есть наше обслуживание. " +
            "Полный список таких городов можно получить на главном сайте). "
        );
        put(
            State.AMOUNT_TERM,
            "Кредит на какую сумму и на какой срок вы хотели бы оформить? " +
            "(Перечислите через пробел в виде чисел сумму в рублях и количество месяцев. " +
            "Например: 50000 12). " +
            "Мы принимаем кредиты на сумму от " + AMOUNT_LOWER_BOUND +
            " до " + AMOUNT_HIGHER_BOUND + " рублей и сроком от " + TERM_LOWER_BOUND +
            " до " + TERM_HIGHER_BOUND + " месяцев. "
        );
        put(
            State.RATE,
            "Укажите предполагаемую ставку по кредиту в год. " +
            "(Укажите число в процентах. Например: 15.3). " +
            "Мы принимаем кредиты под проценты  в год от " + RATE_LOWER_BOUND +
            " до " + RATE_HIGHER_BOUND + " процентов. "
        );
        put(
            State.USER_AGREE,
            "Для дальнейшего заполнения заявки мне необходимо Ваше согласие на обработку персональных данных. " +
            "Полный текст согласия доступен по ссылке https://localhost:8080\n " +
            "Напишите \"Да\", если вы согласны. Это действие будет подтверждать Ваше согласие с указанныеми условиями. " +
            "Любое другое сообщение будет воспринято как отказ. "
        );
        put(
            State.NAME_DATA,
            "Введите Ваши фамилию, имя, отчество через пробел. " +
            "(Например: Иванов Иван Иванович). "
        );
        put(
            State.PASSPORT_DATA,
            "Введите Ваши паспортные данные: серию, номер, дату выдачи паспорта и отделение, выдавшее его через знак \";\" c пробелом после него. " +
            "(Например: 0000000000; 01.01.1970; Организация, выдавшая паспорт). "
        );
        put(
            State.BIRTH_DATE,
            "Введите Вашу дату рождения в формате ДД.ММ.ГГГГ. " +
            "(Например: 01.01.1970). "
        );
        put(
            State.PHONE,
            "Введите Ваш мобильный номер телефона для связи с кодом страны без дополнительных знаков. " +
            "(Например: 70000000000 или 30000000000). "
        );
        put(
            State.ADDRESS,
            "Введите Ваш полный адрес. "
        );
        put(
            State.JOB_INFO,
            "Введите информацию о Вашем месте работы: место, должность, стаж. "
        );

        put(
            State.SUCCESS,
            "Спасибо за заявку! Через некоторое время с Вами свяжется наш оператор для подтверждения данных и дальнейшего оформления заявки. " +
            "Чтобы подать еще одну заявку, необходимо перезагрузить страницу. "
        );
    }};


    private static final HashMap<State, String> chatBotDeclinePhrases = new HashMap<State, String>() {{
        put(
            State.CHECK_CLIENT,
            "Необходимо выполнение всех 4-х условий. "
        );
        put(
            State.CITY,
            "Указанный Вами город не обслуживается нашим банком. " +
            "Полный список городов, которые обслуживаются нашим банком можно получить на главном сайте. ");
        put(
            State.AMOUNT_TERM,
            "Мы принимаем кредиты на сумму от " + AMOUNT_LOWER_BOUND +
            " до " + AMOUNT_HIGHER_BOUND + " рублей и сроком от " + TERM_LOWER_BOUND +
            " до " + TERM_HIGHER_BOUND + " месяцев. ");
        put(
            State.RATE,
            "Мы принимаем кредиты под проценты  в год от " + RATE_LOWER_BOUND +
            " до " + RATE_HIGHER_BOUND + " процентов. ");
        put(
            State.USER_AGREE,
            "Без Вашего согласия на обработку персональных данных мы не можем дальше продолжать работу. "
        );
        put(
            State.BIRTH_DATE,
            "Ваш возраст не удовлетворяет требованиям (он должен быть от 21 года). "
        );
    }};

    public static String buildMessageFromState(State state) {
        return chatBotPhrases.get(state);
    }

    public static String buildDeclineMessageFromState(State state) {
        return chatBotDeclinePhrases.get(state);
    }
}
