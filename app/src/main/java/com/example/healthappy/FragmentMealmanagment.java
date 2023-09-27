package com.example.healthappy;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FragmentMealmanagment extends Fragment {
    private CalendarView calendarView;
    private TextView MyDate;
    private Button EditBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_mealmanagment, container,false);
        calendarView = (CalendarView) view.findViewById(R.id.calandarView);
        MyDate = (TextView) view.findViewById(R.id.MyDate);
        EditBtn = (Button) view.findViewById(R.id.calandarBtn);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = year  + "/" + (month + 1) + "/" + dayOfMonth;
                MyDate.setText(date);
            }
        });
        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMealmanagmentEdit();
            }
        });


        return view;



    }
    public void openMealmanagmentEdit(){
       // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentMealManagmentEdit()).commit();

    }
}
