package com.example.healthappy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import javax.annotation.Nullable;

public class FragmentMealHistory extends Fragment {

    private Spinner elderly_select;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    public FragmentMealHistory(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
        db = FirebaseDatabase.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_history, container, false);

        elderly_select = (Spinner) view.findViewById(R.id.elderly_select);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), R.layout.fragment_meal_history, R.id.t_view);
        adapter.add(getString(R.string.select_elderly));

        DatabaseReference ref = db.getReference(String.format("Caregiver/%s/under_care", mAuth.getUid()));

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                    DatabaseReference elder_info = db.getReference(String.format("Elder/%s/name", itemSnapshot.getValue(String.class)));
                    elder_info.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            adapter.add(snapshot.getValue(String.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                elderly_select.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}
