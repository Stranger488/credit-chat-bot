package com.example.creditchatbot.util;

import java.util.Arrays;

public enum State implements IState {

    INIT {
        @Override
        public State nextState() {
            return IS_BANK_CLIENT;
        }

        @Override
        public State processState(String content) {
            return this.nextState();
        }
    },
    JOIN {

    },
    IS_BANK_CLIENT {
        @Override
        public State nextState() {
            return IS_CITIZEN;
        }

        @Override
        public State processState(String content) {
            if ("да".equals(content) || "нет".equals(content)) {
                return this.nextState();
            }

            return this.nextOnErrorState();
        }
    },
    IS_CITIZEN {
        @Override
        public State nextState() {
            return CITY;
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
    CITY {
        @Override
        public State nextState() {
            return AMOUNT_TERM;
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
    AMOUNT_TERM {
        @Override
        public State nextState() {
            return RATE;
        }

        @Override
        public State processState(String content) {
            if (content.matches("^[0-9]+([0-9 .]+)*$")) {
                String[] res = content.split(" ");

                int[] resInt;
                try {
                    resInt = Arrays.stream(res).mapToInt(Integer::parseInt).toArray();
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    return this.nextOnErrorState();
                }

                if (resInt[0] >= AMOUNT_LOWER_BOUND && resInt[0] <= AMOUNT_HIGHER_BOUND
                    && resInt[1] >= TERM_LOWER_BOUND && resInt[1] <= TERM_HIGHER_BOUND) {
                    return this.nextState();
                }

                return this.nextDeclineState();
            }

            return this.nextOnErrorState();
        }
    },
    RATE {
        @Override
        public State nextState() {
            return AGE;
        }

        @Override
        public State processState(String content) {
            if (content.matches("^[0-9]+([0-9 .]+)*$")) {

                double res;
                try {
                    res = Double.parseDouble(content);
                } catch (NumberFormatException e) {
                    System.err.println(e.getMessage());
                    return this.nextOnErrorState();
                }

                if (res >= RATE_LOWER_BOUND && res <= RATE_HIGHER_BOUND) {
                    return this.nextState();
                }

                return this.nextDeclineState();
            }

            return this.nextOnErrorState();
        }
    },
    AGE {
        @Override
        public State nextState() {
            return JOB;
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
    JOB {
        @Override
        public State nextState() {
            return INCOME;
        }

        @Override
        public State processState(String content) {
            if ("да".equals(content)) {
                return this.nextState();
            } else if ("нет".equals(content)) {
                return this.nextDeclineState();
            }

            return this;
        }
    },
    INCOME {
        @Override
        public State nextState() {
            return SUCCESS;
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
    SUCCESS {

    },
    DECLINE {

    },
    USER_AGREE {

    },
    ERROR {

    };

    public static final int AMOUNT_LOWER_BOUND = 20000;
    public static final int AMOUNT_HIGHER_BOUND = 100000;

    public static final int TERM_LOWER_BOUND = 6;
    public static final int TERM_HIGHER_BOUND = 36;

    public static final double RATE_LOWER_BOUND = 10.2;
    public static final double RATE_HIGHER_BOUND = 50.5;
}
