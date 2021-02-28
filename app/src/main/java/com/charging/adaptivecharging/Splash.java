package com.charging.adaptivecharging;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class Splash extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 100;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash);
        ShellExecuter exe = new ShellExecuter();
        if (!exe.RootCheck()) {
            Toast.makeText(getApplicationContext(), "Root Access Needed", Toast.LENGTH_SHORT).show();
            finish();
            System.exit(0);
        }
        Intent mainIntent = new Intent(Splash.this,MainActivity.class);
        startActivity(mainIntent);
        finish();
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                /* Create an Intent that will start the Menu-Activity. */
//                Intent mainIntent = new Intent(Splash.this,MainActivity.class);
//                Splash.this.startActivity(mainIntent);
//                Splash.this.finish();
//            }
//        }, SPLASH_DISPLAY_LENGTH);
//    }
    }
}