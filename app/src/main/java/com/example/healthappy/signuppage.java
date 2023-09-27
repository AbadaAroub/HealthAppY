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
    EditText editTextEmail, editTextPassword, editTextRePassword;
    Button signUpBtn;
    FirebaseAuth mAuth;

    //Database
    private EditText caregiverNameEdt, caregiverMobileEdt, caregiverEmailEDt;
    Caregiver caregiver;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.signuppass);
        editTextRePassword = findViewById(R.id.resignuppass);

        //Database
        caregiverNameEdt = findViewById(R.id.username1);
        caregiverMobileEdt = findViewById(R.id.mobileNumber);
        caregiverEmailEDt = findViewById(R.id.email);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Caregiver");
        caregiver = new Caregiver();

        signUpBtn = findViewById(R.id.signup);

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email, password, rePassword;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                rePassword = String.valueOf(editTextRePassword.getText());

                //Database
                String name = caregiverNameEdt.getText().toString();
                String number = caregiverMobileEdt.getText().toString();
                String mail = caregiverEmailEDt.getText().toString();

                String uid = user.getUid();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(signuppage.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(signuppage.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(rePassword)) {
                    Toast.makeText(signuppage.this, "Reenter Password", Toast.LENGTH_SHORT).show();
                } else if (!String.valueOf(editTextPassword.getText()).equals(String.valueOf(editTextRePassword.getText()))){
                    Toast.makeText(signuppage.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }

                //Database
                if(TextUtils.isEmpty(name) && TextUtils.isEmpty(number)) {
                    Toast.makeText(signuppage.this, "Add some data:", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(name, number, mail, uid);
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();

                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");
                                            }
                                        }
                                    });
                            Toast.makeText(signuppage.this, "Email verification sent.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), loginpage.class);
                                    startActivity(intent);
                                    finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(signuppage.this, task.getException().getLocalizedMessage(),
                            Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //Database
    private void addDatatoFirebase(String name, String number, String mail, String uid) {
        caregiver.setName(name);
        caregiver.setMobile_nr(number);
        caregiver.setEmail(mail);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(uid).setValue(caregiver);
                Toast.makeText(signuppage.this, "Data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(signuppage.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}