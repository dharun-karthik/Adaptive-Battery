package com.charging.adaptivecharging;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    TextView textview;
    Button button;
    ImageButton imgbutton;

    public SharedPreferences sharedPreferences,sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        button = (Button)findViewById(R.id.button);
        imgbutton = (ImageButton) this.findViewById(R.id.imageButton2);
        imgbutton.setColorFilter(Color.argb(255, 255, 0, 0));
        textview = (TextView)findViewById(R.id.textView);


        ShellExecuter exe = new ShellExecuter();
        sharedPreferences = getSharedPreferences(" ", MODE_PRIVATE);
        sp = getSharedPreferences("wakeTime", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final SharedPreferences.Editor editsp = sp.edit();
        //switch1.setChecked(sharedPreferences.getBoolean(" ", false));
        runswitch();

        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferences.getBoolean(" ", false)==false){
                    imgbutton.setColorFilter(Color.argb(255, 0, 255, 0));
                    button.setVisibility(View.INVISIBLE);
                    editor.putBoolean(" ", true);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), NewService.class);
                    intent.setAction(NewService.ACTION_START_FOREGROUND_SERVICE);
                    startForegroundService(intent);
                }else{
                    imgbutton.setColorFilter(Color.argb(255, 255, 0, 0));
                    button.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(getApplicationContext(), NewService.class);
                    intent.setAction(NewService.ACTION_STOP_FOREGROUND_SERVICE);
                    startForegroundService(intent);
                    editor.putBoolean(" ", false);
                    editor.commit();
                }
                //Toast.makeText(getApplicationContext(),"You download is resumed",Toast.LENGTH_LONG).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });

    }

    private void runswitch() {

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if((sp.getInt("hours",6)==0)){
                    textview.setText("Wake Time: 12:00 AM");
                }else if((sp.getInt("hours",6)==12)){
                    textview.setText("Wake Time: 12:00 PM");
                }
                else if((sp.getInt("hours",6)>12)){
                    textview.setText("Wake Time: "+String.valueOf(sp.getInt("hours",6)-12)+":"+String.valueOf(sp.getInt("min",00))+" PM");
                }else{
                    textview.setText("Wake Time: "+String.valueOf(sp.getInt("hours",6))+":"+String.valueOf(sp.getInt("min",00))+" AM");
                }


                if(sharedPreferences.getBoolean(" ", false)){
                    button.setVisibility(View.INVISIBLE);
                    imgbutton.setColorFilter(Color.argb(255, 0, 255, 0));
                }else{
                    button.setVisibility(View.VISIBLE);
                    imgbutton.setColorFilter(Color.argb(255, 255, 0, 0));
                }
                Log.d("run","running");
                handler.postDelayed(this, 500);
            }
        });
    }

}