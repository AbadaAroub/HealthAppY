package com.example.healthappy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    private EditText caregiverNameEdt, caregiverMobileEdt;
    Caregiver caregiver;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private Button sendDatabtn;

    @Override
    public void onStart() {
        super.onStart();
        //caregiverNameEdt = findViewById(R.id.idEdtCaregiverName);
        //caregiverMobileEdt = findViewById(R.id.idEdtNumber);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Caregiver");

        //sendDatabtn = findViewById(R.id.idBtnSendData);
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), signuppage.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.signuppass);
        editTextRePassword = findViewById(R.id.resignuppass);
        signUpBtn = findViewById(R.id.signup);

        signUpBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String email, password, rePassword;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                rePassword = String.valueOf(editTextRePassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(signuppage.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(signuppage.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(rePassword)) {
                    Toast.makeText(signuppage.this, "Reenter Password", Toast.LENGTH_SHORT).show();
                } else if (!String.valueOf(editTextPassword.getText()).equals(String.valueOf(editTextRePassword.getText()))) {
                    Toast.makeText(signuppage.this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
                }

                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(signuppage.this, "Account Created.", Toast.LENGTH_SHORT).show();
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
        sendDatabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = caregiverNameEdt.getText().toString();
                String number = caregiverMobileEdt.getText().toString();

                if(TextUtils.isEmpty(name) && TextUtils.isEmpty(number)) {
                    Toast.makeText(signuppage.this, "Add some data:", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(name, number);
                }
            }
        });
    }
    private void addDatatoFirebase(String name, String number) {
        caregiver.setName(name);
        caregiver.setMobile_nr(number);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(caregiver);
                Toast.makeText(signuppage.this, "data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(signuppage.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}