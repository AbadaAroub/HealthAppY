package com.example.healthappy;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.List;


public class FragmentMealmanagment extends Fragment {
    private Button savebutton;
    private EditText edittime, dateEdt, editfood;

    String[] item = {"Breakfast", "Lunch", "Small meal", "Dinner"};
    Resources resources;
    AutoCompleteTextView autoCompleteTextView, elder_dropdown;
    TextView date;
    ArrayAdapter<String> adapterItems, adapterCaretakers;


    //Datebase
    TextInputLayout mealEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference dbRef;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        dbRef = firebaseDatabase.getReference("MealManagement");
        resources = getResources();
        item = resources.getStringArray(R.array.meals);

        View view = inflater.inflate(R.layout.fragment_mealmanagment, container,false);

        autoCompleteTextView = view.findViewById(R.id.auto_complete_txt);
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item, item);
        autoCompleteTextView.setAdapter(adapterItems);

        List<String> undercareUIs = getListOfElderly();
        elder_dropdown = view.findViewById(R.id.elder_select_list);
        Log.i("EYO:", "inloggad som: " + mAuth.getCurrentUser().getEmail());


        List<String> underCareNames = new ArrayList<>();
        for(String uid : undercareUIs) {
            getNameByUid(uid, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        underCareNames.add(dataSnapshot.getValue(String.class));
                    } else {
                        Toast.makeText(getActivity(), "Username not found", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getActivity(), "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        adapterCaretakers = new ArrayAdapter<String>(getActivity(), R.layout.list_item, undercareUIs);
        elder_dropdown.setAdapter(adapterCaretakers);

        edittime = (EditText) view.findViewById(R.id.mytime);
        editfood = (EditText) view.findViewById(R.id.mycomment);
        savebutton = (Button) view.findViewById(R.id.savebtn);

        //Database
        mealEdt = view.findViewById(R.id.lol);
        dateEdt = view.findViewById(R.id.myDate);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity(),"Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        elder_dropdown.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String item = parent.getItemAtPosition(i).toString();
                Toast.makeText(getActivity(), "Item: " + item, Toast.LENGTH_SHORT).show();
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

    public void getNameByUid(String uid, ValueEventListener callback) {
        DatabaseReference elderNameRef = firebaseDatabase.getInstance().getReference("Elderly").child(uid).child("name");
        elderNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                callback.onDataChange(snapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onCancelled(databaseError);
            }
        });
    }
    private List<String> getListOfElderly(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference careRef = firebaseDatabase.getReference("Caregiver");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        Log.i("userid: ", uid);

        List<String> underCareList = new ArrayList<>();

        DatabaseReference undercareRef = careRef.child(uid).child("under_care");
        undercareRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Initialize a list to store the data
                    Log.i("DB: ", "Children exists");
                    // Iterate through the children of "under_care"
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        // Get the value (in this case, it's assumed to be a String)
                        String value = childSnapshot.getValue(String.class);
                        underCareList.add(value);
                        Log.i("dataSnapshot:" , value);
                    }
                } else {
                    Log.i("DB: ", "No children");
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // This method is called when there is an error in reading data
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });
        return underCareList;
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
}


