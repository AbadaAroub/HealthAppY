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
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
                String email, password, rePassword;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                String name = elderlyNameEdt.getText().toString();
                String number = elderlyMobileEdt.getText().toString();
                String mail = elderlyMailEdt.getText().toString();
                String address = elderlyAddressEdt.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getActivity(), "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name) && TextUtils.isEmpty(number)) {
                    Toast.makeText(getActivity(), "Add some data:", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(getActivity(), "Email verification sent.", Toast.LENGTH_SHORT).show();
                                //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(getActivity(), task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        return view;
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
                Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
