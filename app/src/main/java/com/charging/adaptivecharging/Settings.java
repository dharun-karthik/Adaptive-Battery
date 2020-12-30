package com.charging.adaptivecharging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;

public class Settings extends AppCompatActivity {
    TimePicker timepicker;
    Button changetime;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_settings);

        timepicker=(TimePicker)findViewById(R.id.timePicker);
        timepicker.setIs24HourView(false);
        changetime=(Button)findViewById(R.id.button1);


        sharedPreferences = getSharedPreferences("wakeTime", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        changetime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                editor.putInt("hours",timepicker.getCurrentHour());
                editor.putInt("min",timepicker.getCurrentMinute());
                editor.commit();
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);
    }
}