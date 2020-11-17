package com.example.riddle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PlayingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Let's play");
        setContentView(R.layout.activity_playing);

        final Button giveUpBtn = (Button) findViewById(R.id.giveUpBtn);
        final Button subminBtn = (Button) findViewById(R.id.subminBtn);

        giveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayingActivity.this, MainActivity.class));
            }
        });

        subminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayingActivity.this, ResultActivity.class));
            }
        });
    }
}