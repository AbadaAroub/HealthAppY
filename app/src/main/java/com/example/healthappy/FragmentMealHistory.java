package com.example.healthappy;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class FragmentMealHistory extends Fragment {


    private Spinner elderly_select;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private ArrayList<String> elderUIDs = new ArrayList<>();

    LinearLayout history_content;
    public FragmentMealHistory(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
        db = FirebaseDatabase.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_history, container, false);
        history_content = view.findViewById(R.id.history_content);
        elderly_select = (Spinner) view.findViewById(R.id.elderly_select);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), R.layout.fragment_meal_history, R.id.t_view);
        elderly_select.setAdapter(adapter);
        adapter.add(getString(R.string.select_elderly));
        elderUIDs.add("Blyat!");

        DatabaseReference ref = db.getReference(String.format("Caregiver/%s/under_care", mAuth.getUid()));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot elderUID_snapshot: snapshot.getChildren()) {
                    DatabaseReference elder_info = db.getReference(String.format("Elder/%s/", elderUID_snapshot.getValue(String.class)));
                    elder_info.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            adapter.add(snapshot.child("name").getValue(String.class));
                            elderUIDs.add(elderUID_snapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        history_content.setDividerPadding(100);
        elderly_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (l!=0){
                    history_content.removeAllViews();
                    TextView p = new TextView(FragmentMealHistory.this.getContext());
                    p.setText(String.format("Selected user: %d", l));
                    history_content.addView(p);
                    get_meal_history((int)l, history_content);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }
    private void get_meal_history(int index, LinearLayout layout) {
        ArrayList<TextView> ret = new ArrayList<>();
        Log.d("FragmentMealHistory","In .get_meal_history");
        DatabaseReference ref = db.getReference(String.format("Elder/%s/meal_history/", elderUIDs.get(index)));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot meal : snapshot.getChildren()) {
                    TextView meal_info = new TextView(FragmentMealHistory.this.getContext());
                    String data = String.format("Date: %s\nMeal: %d\nDescription:%s",
                            meal.child("date").getValue(String.class),
                            meal.child("type").getValue(int.class),
                            meal.child("description").getValue(String.class));
                    Log.d("FragmentMealHistory", String.format("In .get_meal_history .onDataChange data: %s", meal.getValue()));
                    meal_info.setText(data);

                    layout.addView(meal_info);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FragmentMealManagement", String.format("onCancelled: Oh not:\n%s", error.getDetails()));
            }
        });
    }
}
