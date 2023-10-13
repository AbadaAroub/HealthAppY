package com.example.healthappy;


import android.content.Context;
import android.view.LayoutInflater;
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
        holder.tvDate.setText(meals.get(position).getDate());
        holder.tvTime.setText(meals.get(position).getTime_of_day());
        holder.tvMeal.setText(meals.get(position).getType().toString());
        holder.tvComment.setText(meals.get(position).getComment());

    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
}
