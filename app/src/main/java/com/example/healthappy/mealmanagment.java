package com.example.healthappy;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

import java.util.List;

public class mealmanagment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ExampleDialog.ExampleDialogListener, MealEditDialog.MealEditDialogListener {
    AppNotificationManager notif_mngr;
    private DrawerLayout drawerLayout;

    FirebaseAuth mAuth;
    DatabaseReference rootRef;
    DatabaseReference linkElderRef;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        linkElderRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(getApplicationContext(), loginpage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notif_mngr = new AppNotificationManager(this);
        setContentView(R.layout.activity_mealmanagment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.home);


        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
        linkElderRef = FirebaseDatabase.getInstance().getReference();
        rootRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
            setTitle(R.string.home);
        } else if (item.getItemId() == R.id.nav_meal_managment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentMealmanagment()).commit();
            setTitle(R.string.meal_managment);

        } else if (item.getItemId() == R.id.nav_accountsettings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentProfilesettings()).commit();
            setTitle(R.string.profile_settings);
        } else if (item.getItemId() == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mealmanagment.this);
            builder.setMessage(R.string.do_you_want_to_log_out);
            builder.setTitle(R.string.log_out);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.ok, (DialogInterface.OnClickListener) (dialog, which) -> {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentLogout()).commit();
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            });
            builder.setNegativeButton(R.string.cancel, (DialogInterface.OnClickListener) (dialog, which) -> {
                dialog.cancel();
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else if (item.getItemId() == R.id.nav_callus) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentCallus()).commit();
            setTitle(R.string.call_us);
        } else if (item.getItemId() == R.id.nav_emailus) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentEmailus()).commit();
            setTitle(R.string.mail_us);
        } else if (item.getItemId() == R.id.elderlysignup) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentLinkElder()).commit();
            setTitle(getString(R.string.elderly_signup));
        } else if (item.getItemId()==R.id.meal_history) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentMealHistory(mAuth)).commit();
        }
        else if (item.getItemId() == R.id.nav_abouttheapp){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAbouttheapp()).commit();
            setTitle(R.string.about_the_app);
        } else {
            return false;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public void addElder(String username) {
        Log.i("AddElder", "From mealmanangement.java");
        linkElderToCaregiver(username);
    }

    private void linkElderToCaregiver(String username) {
        linkElderRef = FirebaseDatabase.getInstance().getReference();
        String UID = mAuth.getUid();
        Log.i("mAuth", UID);
        linkElderRef.child("Caregiver").child(UID).child("under_care").child(username).setValue(username);
        Toast.makeText(this, "Linked Elder to your account", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void editMeal(String username, String date, String time, String meal, String comment) {
        addMealToElder(username, date, time, meal, comment);
    }

    @Override
    public void removeMeal(String username, String date, String time) {
        removeMealFromDB(username, date, time);
    }
    private void removeMealFromDB(String username, String date, String time){
        DatabaseReference elderRef = FirebaseDatabase.getInstance().getReference().child("Elder");
        Log.i("mm.removemeal", "Trying to remove " + username + " " + date + " " + time);
        elderRef.child(username).child("Meals").child(date).child(time).removeValue();
    }
    private void addMealToElder(String username, String date, String time, String meal, String comment){
        mealType type;
        if(meal.equalsIgnoreCase("Frukost") || meal.equalsIgnoreCase("Middag") || meal.equalsIgnoreCase("Mellanmål")){
            type = convertSwedishStringToMeal(meal);
        } else {
            type = convertStringToMeal(meal);
        }


        //add support for swedish strings
        Meal mealNew = new Meal(type, time, date, comment);
        DatabaseReference elderMealRef = FirebaseDatabase.getInstance().getReference().child("Elder").child(username).child("Meals");

        elderMealRef.child(date).child(time).setValue(mealNew);

    }
    public static mealType convertStringToMeal(String mealString){
        try {
            return mealType.valueOf(mealString.toLowerCase());
        } catch (IllegalArgumentException e){
            throw new IllegalArgumentException("Invalid meal string: " + mealString);
        }
    }

    private mealType convertSwedishStringToMeal(String meal) {
        if(meal.equalsIgnoreCase("Frukost")){ return mealType.breakfast; }
        else if(meal.equalsIgnoreCase("Middag")){ return mealType.dinner; }
        else if(meal.equalsIgnoreCase("Mellanmål")){ return mealType.snack; }
        return mealType.lunch;
    }
}