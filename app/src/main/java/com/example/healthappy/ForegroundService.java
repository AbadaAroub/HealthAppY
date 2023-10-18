package com.example.healthappy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class ForegroundService extends Service {
    private static final String TAG = "FOREGROUND_SERVICE";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD");
    public static final int work_delay = 30000;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private Thread the_service_thread;
    private boolean thread_may_run = true;
    private long time_stamp = System.currentTimeMillis();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");

        Intent notificationIntent = new Intent(this, mealmanagment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();

        Notification notification = new NotificationCompat.Builder(this, App.channel_medium_id)
                .setContentTitle("FOREGROUND_SERVICE")
                .setContentText("Service is running...")
                .setSmallIcon(R.drawable.baseline_fastfood_24)
                .setContentIntent(pendingIntent).build();

        start_service();

        startForeground(1, notification);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        thread_may_run = false;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void start_service() {
        the_service_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (thread_may_run){
                    do_work();
                    try {
                        Thread.sleep(work_delay);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "ForegroundService.start_service: ", e);
                    }
                }
            }
        });
        the_service_thread.start();
    }

    private void do_work() {
        check_45_minute();
    }

    private void check_45_minute() {
        Log.d(TAG, String.format("check_45_minute: Checking missed meals for elders under care of: %s", mAuth.getUid()));

        DatabaseReference ref = db.getReference(String.format("Caregiver/%s/under_care", mAuth.getUid()));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot under_care) {
                for (DataSnapshot elder : under_care.getChildren()) {
                    Log.d(TAG, String.format("check_45_minute: Checking for meals missed... %s", elder.getKey()));
                    String e_username = elder.getKey();
                    if (has_uneaten_meal_45(e_username)) {
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
    }

    private boolean has_uneaten_meal_45(String username) {

        DatabaseReference ref = db.getReference(String.format("Elder/%s/Meals/%s", username, dateFormat.format(Date.from(Instant.now()))));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "has_uneaten_meal_45: ", error.toException());
            }
        });
        return false;
    }
}
