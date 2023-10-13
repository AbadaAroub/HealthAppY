package com.example.healthappy;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentEditMeal extends Fragment {
    private Button btnSave, btnPickDate;
    AutoCompleteTextView actvElderDropdown;
    ArrayAdapter<String> adapterUsernames;
    ArrayList<Meal> listMeals;
    DatabaseReference rootRef;
    FirebaseAuth mAuth;
    RecyclerView rwMeals;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_meal, container,false);
        rootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        ArrayList<String> listUsernames = get_usernames();
        actvElderDropdown = view.findViewById(R.id.elder_select_list);
        adapterUsernames = new ArrayAdapter<String>(getActivity(), R.layout.list_item, listUsernames);
        actvElderDropdown.setAdapter(adapterUsernames);

        btnPickDate = (Button) view.findViewById(R.id.btnDatePicker);

        rwMeals = view.findViewById(R.id.mealList);
        rwMeals.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnPickDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openDatePicker();
            }
        });

        actvElderDropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = actvElderDropdown.getAdapter().getItem(i).toString();
                Log.d("selected:", selected);
                getDatesOfMeals(selected, new DateKeysCallback() {
                    @Override
                    public void onDateKeysCallback(ArrayList<String> list) {
                        listMeals = getMealsOfElder(selected, list);
                        rwMeals.setAdapter(new MealAdapter(getActivity(), listMeals));
                    }
                });
            }
        });
        return view;
    }

    private void getDatesOfMeals(String selected, DateKeysCallback callback) {
        DatabaseReference userMealsRef = FirebaseDatabase.getInstance().getReference()
                .child("Elder").child(selected).child("Meals");

        userMealsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> dateKeys = new ArrayList<String>();
                if (snapshot.exists()) {
                    for (DataSnapshot dateSnapshot : snapshot.getChildren()) {
                        String dateKey = dateSnapshot.getKey();
                        if (dateKey != null) {
                            dateKeys.add(dateKey);
                        }
                    }
                }
                callback.onDateKeysCallback(dateKeys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors if needed
            }
        });
    }
    private ArrayList<Meal> getMealsOfElder(String selected, ArrayList<String> dates) {
        ArrayList<Meal> list = new ArrayList<Meal>();
        DatabaseReference userMealsRef = FirebaseDatabase.getInstance().getReference()
                .child("Elder").child(selected).child("Meals");

        for(String dateKey : dates){
            Log.d("dateKeys", dateKey);
            DatabaseReference userMealsDateRef = userMealsRef.child(dateKey);

            userMealsDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot mealSnapshot : snapshot.getChildren()){
                            Log.d("mealSnapshot", mealSnapshot.toString());
                            Meal meal = mealSnapshot.getValue(Meal.class);
                            if(meal != null){
                                list.add(meal);
                            }
                        }
                        rwMeals.getAdapter().notifyDataSetChanged();
                        Log.d("rwMeals", "updated");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        return list;
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
}


