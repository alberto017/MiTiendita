package com.example.mitiendita;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DatabaseReference;

public class SplashScreen extends AppCompatActivity {

    //Declaracion de variables
    private LottieAnimationView lottieImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        lottieImage = findViewById(R.id.lottieImage);
        lottieImage.playAnimation();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent Acceso = new Intent(SplashScreen.this, Inicio.class);
                startActivity(Acceso);
                finish();
            }//run
        }, 6000);
    }//onCreate

}//SplashScreen