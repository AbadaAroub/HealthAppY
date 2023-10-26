package com.example.healthappy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.icu.util.Calendar;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

enum warnings {
    MISSED,
    CRITICAL,
    MEDIUM,
    EARLY
}

public class ForegroundService extends Service {
    private static final String TAG = "FOREGROUND_SERVICE";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final int work_delay = 30000;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private Thread the_service_thread;
    private boolean thread_may_run = true;
    private AppNotificationManager notif_manager;
    private long time_stamp = System.currentTimeMillis();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");

        Intent notificationIntent = new Intent(this, mealmanagment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        notif_manager = new AppNotificationManager(this);

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
                    if (mAuth.getCurrentUser() != null) {
                        do_work();
                        try {
                            Thread.sleep(work_delay);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "ForegroundService.start_service: ", e);
                        }
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
                int eld = 0;
                for (DataSnapshot elder : under_care.getChildren()) {
                    manage_uneaten(elder, eld++);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: ", error.toException());
            }
        });
    }

    public void print_meal(DataSnapshot meal) {
        String date = meal.child("date").getValue(String.class);
        String type = meal.child("type").getValue(String.class);
        String time = meal.child("time_of_day").getValue(String.class);
        String comment = meal.child("comment").getValue(String.class);

        Log.d(TAG, String.format("Meal______%s_______\nTime:\t%20s\nType:\t%20s\nComment:\t%s\nMintues since meal: %d", date, time, type,comment, minutes_since_meal(meal)));
    }


    private long minutes_since_meal(DataSnapshot meal) {
        try {
            long diff = System.currentTimeMillis() - dateTimeFormat
                    .parse(meal.child("date").getValue(String.class)+" "+meal.child("time_of_day").getValue(String.class))
                    .getTime();
            return diff/60000L;
        } catch (ParseException e) {
            Log.e(TAG, "minutes_since_meal: ", e);
            return -1L;
        }
    }




    private void manage_uneaten(DataSnapshot elder, int eld) {
        String date = dateFormat.format(Calendar.getInstance().getTimeInMillis());
        Log.d(TAG, String.format("has_uneaten_meal_45: Elder/%s/Meals/%s", elder.getKey(), date));
        DatabaseReference ref = db.getReference(String.format("Elder/%s/Meals/%s", elder.getKey(), date));
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot meals_today) {
                for (DataSnapshot meal : meals_today.getChildren()) {
                    warnings w = meal.child("c_warn_stat").getValue(warnings.class);
                    long time_minutes = minutes_since_meal(meal);
                    if (time_minutes >= 60) {
                        if (w != warnings.MISSED) {
                            Log.d(TAG, "manage_uneaten: " + getString(R.string.missed_meal) + elder.getKey() + getString(R.string.missed_meal_info));
                            notif_manager.high_notif(eld, getString(R.string.missed_meal), elder.getKey() + getString(R.string.missed_meal_info));


                            DatabaseReference history_ref = db.getReference(String.format("Elder/%s/meal_history", elder.getKey())).push();
                            history_ref.setValue(meal.getValue(Meal.class));
                            history_ref.child("time_of_report").setValue(
                                    new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTimeInMillis())
                            );
                            history_ref.child("ate").setValue(false);
                            meal.getRef().setValue(null);

                        }
                    } else if (time_minutes >= 45) {
                        if (w != warnings.CRITICAL){
                            notif_manager.high_notif(eld, getString(R.string.crit_meal_alert), elder.child("name").getValue(String.class)+getString(R.string.crit_meal_alert_info));
                            meal.child("c_warn_stat").getRef().setValue(warnings.CRITICAL);
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "has_uneaten_meal_45: ", error.toException());
            }
        });
    }
}
