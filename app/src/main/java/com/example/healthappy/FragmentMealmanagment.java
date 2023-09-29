package com.example.healthappy;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class FragmentMealmanagment extends Fragment {
    private CalendarView calendarView;
    private TextView MyDate;
    private Button EditBtn, savebutton;
    EditText edittime, editdate, editfood;
    String[] item = {"Breakfast", "Lunch", "Small meal", "Dinner"};
    AutoCompleteTextView autoCompleteTextView;
    TextView date;
    ArrayAdapter<String> adapterItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mealmanagment, container, false);

        autoCompleteTextView = view.findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItems);
        editdate = (EditText) view.findViewById(R.id.myDate);
        edittime = (EditText) view.findViewById(R.id.mytime);
        editfood = (EditText) view.findViewById(R.id.mycomment);
        savebutton = (Button) view.findViewById(R.id.savebtn);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(),"Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //write what the save button does???
            }
        });


        //vi kanske behöver den
        /*calendarView = (CalendarView) view.findViewById(R.id.calandarView);
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
       getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentMealManagmentEdit()).commit();

    }*/

        return view;
    }
}

