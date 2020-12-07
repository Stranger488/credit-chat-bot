package com.example.creditchatbot.util;

import com.example.creditchatbot.model.Client;
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
        public State processState(String content, Client client) {
            return this.nextState();
        }
    },
    CITY {
        @Override
        public State nextState() {
            return CHECK_CLIENT;
        }

        @Override
        public State processState(String content, Client client) {
            if (content.matches("^[а-я]+(?:[ -][а-я]+)*$")) {
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
        public State nextState() {
            return AMOUNT_TERM;
        }

        @Override
        public State processState(String content, Client client) {
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
        public State processState(String content, Client client) {
            if (content.matches("^[0-9]+([0-9 .]+)*$")) {
                String[] res = content.split(" ");

                if (res.length == 2) {
                    try {
                        int[] resInt = Arrays.stream(res).mapToInt(Integer::parseInt).toArray();

                        if (resInt[0] >= ChatBotMessagesDB.AMOUNT_LOWER_BOUND && resInt[0] <= ChatBotMessagesDB.AMOUNT_HIGHER_BOUND
                                && resInt[1] >= ChatBotMessagesDB.TERM_LOWER_BOUND && resInt[1] <= ChatBotMessagesDB.TERM_HIGHER_BOUND) {
                            client.setCreditAmount(resInt[0]);
                            client.setCreditTerm(resInt[1]);

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
        public State processState(String content, Client client) {
            if (content.matches("^[0-9]+([0-9 .]+)*$")) {
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
        public State nextState() {
            return NAME_DATA;
        }

        @Override
        public State processState(String content, Client client) {
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
        public State processState(String content, Client client) {
            if (content.matches("^[а-я]+(?:[ -][а-я]+)*$")) {
                String[] res = content.split(" ");

                if (res.length == 3) {
                    client.setFirstName(res[1]);
                    client.setLastName(res[0]);
                    client.setMiddleName(res[2]);

                    return this.nextState();
                }
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
        public State processState(String content, Client client) {
            if (content.matches("^[0-9]+([0-9а-я .;,-]+)*$")) {
                String[] res = content.split("; ");

                if (res.length == 3) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        LocalDate date = LocalDate.parse(res[1], formatter);
                        client.setPassportDate(date);

                        client.setPassportNum(res[0]);
                        client.setPassportOrg(res[2]);

                        return this.nextState();
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.err.println(e.getMessage());
                        return this.nextOnErrorState();
                    }
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
        public State processState(String content, Client client) {
            if (content.matches("^[0-9]+(?:[.][0-9]+)*$")) {
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
        public State nextState() {
            return ADDRESS;
        }

        @Override
        public State processState(String content, Client client) {
            if (content.matches("^[0-9]+([0-9]+)*$")) {
                client.setPhone(content);

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
        public State processState(String content, Client client) {
            if (content.matches("^[0-9а-я]+([0-9а-я .;,-]+)*$")) {
                client.setAddress(content);

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
        public State processState(String content, Client client) {
            if (content.matches("^[0-9а-я]+([0-9а-я .;,-]+)*$")) {
                client.setJobInfo(content);

                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    SUCCESS {
        @Override
        public State nextState() {
            return SUCCESS;
        }

        @Override
        public State processState(String content, Client client) {
            return this.nextState();
        }
    }
}
