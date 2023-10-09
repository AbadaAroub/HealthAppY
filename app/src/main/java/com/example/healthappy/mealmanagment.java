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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class mealmanagment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ExampleDialog.ExampleDialogListener{
    AppNotificationManager notif_mngr;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    FirebaseAuth mAuth;
    DatabaseReference rootRef;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
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

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        rootRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();

        } else if (item.getItemId() == R.id.nav_meal_managment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentMealmanagment()).commit();

        } else if (item.getItemId() == R.id.nav_accountsettings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentProfilesettings()).commit();

        } else if (item.getItemId() == R.id.nav_logout) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentLogout()).commit();
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        } else if (item.getItemId() == R.id.nav_callus) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentCallus()).commit();

        } else if (item.getItemId() == R.id.nav_emailus) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentEmailus()).commit();
        } else if (item.getItemId() == R.id.elderlysignup) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentLinkElder()).commit();
        }
        else {
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
        rootRef.child("Caregiver").child(UID).child("under_care").child(username).setValue(username);
    }
}