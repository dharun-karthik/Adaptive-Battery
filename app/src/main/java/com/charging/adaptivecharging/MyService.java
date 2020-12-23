package com.charging.adaptivecharging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {

    private static final String TAG_FOREGROUND_SERVICE = "FOREGROUND_SERVICE";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    public SharedPreferences sharedPreferences;
    public Integer h;
    public Integer m;

    public Handler mhandler=new Handler();
    ShellExecuter exe = new ShellExecuter();

    Runnable myRunnable =new Runnable() {
        @Override
        public void run() {
            Integer hh = Integer.parseInt(new SimpleDateFormat("kk", Locale.getDefault()).format(new Date()));
            Integer mm = Integer.parseInt(new SimpleDateFormat("mm", Locale.getDefault()).format(new Date()));

            Float totalTime;
            int TC,TS;
            TC = (hh*60)+mm;
            TS = (h*60)+m;
            if(TC<TS){
                totalTime=(float)TS-TC;
            }else{
                totalTime=(float)(1400-TC+TS);
            }
            totalTime/=60;


            BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
            int percentage = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);

            Float caprem=(float)(4000-(percentage*4000/100));

            Integer ma=(int)(caprem/totalTime);
            if(ma>=2800){
                ma=2800;
            }else if(ma<50){
                ma=50;
            }
            ma+=50;
            ma*=1000;


            exe.Executer(ma.toString());
            Log.d("Max_current",ma.toString());
            mhandler.postDelayed(this,1200000);
        }
    };


    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = getSharedPreferences("wakeTime", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        h=sharedPreferences.getInt("hours",6);
        m=sharedPreferences.getInt("min",0);
        Log.d(TAG_FOREGROUND_SERVICE, m+"My foreground service onCreate()."+h);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if(action!=null)

                switch (action) {
                    case ACTION_START_FOREGROUND_SERVICE:
                        startForegroundService();
                        myRunnable.run();
                        Toast.makeText(getApplicationContext(), "Adaptive Charging Enabled", Toast.LENGTH_LONG).show();
                        break;
                    case ACTION_STOP_FOREGROUND_SERVICE:
                        stopForegroundService();
                        Toast.makeText(getApplicationContext(), "Adaptive Charging Disabled", Toast.LENGTH_LONG).show();
                        mhandler.removeCallbacks(myRunnable);
                        exe.Executer("2800000");
                        Log.d("Reverted","2800000");
                        break;
                }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /* Used to build and start foreground service. */
    private void startForegroundService() {
        Log.d(TAG_FOREGROUND_SERVICE, "Start foreground service.");
        createNotificationChannel("my_service", "My Background Service");

    }

    private void createNotificationChannel(String channelId, String channelName) {
        Intent resultIntent = new Intent(this, MainActivity.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("AdaptiveCharging Enabled")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentIntent(resultPendingIntent) //intent
                .build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, notificationBuilder.build());
        startForeground(1, notification);
    }


    private void stopForegroundService() {
        Log.d(TAG_FOREGROUND_SERVICE, "Stop foreground service.");

        // Stop foreground service and remove the notification.
        stopForeground(true);

        // Stop the foreground service.
        stopSelf();
    }
}