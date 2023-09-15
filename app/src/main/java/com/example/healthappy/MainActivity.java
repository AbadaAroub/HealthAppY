package com.example.healthappy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class MainActivity extends AppCompatActivity {
    private Button button;
    private Button button1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    public void openLoginPage() {
        Intent intent = new Intent(this, loginpage.class);
        startActivity(intent);
    }
    public void openSignupPage(){
        Intent intent = new Intent(this, signuppage.class);
        startActivity(intent);
    }
}