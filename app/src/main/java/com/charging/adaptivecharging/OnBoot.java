package com.charging.adaptivecharging;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class OnBoot extends BroadcastReceiver {

    SharedPreferences sp;

    @Override
    public void onReceive(Context context, Intent intent) {
        sp=context.getSharedPreferences(" ", MODE_PRIVATE);
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) && sp.getBoolean(" ", false) ) {
            Intent i = new Intent(context, NewService.class);
            i.setAction(NewService.ACTION_START_FOREGROUND_SERVICE);
            context.startForegroundService(i);

        }
    }
}
