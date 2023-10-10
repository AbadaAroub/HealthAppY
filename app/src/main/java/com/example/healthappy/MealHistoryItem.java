package com.example.healthappy;

import java.util.Collection;

public class MealHistoryItem extends Meal{
    private String time_of_report;
    private boolean ate;
    public MealHistoryItem(meals type, String time_of_day, String date, Collection<allergies> allergy_list, String time_of_report, boolean ate) {
        super(type, time_of_day, date, allergy_list);
        this.time_of_report = time_of_report;
        this.ate = ate;
    }


}
