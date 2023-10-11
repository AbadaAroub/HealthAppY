package com.example.healthappy;

import java.util.ArrayList;
import java.util.Collection;

enum allergies {
    NUTS,
    GLUTEN,
    LACTOS
}

enum meals {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
}

public class Meal {
    private meals type;
    private String time_of_day;
    private String date;
    private String description;
    private ArrayList<allergies> allergy_list;

    public Meal(meals type, String time_of_day, String date, Collection<allergies> allergy_list, String description) {
        this.type = type;
        this.time_of_day = time_of_day;
        this.date = date;
        this.allergy_list = new ArrayList<allergies>();
        allergy_list.addAll(allergy_list);
        this.description = description;

    }
}
