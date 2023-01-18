package com.example.rutmapleon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences= getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
                boolean sesion = preferences.getBoolean("sesion",false);
                if (sesion){
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },7000);
    }
}