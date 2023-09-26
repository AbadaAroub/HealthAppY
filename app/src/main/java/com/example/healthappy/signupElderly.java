package com.example.healthappy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class signupElderly extends AppCompatActivity {
    //Database
    private EditText elderlyNameEdt, elderlyMobileEdt, elderlyAddressEdt;
    Elderly elderly;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button signUpBtn;
    Button elderlyBtn;

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onCreate() {
        //Database
        elderlyNameEdt = findViewById(R.id.username1);
        elderlyMobileEdt = findViewById(R.id.mobileNumber);
        elderlyMobileEdt = findViewById(R.id.address);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Elderly");
        elderly = new Elderly();
        signUpBtn = findViewById(R.id.signup);
        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String name = elderlyNameEdt.getText().toString();
                String number = elderlyMobileEdt.getText().toString();
                String address = elderlyAddressEdt.getText().toString();
                String uid = user.getUid();

                if(TextUtils.isEmpty(name) && TextUtils.isEmpty(number)) {
                    Toast.makeText(signupElderly.this, "Add some data:", Toast.LENGTH_SHORT).show();
                } else {
                    addDatatoFirebase(name, number, address, uid);
                }
            }
        });
    }
    //Database
    private void addDatatoFirebase(String name, String number, String address, String uid) {
        elderly.setName(name);
        elderly.setMobile_nr(number);
        elderly.setAddress(address);

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
