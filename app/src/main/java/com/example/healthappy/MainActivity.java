package com.example.healthappy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private Button button1;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        //selectedDate = LocalDate.now();
        setMonthView();
        button = (Button) findViewById(R.id.loginbtn);
        button1 = (Button) findViewById(R.id.signupbtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignupPage();
            }
        });
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        //monthYearText.setText();
    }

    /*private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
    }*/
    public void openLoginPage() {
        Intent intent = new Intent(this, loginpage.class);
        startActivity(intent);
    }
    public void openSignupPage(){
        Intent intent = new Intent(this, signuppage.class);
        startActivity(intent);
    }

    public void previousMonthAction(View view) {

    }

    public void nextMonthAction(View view) {

    }
}