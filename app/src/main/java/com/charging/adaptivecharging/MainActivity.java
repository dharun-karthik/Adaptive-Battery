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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView textview;
    Switch switch1;
    Button button;

    public SharedPreferences sharedPreferences,sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);
        switch1 = (Switch) findViewById(R.id.switch1);
        textview = (TextView)findViewById(R.id.textView);
        ShellExecuter exe = new ShellExecuter();
        sharedPreferences = getSharedPreferences(" ", MODE_PRIVATE);
        sp = getSharedPreferences("wakeTime", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final SharedPreferences.Editor editsp = sp.edit();
        //switch1.setChecked(sharedPreferences.getBoolean(" ", false));
        runswitch();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    button.setVisibility(View.INVISIBLE);
                    editor.putBoolean(" ", true);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), MyService.class);
                    intent.setAction(MyService.ACTION_START_FOREGROUND_SERVICE);
                    startService(intent);
                } else {
                    button.setVisibility(View.VISIBLE);
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
                textview.setText("Wake Time: "+String.valueOf(sp.getInt("hours",6)+":"+String.valueOf(sp.getInt("min",00))));
                switch1.setChecked(sharedPreferences.getBoolean(" ", false));
                if(sharedPreferences.getBoolean(" ", false)==true){
                    button.setVisibility(View.INVISIBLE);
                }else{
                    button.setVisibility(View.VISIBLE);
                }
                handler.postDelayed(this, 500);
            }
        });
    }
}