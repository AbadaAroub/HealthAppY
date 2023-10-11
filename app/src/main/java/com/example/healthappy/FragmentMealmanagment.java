package com.example.healthappy;


import static com.example.healthappy.R.layout.fragment_mealmanagment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FragmentMealmanagment extends Fragment {
    private Button savebutton;
    private EditText edittime, dateEdt, editfood;
    String[] item = {"Breakfast", "Lunch", "Small meal", "Dinner"};
    Resources resources;
    AutoCompleteTextView autoCompleteTextView;
    TextView date;

    Toolbar toolbar;
    ArrayAdapter<String> adapterItems, adapterUids;
    //Datebase
    TextInputLayout mealEdt, listview;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;
    DatabaseReference careRef;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resources = getResources();
        item = resources.getStringArray(R.array.meals);

        View view =inflater.inflate(R.layout.fragment_mealmanagment, container,false);
        ArrayList<String> list = get_uids();

        autoCompleteTextView = view.findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item, item);
        adapterUids = new ArrayAdapter<String>(getActivity(), R.layout.list_item, list);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setAdapter(adapterUids);
        edittime = (EditText) view.findViewById(R.id.mytime);
        editfood = (EditText) view.findViewById(R.id.mycomment);
        savebutton = (Button) view.findViewById(R.id.savebtn);

        //Database
        mealEdt = view.findViewById(R.id.lol);
        dateEdt = view.findViewById(R.id.myDate);
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference("MealManagement");
        careRef = firebaseDatabase.getReference("Caregiver");

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
                String meal = mealEdt.getEditText().getText().toString();
                String date = dateEdt.getText().toString();
                String food = editfood.getText().toString();

                if (TextUtils.isEmpty(date) && TextUtils.isEmpty(meal) && TextUtils.isEmpty(food)) {
                    Toast.makeText(getActivity(), "Add some data", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(date, meal, food);
                }
            }
        });
        return view;
    }

    //Database
    private void addDatatoFirebase(String date, String meal, String food) {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Database
                dbRef.child(date).child("Meal").child(meal).setValue(food);
                Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<String> get_uids() {
        ArrayList<String> lists = new ArrayList<>();
        careRef = firebaseDatabase.getReference().child("under_care");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Database
                lists.clear();
                for(DataSnapshot snapshot2 : snapshot.getChildren()) {
                    Caregiver care = snapshot.getValue(Caregiver.class);
                    lists.add(snapshot2.getValue().toString());
                }
                Toast.makeText(getActivity(), "Data recieved", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fail to recieve data" + error, Toast.LENGTH_SHORT).show();
            }
        });
        return lists;
    }
}


