package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.ParseInstallation;

public class MainActivity extends AppCompatActivity {
    final Handler handler= new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToNext(getCurrentFocus());
            }
        }, 2000);

    }
    public void moveToNext(View v){
        Intent intent= new Intent(MainActivity.this,SignUp.class);
        startActivity(intent);

    }
}
