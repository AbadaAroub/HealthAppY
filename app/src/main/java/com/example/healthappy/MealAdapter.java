package com.example.healthappy;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MealAdapter extends RecyclerView.Adapter<MealHolder> {

    Context context;
    List<Meal> meals;

    public MealAdapter(Context context, List<Meal> meals) {
        this.meals = meals;
        this.context = context;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Meal Details");
                builder.setMessage("Date: " + date + "\nTime: " + time + "\nMeal: " + meal + "\nComment: " + comment);
                builder.setPositiveButton("OK", null); // No action needed for the OK button
                builder.setNegativeButton("CANCEL", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
}
