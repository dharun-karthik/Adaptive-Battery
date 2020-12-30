package com.charging.adaptivecharging;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity{
    TextView textview;
    ImageButton button;
    ImageButton imgbutton;
    final Handler handler = new Handler();
    public SharedPreferences sharedPreferences,sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        button = (ImageButton) findViewById(R.id.button);
        imgbutton = (ImageButton) this.findViewById(R.id.imageButton2);
        imgbutton.setColorFilter(Color.argb(255, 255, 0, 0));
        textview = (TextView)findViewById(R.id.textView);

        sharedPreferences = getSharedPreferences(" ", MODE_PRIVATE);
        sp = getSharedPreferences("wakeTime", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final SharedPreferences.Editor editsp = sp.edit();
        runswitch();
        //Log.d("tag",exe.Get("cat cat /sys/class/power_supply/battery/charge_full"));

        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sharedPreferences.getBoolean(" ", false)==false){
                    imgbutton.setColorFilter(Color.argb(255, 0, 242, 254));
                    button.setVisibility(View.INVISIBLE);
                    editor.putBoolean(" ", true);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), NewService.class);
                    intent.setAction(NewService.ACTION_START_FOREGROUND_SERVICE);
                    startForegroundService(intent);
                }else{
                    imgbutton.setColorFilter(Color.argb(255, 33, 147, 176));
                    button.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(getApplicationContext(), NewService.class);
                    intent.setAction(NewService.ACTION_STOP_FOREGROUND_SERVICE);
                    startForegroundService(intent);
                    editor.putBoolean(" ", false);
                    editor.commit();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                finish();
            }
        });

    }



    private void runswitch() {


        handler.post(new Runnable() {
            @Override
            public void run() {
                String s;
                if(sp.getInt("min",00)>9){
                    if(sp.getInt("hours",6)==0){
                        s="12:"+String.valueOf(sp.getInt("min",00))+" AM";
                    }else if(sp.getInt("hours",6)==12){
                        s="12:"+String.valueOf(sp.getInt("min",0))+" PM";
                    }else if((sp.getInt("hours",6)>12)){
                        s=(String.valueOf(sp.getInt("hours",6)-12)+":"+String.valueOf(sp.getInt("min",00))+" PM");
                    }else{
                        s=(String.valueOf(sp.getInt("hours",6))+":"+String.valueOf(sp.getInt("min",00))+" AM");
                    }
                }else{
                    if(sp.getInt("hours",6)==0){
                        s="12:0"+String.valueOf(sp.getInt("min",00))+" AM";
                    }else if(sp.getInt("hours",6)==12){
                        s="12:0"+String.valueOf(sp.getInt("min",0))+" PM";
                    }else if((sp.getInt("hours",6)>12)){
                        s=(String.valueOf(sp.getInt("hours",6)-12)+":0"+String.valueOf(sp.getInt("min",00))+" PM");
                    }else{
                        s=(String.valueOf(sp.getInt("hours",6))+":0"+String.valueOf(sp.getInt("min",00))+" AM");
                    }
                }
                textview.setText(s);


                if(sharedPreferences.getBoolean(" ", false)){
                    button.setVisibility(View.INVISIBLE);
                    imgbutton.setColorFilter(Color.argb(255, 0, 242, 254));
                }else{
                    button.setVisibility(View.VISIBLE);
                    imgbutton.setColorFilter(Color.argb(255, 33, 147, 176));
                }

                handler.postDelayed(this, 500);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}