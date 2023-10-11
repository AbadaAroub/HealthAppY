package com.example.healthappy;

import android.view.View;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentNotificationTest extends Fragment{
    private mealmanagment ac;
    public FragmentNotificationTest(mealmanagment ac) {
        this.ac = ac;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_debug_notification, container, false);

        Button basic_btn = (Button) view.findViewById(R.id.notification1);

        basic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ac.basic_notif("Bitconnect", "Bitcooooooonnnnnnnneeeeeeecccccctttt!!!!!.... WOOOOAHH!!!!");
            }

        });

        return view;
    }

}
