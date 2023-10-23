package com.example.healthappy;

import java.util.Collection;

public class MealHistoryItem extends Meal{
    private String time_of_report;
    private boolean ate;
    public MealHistoryItem(mealType type, String time_of_day, String date, String comment/*, Collection<allergies> allergy_list*/, String time_of_report, boolean ate) {
        super(type, time_of_day, date, comment/*, allergy_list*/);
        this.time_of_report = time_of_report;
        this.ate = ate;

    }
    public void setTime_of_report(String time_of_report) {
        this.time_of_report = time_of_report;
    }

    public String getTime_of_report() {
        return time_of_report;
    }

    public void setAte(boolean ate) {
        this.ate = ate;
    }

    public boolean isAte() {
        return ate;
    }
}
