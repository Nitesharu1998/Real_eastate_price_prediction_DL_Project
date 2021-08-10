package com.example.realeastatepriceprediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

public class splasher extends AppCompatActivity {
    public static final String PREFERENCE= "preference";
    ProgressBar pb;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splasher);
        pb=findViewById(R.id.splashpbar);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pb.setVisibility(View.VISIBLE);

                SharedPreferences sharedPreferences =getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                SharedPreferences.Editor ed= sharedPreferences.edit();
                String email= sharedPreferences.getString("email",null);
                if (email!=null){

                    startActivity(new Intent(splasher.this,homepage.class));
                    finish();

                }
                else {startActivity(new Intent(splasher.this,MainActivity.class));
                finish();}
            }
        },3000);
    }
}