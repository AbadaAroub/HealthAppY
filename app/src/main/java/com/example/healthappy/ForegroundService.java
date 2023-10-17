package com.example.healthappy;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.sql.Timestamp;

public class ForegroundService extends Service {

    private Thread the_service_thread;
    private long time_stamp = System.currentTimeMillis();
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("FOREGROUND_SERVICE", "onStartCommand: ");

        Intent notificationIntent = new Intent(this, mealmanagment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);


        Notification notification = new NotificationCompat.Builder(this, App.channel_medium_id)
                .setContentTitle("FOREGROUND_SERVICE")
                .setContentText("Service is running...")
                .setSmallIcon(R.drawable.baseline_fastfood_24)
                .setContentIntent(pendingIntent).build();

        startForeground(1, notification);

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
