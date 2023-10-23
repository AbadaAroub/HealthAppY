package com.example.healthappy;

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
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import javax.security.auth.callback.Callback;

public class FragmentLinkElder extends Fragment implements ExampleDialog.ExampleDialogListener {
    EditText etUsername;
    Button btnGetInfo;
    DatabaseReference elderRootRef;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_link_elder, container,false);
        elderRootRef = FirebaseDatabase.getInstance().getReference().child("Elder");

        btnGetInfo = (Button) view.findViewById(R.id.linkbtn);
        etUsername = (EditText) view.findViewById(R.id.username);


        btnGetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = String.valueOf(etUsername.getText());
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(getActivity(), "Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                }
                elderRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.hasChild(username)){
                            openElderInfoDialog(username);
                        } else {
                            Toast.makeText(getActivity(), "No Elder was found with that username", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        return view;
    }
    //Pre: Username exists in database under node "Elder"
    public void openElderInfoDialog(String username) {
        getElderFromDatabase(username, "name", new DataCallback() {
            @Override
            public void onDataLoaded(String data) {
                Log.i("Callback", data);
                ExampleDialog dialogElderInfo = new ExampleDialog(username, data);
                dialogElderInfo.show(getActivity().getSupportFragmentManager(), "Elder Dialog");
            }
        });
    }

    public void getElderFromDatabase(String username, String childRef, DataCallback callback){
        elderRootRef.child(username).child(childRef).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onDataLoaded(String.valueOf(task.getResult().getValue()));
                    Log.i("Callback", String.valueOf(task.getResult().getValue()));
                } else {
                    callback.onDataLoaded("errorname");
                }
            }
        });
    }
    @Override
    public void addElder(String username) {
        //This function is run from mealmanagement.java instead =)
        Log.i("AddElder", "From FragmentLinkElder.java");
    }
}
