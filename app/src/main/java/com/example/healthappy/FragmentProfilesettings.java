package com.example.healthappy;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FragmentProfilesettings extends Fragment {

    Button saveChanges;
    EditText etUsername, etNumber;
    TextView tvEmail;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String[] langs;
    Resources resources;
    FirebaseUser fUser;
    DatabaseReference rootRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profilesettings, container, false);
        langs = view.getResources().getStringArray(R.array.langs);
        resources = getResources();
        fUser = FirebaseAuth.getInstance().getCurrentUser();


        saveChanges = view.findViewById(R.id.savechangesbtn);
        rootRef = FirebaseDatabase.getInstance().getReference();
        etUsername = view.findViewById(R.id.yourusername);
        tvEmail = view.findViewById(R.id.youremail);
        etNumber = view.findViewById(R.id.yournumber);
        //etPassword = view.findViewById(R.id.yourpass);

        rootRef.child("Caregiver").child(fUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                etUsername.setText(String.valueOf(task.getResult().child("name").getValue()));
                //etEmail.setText(String.valueOf(task.getResult().child("email").getValue()));
                etNumber.setText(String.valueOf(task.getResult().child("mobile_nr").getValue()));
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword, newName, newEmail, newNumber;
                //newPassword = String.valueOf(etPassword.getText());
                newName = String.valueOf(etUsername.getText());
                //newEmail = String.valueOf(etEmail.getText());
                newNumber = String.valueOf(etNumber.getText());
                updateCaregiver(newName, newNumber);
                //updateUser(newEmail, newPassword); DOES NOT WORK, REMOVED UNTIL NEEDED

            }
        });

        autoCompleteTextView = view.findViewById(R.id.auto_complete_lang);
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item, langs);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lang = parent.getItemAtPosition(position).toString();
                if (lang.equals("English")) {
                    LocaleHelper.setLocale(FragmentProfilesettings.this.getContext(), "en");
                    LocaleHelper.saveLocale(FragmentProfilesettings.this.getContext(), "en");

                } else if (lang.equals("Svenska")) {
                    LocaleHelper.setLocale(FragmentProfilesettings.this.getContext(), "sv");
                    LocaleHelper.saveLocale(FragmentProfilesettings.this.getContext(), "sv");
                }
                FragmentProfilesettings.this.getActivity().recreate();
                String toastString = getString(R.string.set_language_to) + " " + lang;
                Toast.makeText(getActivity(), toastString, Toast.LENGTH_SHORT).show();
                autoCompleteTextView.setText("");
            }
        });

        return view;
    }

    /*private void updateUser(String newEmail, String newPassword) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.i("updateUser", "trying to update "+ fUser.getEmail() + " with " + newEmail + " and set password to " + newPassword);
        fUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User email address updated.");
                    Toast.makeText(getActivity(), "updated email", Toast.LENGTH_SHORT).show();

                }
            }
        });
        fUser.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "User password updated.");
                    Toast.makeText(getActivity(), "updated password", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }*/

    private void updateCaregiver(String newName, String newNumber) {
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        //fUser.getEmail to replace editText string, since it's not supported and new Caregiver needs email field to match with FBauth.
        //Normally we should delete this whole fragment, it's not very usable.
        Caregiver updatedCaregiver = new Caregiver(newName, newNumber, fUser.getEmail());

        DatabaseReference userRef = rootRef.child("Caregiver").child(fUser.getUid());
        userRef.setValue(updatedCaregiver);
    }

}
