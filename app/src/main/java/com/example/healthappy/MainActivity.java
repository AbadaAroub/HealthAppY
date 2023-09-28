package com.example.healthappy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button loginBtn;
    private Button signupBtn;
    private Button swedishBtn;
    private Button englishBtn;
    private TextView test;
    private Locale locale;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private LocalDate selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
        selectedDate = LocalDate.now();
        setMonthView();

        loginBtn = (Button) findViewById(R.id.loginbtn);
        signupBtn = (Button) findViewById(R.id.signupbtn);
        englishBtn = (Button) findViewById(R.id.english);
        swedishBtn = (Button) findViewById(R.id.swedish);
        loginBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignupPage();
            }
        });
        englishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLocale("en");
                MainActivity.this.recreate();
            }
        });
        swedishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeLocale("sv");
                MainActivity.this.recreate();
            }
        });
    }

    private void initWidgets() {
  //      calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
    //    monthYearText = findViewById(R.id.monthYearTV);
    }

    private void setMonthView() {
        //monthYearText.setText();
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        return "";
    }
    public void openLoginPage() {
        Intent intent = new Intent(this, loginpage.class);
        startActivity(intent);
        finish();
    }
    public void openSignupPage(){
        Intent intent = new Intent(this, signuppage.class);
        startActivity(intent);
        finish();
    }
    private void changeLocale(String lang) {
        locale = new Locale(lang);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        getApplicationContext().createConfigurationContext(config);

    }
    public void previousMonthAction(View view) {

    }

    public void nextMonthAction(View view) {

    }
}