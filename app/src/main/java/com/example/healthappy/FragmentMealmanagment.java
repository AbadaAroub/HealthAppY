package com.example.healthappy;


import static com.example.healthappy.R.layout.fragment_mealmanagment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;


public class FragmentMealmanagment extends Fragment {
    private Button btnSave, btnPickDate, btnPickTime;
    private EditText etComment;

    String[] mealItems = {"Breakfast", "Lunch", "Dinner", "Snack"};
    Resources resources;
    AutoCompleteTextView actvMealDropdown, actvElderDropdown;
    ArrayAdapter<String> adapterItems, adapterUids;
    //Datebase
    TextInputLayout mealEdt;
    DatabaseReference rootRef;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resources = getResources();
        mealItems = resources.getStringArray(R.array.meals);
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseDatabase.getInstance().getReference();

        //Fill under_care
        ArrayList<String> listUsernames = get_usernames();

        View view =inflater.inflate(fragment_mealmanagment, container,false);
        //Fill Select A Meal-dropdown
        actvMealDropdown = view.findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item, mealItems);
        actvMealDropdown.setAdapter(adapterItems);

        //Fill Select Elder-dropdown
        actvElderDropdown = view.findViewById(R.id.elder_select_list);
        adapterUids = new ArrayAdapter<String>(getActivity(), R.layout.list_item, listUsernames);
        actvElderDropdown.setAdapter(adapterUids);

        //Clickables
        btnPickTime = (Button) view.findViewById(R.id.btnTimePicker);
        btnPickDate = (Button) view.findViewById(R.id.btnDatePicker);
        etComment = (EditText) view.findViewById(R.id.mealComment);
        btnSave = (Button) view.findViewById(R.id.savebtn);

        btnPickDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        btnPickTime.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                openTimePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username, date, time, meal, comment;
                username = String.valueOf(actvElderDropdown.getText());
                date = String.valueOf(btnPickDate.getText());
                time = String.valueOf(btnPickTime.getText());
                meal = String.valueOf(actvMealDropdown.getText());
                comment = String.valueOf(etComment.getText());

                if(!isFormCorrect(username, date, time, meal)){
                    return;
                }

                Log.i("addMealToElder", username + " " + date + " " + time + " " + meal + " " + comment);
                addMealToElder(username, date, time, meal, comment);
            }
        });
        return view;
    }

    private void addMealToElder(String username, String date, String time, String meal, String comment){
        mealType type = convertStringToMeal(meal);
        Meal mealNew = new Meal(type, time, date, comment);
        DatabaseReference elderMealRef = FirebaseDatabase.getInstance().getReference().child("Elder").child(username).child("Meals");

        elderMealRef.child(date).child(meal).setValue(mealNew);
    }

    private boolean isFormCorrect(String username, String date, String time, String meal){
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getActivity(), "Select Elder", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(date)) {
            Toast.makeText(getActivity(), "Select Date", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(time)){
            Toast.makeText(getActivity(), "Select Time", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(meal)) {
            Toast.makeText(getActivity(), "Select Meal", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
                btnPickDate.setText(date);
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
                // Showing the picked time value in the Button
                String time = String.format("%02d:%02d", hour, minute);
                btnPickTime.setText(time);
            }
        }, hour, minute, true);

        // Add OK and Cancel buttons
        timePickerDialog.setButton(TimePickerDialog.BUTTON_POSITIVE, "OK", timePickerDialog);
        timePickerDialog.setButton(TimePickerDialog.BUTTON_NEGATIVE, "Cancel", timePickerDialog);

        timePickerDialog.show();
    }

    private ArrayList<String> get_usernames() {
        ArrayList<String> lists = new ArrayList<>();
        rootRef = FirebaseDatabase.getInstance().getReference().child("Caregiver").child(mAuth.getUid()).child("under_care");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Database
                lists.clear();
                for(DataSnapshot snapshot2 : snapshot.getChildren()) {
                    lists.add(snapshot2.getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return lists;
    }
    public static mealType convertStringToMeal(String mealString){
        try {
            return mealType.valueOf(mealString.toLowerCase());
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid meal string: " + mealString);
        }
    }
}


