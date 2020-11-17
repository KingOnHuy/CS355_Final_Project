package com.example.riddle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class PlayingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Let's play");
        setContentView(R.layout.activity_playing);
    }
}