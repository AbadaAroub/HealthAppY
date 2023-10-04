package com.example.healthappy;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
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
    }
    //NOTIFICATIONPART
    public void requestNotificationPermissions() {
        //NOTIFICATION STUFF

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


    public void basic_notif(String title, String message) {
        notif_mngr = NotificationManagerCompat.from(activity);
        Notification notif = (new NotificationCompat.Builder(activity, App.channel_high_id)
                .setSmallIcon(R.drawable.loginphoto)
                .setContentText(message)
                .setContentTitle(title)
                .setPriority(android.app.NotificationManager.IMPORTANCE_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        ).build();
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
            notif_mngr.notify(0, notif);
        else
            new AlertDialog.Builder(activity).setMessage("FFFuuuuuuuuuu---").create().show();

    }
}
