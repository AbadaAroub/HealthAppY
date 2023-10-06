package com.example.healthappy;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotPassPage extends AppCompatActivity {

    EditText editTextEmail;
    Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass_page);

        editTextEmail = findViewById(R.id.email);
        sendBtn = findViewById(R.id.btn_sendResetMail);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        sendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email;
                email = String.valueOf(editTextEmail.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(forgotPassPage.this, R.string.toast_enter_email, Toast.LENGTH_SHORT).show();
                    return;
                }


                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                            Toast.makeText(forgotPassPage.this, R.string.toast_email_sent, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), loginpage.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), loginpage.class);
        startActivity(intent);
        finish();
    }
}