package com.example.afinal;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LodingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loding);

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        startLoading();



    }


    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(LodingActivity.this,
                        HomeActivity.class);
                startActivity(intent);
                LodingActivity.this.finish();
                finish();
            }
        }, 4000);
    }
}
