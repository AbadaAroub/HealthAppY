package com.example.healthappy;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class MealAdapter extends RecyclerView.Adapter<MealHolder> implements MealEditDialog.MealEditDialogListener{

    Context context;
    List<Meal> meals;
    private FragmentManager fragmentManager;
    private String user;

    public MealAdapter(Context context, List<Meal> meals, FragmentManager fragmentManager, String user) {
        this.meals = meals;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.user = user;
    }

    @NonNull
    @Override
    public MealHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MealHolder(LayoutInflater.from(context).inflate(R.layout.meal_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MealHolder holder, int position) {
        String date, time, meal, comment;
        date = meals.get(position).getDate();
        time = meals.get(position).getTime_of_day();
        meal = meals.get(position).getType().toString();
        comment = meals.get(position).getComment();
        holder.tvDate.setText(date);
        holder.tvTime.setText(time);
        holder.tvMeal.setText(meal);
        holder.tvComment.setText(comment);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create and show a dialog

                MealEditDialog editDialog = new MealEditDialog(user, date, time, meal, comment);
                editDialog.show(fragmentManager, "Meal Edit Dialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }

    @Override
    public void editMeal(String user, String date, String time, String meal, String comment) {
        Log.d("MealAdapter", "ran editMeal");
    }

    @Override
    public void removeMeal(String user, String date, String time) {
        Log.d("MealAdapter", "ran removeMeal");
    }
}
