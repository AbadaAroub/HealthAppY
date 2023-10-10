package com.example.healthappy;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class mealmanagment extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuth = FirebaseAuth.getInstance();
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


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentHome()).commit();
            setTitle("Home");


        } else if (item.getItemId() == R.id.nav_meal_managment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentMealmanagment()).commit();
            setTitle("Meal Managment");

        } else if (item.getItemId() == R.id.nav_accountsettings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentProfilesettings()).commit();
            setTitle("Profile Settings");
        } else if (item.getItemId() == R.id.nav_logout) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentLogout()).commit();
            mAuth.signOut();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        } else if (item.getItemId() == R.id.nav_callus) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentCallus()).commit();
            setTitle("Call Us");
        } else if (item.getItemId() == R.id.nav_emailus) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentEmailus()).commit();
            setTitle("Email Us");
        } else if (item.getItemId() == R.id.elderlysignup) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentSignupElderly()).commit();
            setTitle("Elderly SignUp");
        }
        else if (item.getItemId() == R.id.nav_abouttheapp){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentAbouttheapp()).commit();
            setTitle("About The App");
        }else {
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
}