package com.example.healthappy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.w3c.dom.Text;

public class ExampleDialog extends AppCompatDialogFragment {
    private TextView tvName, tvUsername;
    private String username, name;
    private ExampleDialogListener listener;

    public ExampleDialog(String username, String name) {
        this.username = username;
        this.name = name;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_elder_info, null);
        tvName = (TextView) view.findViewById(R.id.tvName);
        tvUsername = (TextView) view.findViewById(R.id.dialog_username);
        tvName.setText(name);
        tvUsername.setText(username);

        builder.setView(view)
                .setTitle("Add Elder as Caretaker")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.addElder(username);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener{
        void addElder(String username);
    }
}
