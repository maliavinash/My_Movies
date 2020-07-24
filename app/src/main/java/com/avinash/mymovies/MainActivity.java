package com.avinash.mymovies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Loading app resources if required
        LoadAppResource();

    }

    private void LoadAppResource() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homePageIntent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(homePageIntent);
                finish();
            }
        }, 1000);
    }
}