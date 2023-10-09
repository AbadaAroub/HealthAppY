package com.example.healthappy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class FragmentMealPlan extends Fragment {

    private TextView textView;
    private Button button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_plan, container, false);

        textView = view.findViewById(R.id.text);
        button = view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(); // Open date picker dialog
            }
        });

        return view;
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Showing the picked date value in the textView
                String date = String.valueOf(year) + "." + String.valueOf(month + 1) + "." + String.valueOf(day);
                textView.setText(date);

                // After selecting a date, open the time picker
                openTimePicker();
            }
        }, year, month, day);

        // Add OK and Cancel buttons
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "OK", datePickerDialog);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", datePickerDialog);

        datePickerDialog.show();
    }

    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                // Showing the picked time value in the textView
                String time = String.format("%02d:%02d", hour, minute);
                textView.append(" " + time);
            }
        }, hour, minute, true);

        // Add OK and Cancel buttons
        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "OK", timePickerDialog);
        timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Cancel", timePickerDialog);

        timePickerDialog.show();
    }
}
