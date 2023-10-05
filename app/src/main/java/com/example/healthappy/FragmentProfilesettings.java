package com.example.healthappy;


import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public class FragmentProfilesettings extends Fragment {
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    String[] langs;
    Resources resources;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resources = getResources();
        langs = resources.getStringArray(R.array.langs);
        View view =inflater.inflate(R.layout.fragment_profilesettings, container,false);

        autoCompleteTextView = view.findViewById(R.id.auto_complete_lang);
        adapterItems = new ArrayAdapter<String>(getActivity(), R.layout.list_item, langs);
        autoCompleteTextView.setAdapter(adapterItems);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String lang = parent.getItemAtPosition(position).toString();
                if(lang.equals("English"))
                    LocaleHelper.setLocale(FragmentProfilesettings.this.getContext(), "en");
                else if(lang.equals("Svenska"))
                    LocaleHelper.setLocale(FragmentProfilesettings.this.getContext(), "sv");
                FragmentProfilesettings.this.getActivity().recreate();
                String toastString = getString(R.string.set_language_to) + " " + lang;
                Toast.makeText(getActivity(),toastString, Toast.LENGTH_SHORT).show();
                autoCompleteTextView.setText("");
            }
        });

        return view;
    }
}
