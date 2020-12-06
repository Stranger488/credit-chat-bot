package com.example.creditchatbot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CitiesDB {
    private static final List<String> citiesArrayList = new ArrayList<>(
            Stream.of(
            "москва", "санкт-петербург", "ростов", "самара", "саратов",
            "воронеж", "тамбов", "тюмень", "рязань", "ростов-на-дону"
        ).collect(Collectors.toList())
    );

    private CitiesDB() {

    }

    public static boolean isInDb(String city) {
        return citiesArrayList.contains(city);
    }
}
