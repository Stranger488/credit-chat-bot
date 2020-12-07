package com.example.creditchatbot.util.staticDB;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CitiesDB {
    private static final List<String> citiesArrayList = Stream.of(
            "москва", "санкт-петербург", "ростов", "самара", "саратов",
            "воронеж", "тамбов", "тюмень", "рязань", "ростов-на-дону",
            "волгоград", "хабаровск"
        ).collect(Collectors.toList());

    private CitiesDB() {

    }

    public static boolean isInDb(String city) {
        return citiesArrayList.contains(city);
    }
}
