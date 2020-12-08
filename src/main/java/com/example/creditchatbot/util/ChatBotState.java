package com.example.creditchatbot.util;

import com.example.creditchatbot.model.Client;
import com.example.creditchatbot.util.staticDB.ChatBotMessagesDB;
import com.example.creditchatbot.util.staticDB.CitiesDB;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.example.creditchatbot.util.ChatBotCommonConstants.*;

public enum ChatBotState implements IChatBotState {

    JOIN {
    },
    DECLINE {
        @Override
        public ChatBotState nextState() {
            return END_SESSION;
        }
    },
    END_SESSION {
    },
    ERROR {
    },
    INIT {
        @Override
        public ChatBotState nextState() {
            return CITY;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            return this.nextState();
        }
    },
    CITY {
        @Override
        public ChatBotState nextState() {
            return CHECK_CLIENT;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_NAME)) {
                if (CitiesDB.isInDb(content)) {
                    client.setCity(content);

                    return this.nextState();
                }

                return this.nextDeclineState();
            }

            return this.nextOnErrorState();
        }
    },
    CHECK_CLIENT {
        @Override
        public ChatBotState nextState() {
            return AMOUNT;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (YES_ANSWER.equals(content)) {
                return this.nextState();
            } else if (NO_ANSWER.equals(content)) {
                return this.nextDeclineState();
            }

            return this.nextOnErrorState();
        }
    },
    AMOUNT {
        public ChatBotState nextState() {
            return TERM;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_NUM)) {
                try {
                    int res = Integer.parseInt(content);

                    if (res >= ChatBotMessagesDB.AMOUNT_LOWER_BOUND && res <= ChatBotMessagesDB.AMOUNT_HIGHER_BOUND) {
                        client.setCreditAmount(res);

                        return this.nextState();
                    }

                    return this.nextDeclineState();
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    return this.nextOnErrorState();
                }
            }

            return this.nextOnErrorState();
        }
    },
    TERM {
        @Override
        public ChatBotState nextState() {
            return RATE;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_NUM)) {
                try {
                    int res = Integer.parseInt(content);

                    if (res >= ChatBotMessagesDB.TERM_LOWER_BOUND && res <= ChatBotMessagesDB.TERM_HIGHER_BOUND) {
                        client.setCreditTerm(res);

                        return this.nextState();
                    }

                    return this.nextDeclineState();
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    return this.nextOnErrorState();
                }
            }

            return this.nextOnErrorState();
        }
    },
    RATE {
        @Override
        public ChatBotState nextState() {
            return USER_AGREE;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_DOUBLE_NUM)) {
                try {
                    double res = Double.parseDouble(content);

                    if (res >= ChatBotMessagesDB.RATE_LOWER_BOUND && res <= ChatBotMessagesDB.RATE_HIGHER_BOUND) {
                        client.setCreditRate(res);

                        return this.nextState();
                    }
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    return this.nextOnErrorState();
                }

                return this.nextDeclineState();
            }

            return this.nextOnErrorState();
        }
    },
    USER_AGREE {
        @Override
        public ChatBotState nextState() {
            return LAST_NAME;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (YES_ANSWER.equals(content)) {
                return this.nextState();
            }

            return this.nextDeclineState();
        }
    },
    LAST_NAME {
        @Override
        public ChatBotState nextState() {
            return FIRST_NAME;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_NAME)) {
                client.setLastName(content);

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    FIRST_NAME {
        @Override
        public ChatBotState nextState() {
            return MIDDLE_NAME;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_NAME)) {
                client.setFirstName(content);

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    MIDDLE_NAME {
        @Override
        public ChatBotState nextState() {
            return PASSPORT_DATE;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_NAME)) {
                client.setMiddleName(content);

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    PASSPORT_DATE {
        @Override
        public ChatBotState nextState() {
            return PASSPORT_NUM;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_DATE)) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    LocalDate date = LocalDate.parse(content, formatter);
                    client.setPassportDate(date);

                    return this.nextState();
                } catch (NumberFormatException | DateTimeParseException e) {
                    System.err.println(e.getMessage());
                    return this.nextOnErrorState();
                }
            }

            return this.nextOnErrorState();
        }
    },
    PASSPORT_NUM {
        @Override
        public ChatBotState nextState() {
            return PASSPORT_ORG;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_NUM)) {
                client.setPassportNum(content);

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    PASSPORT_ORG {
        @Override
        public ChatBotState nextState() {
            return BIRTH_DATE;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_FREE_TEXT)) {
                client.setPassportOrg(content);

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    BIRTH_DATE {
        @Override
        public ChatBotState nextState() {
            return PHONE;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_DATE)) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    LocalDate date = LocalDate.parse(content, formatter);
                    LocalDate nowDate = LocalDate.now();

                    int age = Period.between(date, nowDate).getYears();

                    if (age > 21) {
                        client.setBirthDate(date);

                        return this.nextState();
                    }

                    return this.nextDeclineState();
                } catch (NumberFormatException | DateTimeParseException e) {
                    System.err.println(e.getMessage());
                    return this.nextOnErrorState();
                }
            }

            return this.nextOnErrorState();
        }
    },
    PHONE {
        @Override
        public ChatBotState nextState() {
            return ADDRESS;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_NUM)) {
                client.setPhone(content);

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    ADDRESS {
        @Override
        public ChatBotState nextState() {
            return JOB_PLACE;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_FREE_TEXT)) {
                client.setAddress(content);

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    JOB_PLACE {
        @Override
        public ChatBotState nextState() {
            return JOB_POSITION;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_FREE_TEXT)) {
                client.setJobPlace(content);

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    JOB_POSITION {
        @Override
        public ChatBotState nextState() {
            return JOB_EXP;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_FREE_TEXT)) {
                client.setJobPosition(content);

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    JOB_EXP {
        @Override
        public ChatBotState nextState() {
            return SUCCESS;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            if (content.matches(REGEX_FREE_TEXT)) {
                client.setJobExp(content);

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    SUCCESS {
        @Override
        public ChatBotState nextState() {
            return SUCCESS;
        }

        @Override
        public ChatBotState processState(String content, Client client) {
            return SUCCESS;
        }
    }
}
