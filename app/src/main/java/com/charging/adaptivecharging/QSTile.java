package com.charging.adaptivecharging;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;


public class QSTile extends TileService {

    private Context context;
    public SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferenceEditor;
    Tile tile;
    final Handler handler = new Handler();


    @Override
    public void onStartListening() {
        context = getApplicationContext();
        sharedPreferences = getSharedPreferences(" ", MODE_PRIVATE);
        runswitch();
        tile = getQsTile();
    }

    @Override
    public void onStopListening() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick() {
        if (tile.getState() == Tile.STATE_INACTIVE) {
            try {
                Runtime.getRuntime().exec("su");

                enable(context, true);
                tile.setState(Tile.STATE_ACTIVE);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Root Access Needed", Toast.LENGTH_SHORT).show();

            }
        }
        else if (tile.getState() == Tile.STATE_ACTIVE) {
            enable(context, false);
            tile.setState(Tile.STATE_INACTIVE);
        }


        tile.updateTile();

    }
    public void enable(Context context, boolean state) {
        sharedPreferences = context.getSharedPreferences(" ", MODE_PRIVATE);
        sharedPreferenceEditor = sharedPreferences.edit();
        if (state) {
            Intent intent = new Intent(context, NewService.class);
            intent.setAction(NewService.ACTION_START_FOREGROUND_SERVICE);
            startForegroundService(intent);
            sharedPreferenceEditor.putBoolean(" ", true);
            sharedPreferenceEditor.commit();
            return;
        }else {
            Intent intent = new Intent(context, NewService.class);
            intent.setAction(NewService.ACTION_STOP_FOREGROUND_SERVICE);
            startForegroundService(intent);
            sharedPreferenceEditor.putBoolean(" ", false).apply();
        }
    }
    private void runswitch() {


        handler.post(new Runnable() {
            @Override
            public void run() {

                QSTile qSTileServiceWave = QSTile.this;
                qSTileServiceWave.tile = qSTileServiceWave.getQsTile();
                if (QSTile.this.sharedPreferences.getBoolean(" ", false)) {
                    QSTile.this.tile.setState(Tile.STATE_ACTIVE);
                } else {
                    QSTile.this.tile.setState(Tile.STATE_INACTIVE);
                }
                QSTile.this.tile.updateTile();
                handler.postDelayed(this, 500);
            }
        });
    }
}