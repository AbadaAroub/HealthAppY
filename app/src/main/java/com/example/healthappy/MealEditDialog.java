package com.example.healthappy;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Calendar;

public class MealEditDialog extends AppCompatDialogFragment {
    String[] mealItems = {"Breakfast", "Lunch", "Dinner", "Snack"};
    private String user, date, time, meal, comment;
    private MealEditDialogListener listener;
    AutoCompleteTextView actvMealDropdown;
    ArrayAdapter<String> adapterItems;
    Button btnDialogDate, btnDialogTime;
    EditText etDialogComment;

    public MealEditDialog(String user, String date, String time, String meal, String comment) {
        this.user = user;
        this.date = date;
        this.time = time;
        this.meal = meal;
        this.comment = comment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_meal_edit, null);

        btnDialogDate = (Button) view.findViewById(R.id.btnDialogDate);
        btnDialogTime = (Button) view.findViewById(R.id.btnDialogTime);
        etDialogComment = (EditText) view.findViewById(R.id.etDialogComment);

        btnDialogDate.setText(date);
        btnDialogTime.setText(time);
        etDialogComment.setText(comment);

        actvMealDropdown = (AutoCompleteTextView) view.findViewById(R.id.meal_select_list);
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item, mealItems);
        actvMealDropdown.setAdapter(adapterItems);
        actvMealDropdown.setText(meal, false);

        Log.d("Meal dialog ", date + " " + time + " " + meal + " " + comment);

        // run a function based of what information has changed
        btnDialogDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        btnDialogTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePicker();
            }
        });

        builder.setView(view)
                .setTitle("Edit Meal")
                .setNeutralButton("REMOVE MEAL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("MealEditDialog ", "Remove Meal!");

                        listener.removeMeal(user, date, time);
                    }
                })

                .setNegativeButton("CANCEL", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (changesNeedsRemoval()) { //Remove old data node
                            listener.removeMeal(user, date, time);
                        }//Edit or add node with updated info
                        listener.editMeal(user, date, time,
                                String.valueOf(actvMealDropdown.getText()),
                                String.valueOf(etDialogComment.getText()));
                    }
                });

        return builder.create();
    }

    private boolean changesNeedsRemoval() {
        if (date.equalsIgnoreCase(btnDialogDate.getText().toString()) || time.equalsIgnoreCase(btnDialogTime.getText().toString())) {
            return true;
        }
        return false;
    }

    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (MealEditDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement MealEditDialogListener");
        }
    }

    public interface MealEditDialogListener {
        void editMeal(String user, String date, String time, String meal, String comment);

        void removeMeal(String user, String date, String time);
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Showing the picked date value in the Button
                String date = String.valueOf(year) + "-" + String.valueOf(month + 1) + "-" + String.valueOf(day);
                btnDialogDate.setText(date);
            }
        }, year, month, day);

        // Add OK and Cancel buttons
        datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, getString(R.string.ok), datePickerDialog);
        datePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), datePickerDialog);

        datePickerDialog.show();
    }

    private void openTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                // Showing the picked time value in the Button
                String time = String.format("%02d:%02d", hour, minute);
                btnDialogTime.setText(time);
            }
        }, hour, minute, true);

        // Add OK and Cancel buttons
        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, getString(R.string.ok), timePickerDialog);
        timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, getString(R.string.cancel), timePickerDialog);

        timePickerDialog.show();
    }
}
