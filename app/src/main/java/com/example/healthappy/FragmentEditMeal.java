package com.example.healthappy;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class FragmentEditMeal extends Fragment {
    private Button btnSave, btnPickDate;
    AutoCompleteTextView actvElderDropdown;
    ArrayAdapter<String> adapterUsernames;
    DatabaseReference rootRef;
    FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_meal, container,false);
        rootRef = FirebaseDatabase.getInstance().getReference();

        ArrayList<String> listUsernames = get_usernames();
        actvElderDropdown = view.findViewById(R.id.elder_select_list);
        adapterUsernames = new ArrayAdapter<String>(getActivity(), R.layout.list_item, listUsernames);
        actvElderDropdown.setAdapter(adapterUsernames);

        btnPickDate = (Button) view.findViewById(R.id.btnDatePicker);

        btnPickDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openDatePicker();
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


