package com.example.tamz2test.Data;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tamz2test.Config;
import com.example.tamz2test.MainActivity;
import com.example.tamz2test.R;
import com.example.tamz2test.Utils;

public class Notifications extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Config config = Config.loadConfig(context);
        if (!config.isNotifyOn()) {
            return;
        }

        Intent nextIntent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nextIntent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Utils.notificationChannelName)
                .setSmallIcon(R.drawable.car_24_icon)
                .setContentTitle(context.getString(R.string.notificationsTitle))
                .setContentText(context.getString(R.string.notificationsText))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(1, builder.build());
    }
}
