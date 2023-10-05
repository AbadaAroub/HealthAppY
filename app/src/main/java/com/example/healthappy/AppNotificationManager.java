package com.example.healthappy;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class AppNotificationManager {
    private static final int NOTIFICATION_POST_CODE = 1;
    private NotificationManagerCompat notif_mngr;
    private AppCompatActivity activity;
    public AppNotificationManager(AppCompatActivity activity) {
        this.activity = activity;
        requestNotificationPermissions();
        notif_mngr = NotificationManagerCompat.from(activity);
    }
    //NOTIFICATIONPART
    private void requestNotificationPermissions() {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.POST_NOTIFICATIONS)) {
                new AlertDialog.Builder(activity)
                        .setIcon(R.drawable.elderly)
                        .setTitle(R.string.permission_request)
                        .setMessage(R.string.notif_post_perm)
                        .setPositiveButton(R.string.perm_accept, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_POST_CODE);
                            }
                        })
                        .setNegativeButton(R.string.perm_deny, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[] {android.Manifest.permission.POST_NOTIFICATIONS}, NOTIFICATION_POST_CODE);
            }
        }
    }


    public void discrete_notif(int n_id, String title, String message) {
        Notification notif = (new NotificationCompat.Builder(activity, App.channel_low_id)
                .setSmallIcon(R.drawable.baseline_fastfood_24)
                .setContentText(message)
                .setContentTitle(title)
                .setPriority(NotificationManager.IMPORTANCE_LOW)
        ).build();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
            notif_mngr.notify(n_id, notif);

    }

    public void medium_notif(int n_id, String title, String message) {
        Notification notif = (new NotificationCompat.Builder(activity, App.channel_medium_id)
                .setSmallIcon(R.drawable.baseline_fastfood_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
        ).build();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
            notif_mngr.notify(n_id, notif);
    }

    public void high_notif(int n_id,String title, String message) {
        Notification notif = (new NotificationCompat.Builder(activity, App.channel_high_id)
                .setSmallIcon(R.drawable.baseline_fastfood_24)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
        ).build();

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
            notif_mngr.notify(n_id, notif);
    }


}
