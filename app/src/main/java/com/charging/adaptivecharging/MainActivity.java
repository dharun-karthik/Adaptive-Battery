package com.charging.adaptivecharging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    Switch switch1;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        switch1 = (Switch) findViewById(R.id.switch1);
        ShellExecuter exe = new ShellExecuter();
        sharedPreferences = getSharedPreferences(" ", MODE_PRIVATE);

        final SharedPreferences.Editor editor = sharedPreferences.edit();
        //switch1.setChecked(sharedPreferences.getBoolean(" ", false));
        runswitch();
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putBoolean(" ", true);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), MyService.class);
                    intent.setAction(MyService.ACTION_START_FOREGROUND_SERVICE);
                    startService(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), MyService.class);
                    intent.setAction(MyService.ACTION_STOP_FOREGROUND_SERVICE);
                    startService(intent);
                    editor.putBoolean(" ", false);
                    editor.commit();
                }
            }
        });
    }

    private void runswitch() {

        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                switch1.setChecked(sharedPreferences.getBoolean(" ", false));
                handler.postDelayed(this, 500);
            }
        });
    }
}