package apps.ejemplo.rutmapleon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //verifica si no hay una sesion iniciada(Login).
                SharedPreferences preferences = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
                boolean sesion = preferences.getBoolean("sesion",false);
                if (sesion){
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    finish();
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    guardarpreferencias();
                    startActivity(intent);
                    finish();
                }

            }
        },4000);
    }
    private void guardarpreferencias(){
        SharedPreferences preferences = getSharedPreferences("PreferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("correo","user@user.com");
        editor.putString("contraseña","User123*");
        editor.putBoolean("sesion",true);
        editor.commit();
    }
}