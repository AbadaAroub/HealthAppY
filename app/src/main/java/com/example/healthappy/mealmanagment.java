package com.example.healthappy;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

public class mealmanagment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ExampleDialog.ExampleDialogListener{
    AppNotificationManager notif_mngr;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

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
        //View inflatedView = getLayoutInflater().inflate(R.layout.header, null);
        setTitle("Home");
        //TextView yourName = (TextView) inflatedView.findViewById(R.id.yourname);
        //yourName.setText("Abada Aroub");
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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentLogout()).commit();
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        } else if (item.getItemId() == R.id.nav_callus) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentCallus()).commit();
            setTitle(R.string.call_us);
        } else if (item.getItemId() == R.id.nav_emailus) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentEmailus()).commit();
            setTitle(R.string.mail_us);
        } else if (item.getItemId() == R.id.elderlysignup) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentLinkElder()).commit();
            setTitle(getString(R.string.elderly_signup));
        }
        else if (item.getItemId() == R.id.nav_abouttheapp){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAbouttheapp()).commit();
            setTitle(R.string.about);
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
        String UID = mAuth.getUid();
        Log.i("mAuth", UID);
        linkElderRef.child("Caregiver").child(UID).child("under_care").child(username).setValue(username);
        Toast.makeText(this, "Linked Elder to your account", Toast.LENGTH_SHORT).show();

    }
}