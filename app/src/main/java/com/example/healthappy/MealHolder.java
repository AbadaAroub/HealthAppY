package com.example.healthappy;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MealHolder extends RecyclerView.ViewHolder {

    TextView tvDate, tvTime, tvMeal,tvComment;
    public MealHolder(@NonNull View itemView) {
        super(itemView);

        tvDate = itemView.findViewById(R.id.tvItemDate);
        tvTime = itemView.findViewById(R.id.tvItemTime);
        tvMeal = itemView.findViewById(R.id.tvItemMeal);
        tvComment = itemView.findViewById(R.id.tvItemComment);

    }
}
