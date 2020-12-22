package com.charging.adaptivecharging;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;
import android.widget.Toast;

public class QSTile extends TileService {

    private Context f73c;
    public int flag=0;
    public SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedPreferenceEditor;
    Tile tile;

    class C03711 implements SharedPreferences.OnSharedPreferenceChangeListener {
        C03711() {
        }

        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            QSTile qSTileServiceWave = QSTile.this;
            qSTileServiceWave.tile = qSTileServiceWave.getQsTile();
            if (QSTile.this.sharedPreferences.getBoolean(" ", false)) {
                QSTile.this.tile.setState(2);
            } else {
                QSTile.this.tile.setState(1);
            }
            QSTile.this.tile.updateTile();
        }
    }

    @Override
    public void onStartListening() {
        this.f73c = getApplicationContext();
        this.sharedPreferences = getSharedPreferences(" ", 0);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(new C03711());
        this.tile = getQsTile();
        Log.i("listening", String.valueOf(this.tile.getState()));
    }

    @Override
    public void onClick() {
        if (this.tile.getState() == 1) {
            enableHandWave(this.f73c, true);
            this.tile.setState(2);
        } else if (this.tile.getState() == 2) {
            enableHandWave(this.f73c, false);
            this.tile.setState(1);
        }
        this.tile.updateTile();

    }
    public void enableHandWave(Context context, boolean state) {
        this.sharedPreferences = context.getSharedPreferences(" ", 0);
        this.sharedPreferenceEditor = this.sharedPreferences.edit();
        if (state) {
            Intent intent = new Intent(context, MyService.class);
            intent.setAction(MyService.ACTION_START_FOREGROUND_SERVICE);
            startService(intent);
            this.sharedPreferenceEditor.putBoolean(" ", true).apply();
            return;
        }else {
            Intent intent = new Intent(context, MyService.class);
            intent.setAction(MyService.ACTION_STOP_FOREGROUND_SERVICE);
            startService(intent);
            this.sharedPreferenceEditor.putBoolean(" ", false).apply();
        }
    }
}
