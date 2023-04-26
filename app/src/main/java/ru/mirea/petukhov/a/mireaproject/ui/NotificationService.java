package ru.mirea.petukhov.a.mireaproject.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import ru.mirea.petukhov.a.mireaproject.MainActivity;
import ru.mirea.petukhov.a.mireaproject.R;

public class NotificationService extends Service {
    private final String CHANNEL_ID = "notification_service";
    public static long result = 0;
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Notification service", "start computing...");
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onCreate(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentText("Computing mega long value...")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Computing mega long value..."))
                .setContentTitle("");
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "COMPUTING", importance);

        channel.setDescription("notification service");
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.createNotificationChannel(channel);
        startForeground(1, builder.build());
    }
    @Override
    public void onDestroy(){
        stopForeground(true);
    }
}