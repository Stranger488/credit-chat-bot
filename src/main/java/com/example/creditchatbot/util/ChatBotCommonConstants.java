package com.example.creditchatbot.util;

public class ChatBotCommonConstants {
    // ChatBot commands
    public static final String END_CMD = "/end";


    // regex
    public static final String REGEX_NAME = "^[а-я]+(?:[ -][а-я]+)*$";
    public static final String REGEX_NUM = "^[0-9]+([0-9]+)*$";
    public static final String REGEX_DOUBLE_NUM = "^[0-9]+([0-9 .]+)*$";
    public static final String REGEX_DATE = "^[0-9]+([0-9.]+)*$";
    public static final String REGEX_FREE_TEXT = "^[0-9а-я]+([0-9а-я .;,-]+)*$";


    // client answers
    public static final String YES_ANSWER = "да";
    public static final String NO_ANSWER = "нет";

    private ChatBotCommonConstants() {

    }
}
