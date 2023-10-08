package com.example.healthappy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class FragmentMealHistory extends Fragment {

    private Spinner elderly_select;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_history, container, false);

        elderly_select = (Spinner) view.findViewById(R.id.elderly_select);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), R.layout.fragment_meal_history, R.id.t_view);
        adapter.add(getString(R.string.select_elderly));
        adapter.add("Runar Andersson");

        elderly_select.setAdapter(adapter);
        return view;
    }
}
