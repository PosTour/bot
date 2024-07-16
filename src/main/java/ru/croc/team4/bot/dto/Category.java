package ru.croc.team4.bot.dto;

import java.util.HashMap;
import java.util.Map;

public enum Category {
    BAD("Боковушка"),
    GOOD("Не до поцелуев"),
    EXCELLENT("Полное погружение");

    private final String russianName;

    Category(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }

    private static final Map<String, Category> LOOKUP_MAP2 = new HashMap<>();

    static {
        for (Category type : values()) {
            LOOKUP_MAP2.put(type.russianName, type);
        }
    }

    public static Category getStatusByString(String type) {
        return LOOKUP_MAP2.get(type);
    }
}

