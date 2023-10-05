package com.example.healthappy;


import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

import org.w3c.dom.Text;

public class FragmentSignupElderly extends Fragment {
    EditText editTextEmail, editTextPassword, editTextRePassword;
    Button signUpBtn;
    FirebaseAuth mAuth;

    //Database
    private EditText elderlyNameEdt, elderlyMobileEdt, elderlyMailEdt, elderlyAddressEdt;
    Elderly elderly;
    Caregiver caregivers;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference careRef;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_signup_elderly, container, false);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = view.findViewById(R.id.emaileld);
        editTextPassword = view.findViewById(R.id.signuppasseld);

        //Database
        elderlyNameEdt = view.findViewById(R.id.usernameeld);
        elderlyMobileEdt = view.findViewById(R.id.mobileNumbereld);
        elderlyMailEdt = view.findViewById(R.id.emaileld);
        elderlyAddressEdt = view.findViewById(R.id.addresseld);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Elderly");

        elderly = new Elderly();

        signUpBtn = view.findViewById(R.id.signupeld);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PIN;
                String cuid = mAuth.getUid();
                String email, password, rePassword;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                //Setting (00 +) to achieve 6 digit password as Firebase requirement. Doing this on Elderly side aswell
                PIN = "00" + password;
                String name = elderlyNameEdt.getText().toString();
                String number = elderlyMobileEdt.getText().toString();
                String mail = elderlyMailEdt.getText().toString();
                String address = elderlyAddressEdt.getText().toString();
                Caregiver caregiver = new Caregiver();
                caregiver.setUser_UID(cuid);

                if(!isFormCorrect(name, number, email, address, PIN)) {
                    return;
                }
                    mAuth.createUserWithEmailAndPassword(email, PIN).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                addDatatoFirebase(name, number, mail, address, caregiver);
                                Log.d(TAG, "Email sent.");
                                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getActivity(), task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
        });
        return view;
    }
    private boolean isFormCorrect(String name, String phone, String email, String address, String PIN){
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(PIN)) {
            Toast.makeText(getActivity(), "Enter PIN-code", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(name)){
            Toast.makeText(getActivity(), "Enter name", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getActivity(), "Enter phone number", Toast.LENGTH_SHORT).show();
            return false;
        }else if (TextUtils.isEmpty(address)){
            Toast.makeText(getActivity(), "Enter address", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //Database
    private void addDatatoFirebase(String name, String number, String mail, String address, Caregiver caregiver) {
        String cuid = caregiver.getUser_UID();
        careRef = firebaseDatabase.getReference("Caregiver");
        elderly.setName(name);
        elderly.setMobile_nr(number);
        elderly.setAddress(address);

        String uid = mAuth.getCurrentUser().getUid();


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Databas
                databaseReference.child(uid).setValue(elderly);
                databaseReference.child(uid).child("caregivers").child(cuid).setValue(cuid);
                careRef.child(cuid).child("under_care").child(uid).setValue(uid);
                Toast.makeText(getActivity(), R.string.toast_data_added, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), getString(R.string.toast_data_fail) + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
