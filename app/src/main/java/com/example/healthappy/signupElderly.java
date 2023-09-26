package com.example.healthappy;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


public class signupElderly extends AppCompatActivity {
    EditText editTextEmail, editTextPassword, editTextRePassword;
    Button signUpBtn;
    FirebaseAuth mAuth;

    //Database
    private EditText elderlyNameEdt, elderlyMobileEdt, elderlyMailEdt, elderlyAddressEdt;
    Elderly elderly;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onCreate() {
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.signuppass);
        editTextRePassword = findViewById(R.id.resignuppass);

        //Database
        elderlyNameEdt = findViewById(R.id.username1);
        elderlyMobileEdt = findViewById(R.id.mobileNumber);
        elderlyMailEdt = findViewById(R.id.email);
        elderlyMobileEdt = findViewById(R.id.address);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Elderly");
        elderly = new Elderly();

        signUpBtn = findViewById(R.id.signup);
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String email, password, rePassword;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                rePassword = String.valueOf(editTextRePassword.getText());

                String name = elderlyNameEdt.getText().toString();
                String number = elderlyMobileEdt.getText().toString();
                String mail = elderlyMailEdt.getText().toString();
                String address = elderlyAddressEdt.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(signupElderly.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(signupElderly.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(name) && TextUtils.isEmpty(number)) {
                    Toast.makeText(signupElderly.this, "Add some data:", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            addDatatoFirebase(name, number, mail, address);
                                            Log.d(TAG, "Email sent.");
                                        }
                                    }
                                });
                                Toast.makeText(signupElderly.this, "Email verification sent.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), loginpage.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(signupElderly.this, task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    //Database
    private void addDatatoFirebase(String name, String number, String mail, String address) {
        elderly.setName(name);
        elderly.setMobile_nr(number);
        elderly.setAddress(address);
        String uid = mAuth.getCurrentUser().getUid();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(uid).setValue(elderly);
                Toast.makeText(signupElderly.this, "Data added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(signupElderly.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
