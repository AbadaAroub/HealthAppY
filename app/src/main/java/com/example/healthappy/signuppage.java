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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signuppage extends AppCompatActivity {
    EditText editTextEmail, editTextPassword, editTextRePassword, editTextName, editTextPhone;
    Button signUpBtn;
    FirebaseAuth mAuth;
    Caregiver caregiver;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);

        //User input fields
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.signuppass);
        editTextRePassword = findViewById(R.id.resignuppass);
        editTextName = findViewById(R.id.username1);
        editTextPhone = findViewById(R.id.mobileNumber);
        //Firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference().getDatabase();
        databaseReference = firebaseDatabase.getReference("Caregiver");
        //DB
        caregiver = new Caregiver();

        signUpBtn = findViewById(R.id.signup);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, rePassword, name, phone;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                rePassword = String.valueOf(editTextRePassword.getText());
                name = String.valueOf(editTextName.getText());
                phone = String.valueOf(editTextPhone.getText());

                if (!isFormCorrect(email, password, rePassword, name, phone)) {
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        String name, number, email;
                        if (task.isSuccessful()) {
                            Log.d("DeublerDebug", "Completed create user");

                            name = String.valueOf(editTextName.getText());
                            number = String.valueOf(editTextPhone.getText());
                            email = String.valueOf(editTextEmail.getText());

                            FirebaseUser user = mAuth.getCurrentUser();
                            addDatatoFirebase(name, number, email, user.getUid());

                            /*user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Email sent.");
                                    }
                                }
                            });
                            Toast.makeText(signuppage.this, R.string.toast_email_verification_sent, Toast.LENGTH_SHORT).show();*/

                            Intent intent = new Intent(getApplicationContext(), loginpage.class);
                            startActivity(intent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("DeublerDebug", "Failed create user");
                            Toast.makeText(signuppage.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            private boolean isFormCorrect(String email, String password, String rePassword, String name, String phone) {

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(signuppage.this, R.string.toast_enter_email, Toast.LENGTH_SHORT).show();
                    return false;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(signuppage.this, R.string.toast_enter_password, Toast.LENGTH_SHORT).show();
                    return false;
                } else if (TextUtils.isEmpty(rePassword)) {
                    Toast.makeText(signuppage.this, R.string.toast_reenter_password, Toast.LENGTH_SHORT).show();
                    return false;
                } else if (!String.valueOf(editTextPassword.getText()).equals(String.valueOf(editTextRePassword.getText()))) {
                    Toast.makeText(signuppage.this, R.string.toast_passwords_do_not_match, Toast.LENGTH_SHORT).show();
                    return false;
                } else if (TextUtils.isEmpty(name)) {
                    Toast.makeText(signuppage.this, R.string.toast_enter_name, Toast.LENGTH_SHORT).show();
                    return false;
                } else if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(signuppage.this, R.string.toast_enter_phone_number, Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    Log.d("DeublerDebug", "Made it through this jungle of field-checks");
                    return true;
                }
            }

            //Database
            private void addDatatoFirebase(String name, String number, String mail, String uid) {
                caregiver = new Caregiver();
                caregiver.setName(name);
                caregiver.setMobile_nr(number);
                caregiver.setEmail(mail);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        databaseReference.child(uid).setValue(caregiver);
                        Toast.makeText(signuppage.this, R.string.toast_data_added, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(signuppage.this, "Failed to add data " + error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });}}