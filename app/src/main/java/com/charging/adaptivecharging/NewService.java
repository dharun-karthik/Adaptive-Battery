package com.charging.adaptivecharging;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewService extends Service{
    private static final String CHANNEL = "Adaptivecharging_notification_channel";
    private static final String notificationText = "Adaptive Charging Enabled";
    private static final String notificationTitle = "Services";
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    public SharedPreferences sharedPreferences,sp;
    public Integer h;
    public Integer m;
    public Integer cap;
    public String max;
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

            Float caprem=(float)(cap-(percentage*cap/100));

            Integer ma=(int)(caprem/totalTime);
            if(ma>=Integer.parseInt(max)){
                ma=Integer.parseInt(max);
            }else if(ma<100){
                ma=100;
            }
            ma*=1000;


            exe.Executer(ma.toString());
            mhandler.postDelayed(this,120000);
        }
    };




    public int onStartCommand(Intent i, int flag, int startID) {
        sp = getSharedPreferences(" ", MODE_PRIVATE);
        final SharedPreferences.Editor editsp = sp.edit();

        if (i.getAction().equals(ACTION_START_FOREGROUND_SERVICE)) {
            try {
                max = exe.Get("cat /sys/class/power_supply/battery/constant_charge_current_max");
                cap = Integer.parseInt(exe.Get("cat /sys/class/power_supply/battery/charge_full"));
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Device Not Supported",Toast.LENGTH_SHORT).show();
                editsp.putBoolean(" ", false);
                editsp.commit();
                stopSelf();
                stopForeground(true);
            }
            cap/=1000;
            sharedPreferences = getSharedPreferences("wakeTime", MODE_PRIVATE);
            final SharedPreferences.Editor editor = sharedPreferences.edit();
            h=sharedPreferences.getInt("hours",6);
            m=sharedPreferences.getInt("min",0);
            myRunnable.run();
            Toast.makeText(getApplicationContext(), "Adaptive Charging Enabled", Toast.LENGTH_LONG).show();

        } else if (i.getAction().equals(ACTION_STOP_FOREGROUND_SERVICE)) {
            mhandler.removeCallbacks(myRunnable);
            exe.Executer(max);
            stopSelf();
            stopForeground(true);
            Toast.makeText(getApplicationContext(), "Adaptive Charging Disabled", Toast.LENGTH_LONG).show();

        }
        return START_STICKY;
    }

    public void onCreate() {

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            try {
                notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL, "Adaptive Charging", NotificationManager.IMPORTANCE_DEFAULT));
            } catch (IllegalArgumentException e) {
                notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL, "Adaptive Charging", NotificationManager.IMPORTANCE_DEFAULT));
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);
            Notification.Builder builder = new Notification.Builder(this, CHANNEL);
            builder.setContentTitle(notificationTitle).setContentText(notificationText).setSmallIcon(R.drawable.ic_noti).setContentIntent(pendingIntent).setOngoing(true);
            startForeground(3, builder.build());
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {

            stopForeground(true);
        } catch (NullPointerException e) {
        }
    }

    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}

