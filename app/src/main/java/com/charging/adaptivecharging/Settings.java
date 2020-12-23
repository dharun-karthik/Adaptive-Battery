package com.charging.adaptivecharging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

public class Settings extends AppCompatActivity {
    TextView textview1;
    TimePicker timepicker;
    Button changetime;
    public SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        textview1=(TextView)findViewById(R.id.textView1);
        timepicker=(TimePicker)findViewById(R.id.timePicker);
        timepicker.setIs24HourView(false);
        changetime=(Button)findViewById(R.id.button1);
        textview1.setText(getCurrentTime());

        sharedPreferences = getSharedPreferences("wakeTime", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        textview1.setText("wake Time: "+String.valueOf(sharedPreferences.getInt("hours",6))+":"+String.valueOf(sharedPreferences.getInt("min",0)));
        changetime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                textview1.setText(getCurrentTime());
                editor.putInt("hours",timepicker.getCurrentHour());
                editor.putInt("min",timepicker.getCurrentMinute());
                editor.commit();
            }
        });

    }

    public String getCurrentTime(){
        String currentTime="Wake Time: "+timepicker.getCurrentHour()+":"+timepicker.getCurrentMinute();
        return currentTime;
    }

}