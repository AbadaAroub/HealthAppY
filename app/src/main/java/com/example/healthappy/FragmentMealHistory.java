package com.example.healthappy;





import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
                    elder_info.addListenerForSingleValueEvent(new ValueEventListener() {
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
        elderly_select.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                history_content.removeAllViews();
                if (l!=0){
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
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot d_meal : snapshot.getChildren()) {
                    View p = LayoutInflater.from(FragmentMealHistory.this.getContext()).inflate(R.layout.history_item, null);

                    // Setting date and time
                    TextView datetime = (TextView) p.findViewById(R.id.history_item_root_when_datetime);
                    datetime.setText(d_meal.child("date").getValue(String.class)+"."+d_meal.child("time_of_report").getValue(String.class));

                    // Setting check mark or cross mark
                    ImageView check = (ImageView) p.findViewById(R.id.history_item_root_when_ate);
                    if (d_meal.child("ate").getValue(boolean.class))
                        check.setImageResource(R.drawable.checkmark);
                    else
                        check.setImageResource(R.drawable.crossmark);

                    // Setting type of meal
                    TextView type = (TextView) p.findViewById(R.id.history_item_root_info_type);
                    switch (d_meal.child("type").getValue(int.class)) {
                        case 0: //BREAKFAST
                            type.setText("Breakfast");
                            break;
                        case 1: //LUNCH
                            type.setText("Lunch");
                            break;
                        case 2: //DINNER
                            type.setText("Dinner");
                            break;
                        case 3: //SNACK
                            type.setText("Snack");
                            break;
                    }

                    ((TextView)p.findViewById(R.id.history_item_root_info_desc)).setText(d_meal.child("description").getValue(String.class));

                    layout.addView(p);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FragmentMealManagement", String.format("onCancelled: Oh not:\n%s", error.getDetails()));
            }
        });
    }

}
