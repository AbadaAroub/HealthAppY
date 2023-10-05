package com.example.healthappy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class loginpage extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button loginbutton;
    FirebaseAuth mAuth;
    TextView forgotPassText;
    // Kanske behöver denna senare
    /*@Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), signuppage.class);
            startActivity(intent);
            finish();
        }
    }*/

    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);


        /*button = (Button) findViewById(R.id.loginbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openmealmanagmentpage();
            }
        });*/


        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email1); //Change to email?
        editTextPassword = findViewById(R.id.password);
        loginbutton = findViewById(R.id.loginbutton);
        forgotPassText = findViewById(R.id.forgotpass);

        loginbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(loginpage.this, R.string.toast_enter_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    Toast.makeText(loginpage.this, R.string.toast_enter_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(loginpage.this, R.string.toast_login_success, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), mealmanagment.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(loginpage.this, R.string.toast_auth_fail, Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });

        forgotPassText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), forgotPassPage.class);
                startActivity(intent);
                finish();
            }
        });
    }
    /*public void openmealmanagmentpage() {
        Intent intent = new Intent(this, mealmanagment.class);
        startActivity(intent);
    }
    */
}