package com.example.healthappy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button loginBtn;
    private Button signupBtn;
    private TextView welcome, healthcare;
    Locale locale;
    RadioGroup rgLanguage;
    RadioButton rbEnglish, rbSwedish;
    FirebaseAuth mAuth;
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), mealmanagment.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        welcome = findViewById(R.id.welcome);
        healthcare = findViewById(R.id.healthcare);
        loginBtn = findViewById(R.id.loginbtn);
        signupBtn = findViewById(R.id.signupbtn);
        rgLanguage = findViewById(R.id.radiog);
        rbEnglish = findViewById(R.id.rb_english);
        rbSwedish = findViewById(R.id.rb_swedish);
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
        rgLanguage.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_english) {
                    changeLocale("en");
                    rbEnglish.setChecked(true);
                    welcome.setText(R.string.welcome);
                    healthcare.setText(R.string.health_care);
                    loginBtn.setText(R.string.login);
                    signupBtn.setText(R.string.signup);
                } else if (i == R.id.rb_swedish) {
                    changeLocale("sv");
                    rbSwedish.setChecked(true);
                    welcome.setText(R.string.welcome);
                    healthcare.setText(R.string.health_care);
                    loginBtn.setText(R.string.login);
                    signupBtn.setText(R.string.signup);
                }
            }
        });
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
        Resources resources = getBaseContext().getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        DisplayMetrics metrics = resources.getDisplayMetrics();
        resources.updateConfiguration(config, metrics);
    }
}