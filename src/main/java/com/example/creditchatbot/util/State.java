package com.example.creditchatbot.util;

import com.example.creditchatbot.util.staticDB.ChatBotMessagesDB;
import com.example.creditchatbot.util.staticDB.CitiesDB;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

public enum State implements IState {

    JOIN {

    },
    DECLINE {

    },
    ERROR {

    },
    INIT {
        @Override
        public State nextState() {
            return CITY;
        }

        @Override
        public State processState(String content) {
            return this.nextState();
        }
    },
    CITY {
        @Override
        public State nextState() {
            return CHECK_CLIENT;
        }

        @Override
        public State processState(String content) {
            if (content.matches("^[а-я]+(?:[ -][а-я]+)*$")) {
                if (CitiesDB.isInDb(content)) {
                    return this.nextState();
                }

                return this.nextDeclineState();
            }

            return this.nextOnErrorState();
        }
    },
    CHECK_CLIENT {
        @Override
        public State nextState() {
            return AMOUNT_TERM;
        }

        @Override
        public State processState(String content) {
            if ("да".equals(content)) {
                return this.nextState();
            } else if ("нет".equals(content)) {
                return this.nextDeclineState();
            }

            return this.nextOnErrorState();
        }
    },
    AMOUNT_TERM {
        @Override
        public State nextState() {
            return RATE;
        }

        @Override
        public State processState(String content) {
            if (content.matches("^[0-9]+([0-9 .]+)*$")) {
                String[] res = content.split(" ");

                if (res.length == 2) {
                    try {
                        int[] resInt = Arrays.stream(res).mapToInt(Integer::parseInt).toArray();

                        if (resInt[0] >= ChatBotMessagesDB.AMOUNT_LOWER_BOUND && resInt[0] <= ChatBotMessagesDB.AMOUNT_HIGHER_BOUND
                                && resInt[1] >= ChatBotMessagesDB.TERM_LOWER_BOUND && resInt[1] <= ChatBotMessagesDB.TERM_HIGHER_BOUND) {
                            return this.nextState();
                        }

                        return this.nextDeclineState();
                    } catch (NumberFormatException e) {
                        System.err.println(e.getMessage());
                        return this.nextOnErrorState();
                    }
                }
            }

            return this.nextOnErrorState();
        }
    },
    RATE {
        @Override
        public State nextState() {
            return USER_AGREE;
        }

        @Override
        public State processState(String content) {
            if (content.matches("^[0-9]+([0-9 .]+)*$")) {
                try {
                    double res = Double.parseDouble(content);

                    if (res >= ChatBotMessagesDB.RATE_LOWER_BOUND && res <= ChatBotMessagesDB.RATE_HIGHER_BOUND) {
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
        public State nextState() {
            return NAME_DATA;
        }

        @Override
        public State processState(String content) {
            if ("да".equals(content)) {
                return this.nextState();
            }

            return this.nextDeclineState();
        }
    },
    NAME_DATA {
        @Override
        public State nextState() {
            return PASSPORT_DATA;
        }

        @Override
        public State processState(String content) {
            if (content.matches("^[а-я]+(?:[ -][а-я]+)*$")) {
                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    PASSPORT_DATA {
        @Override
        public State nextState() {
            return BIRTH_DATE;
        }

        @Override
        public State processState(String content) {
            if (content.matches("^[0-9]+([0-9а-я .;,-]+)*$")) {
                String[] res = content.split("; ");

                if (res.length == 3) {
                    // TODO: save passportDate to repository

                    return this.nextState();
                }
            }

            return this.nextOnErrorState();
        }
    },
    BIRTH_DATE {
        @Override
        public State nextState() {
            return PHONE;
        }

        @Override
        public State processState(String content) {
            if (content.matches("^[0-9]+(?:[.][0-9]+)*$")) {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    LocalDate date = LocalDate.parse(content, formatter);
                    LocalDate nowDate = LocalDate.now();

                    int age = Period.between(date, nowDate).getYears();

                    if (age > 21) {
                        // TODO: save data to repo

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
        public State nextState() {
            return ADDRESS;
        }

        @Override
        public State processState(String content) {
            if (content.matches("^[0-9]+([0-9]+)*$")) {
                // TODO: save phone to repo

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    ADDRESS {
        @Override
        public State nextState() {
            return JOB_INFO;
        }

        @Override
        public State processState(String content) {
            if (content.matches("^[0-9а-я]+([0-9а-я .;,-]+)*$")) {
                // TODO: save address to repo

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    JOB_INFO {
        @Override
        public State nextState() {
            return SUCCESS;
        }

        @Override
        public State processState(String content) {
            if (content.matches("^[0-9а-я]+([0-9а-я .;,-]+)*$")) {
                // TODO: save address to repo

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    SUCCESS {

    }
}
