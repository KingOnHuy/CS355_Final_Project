package com.example.riddle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ResultActivity extends AppCompatActivity {
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("Result");

        final TextView scoreTxt = (TextView) findViewById(R.id.resultScore);
        final TextView highScore = (TextView) findViewById(R.id.highScore);
        final TextView outMsg = (TextView) findViewById(R.id.outMsg);
        long score = Long.parseLong(getIntent().getStringExtra("score"));
        outMsg.setText(getIntent().getStringExtra("msg"));
        DecimalFormat decim = new DecimalFormat("#,###.##");
        dbManager = new DBManager(this);
        dbManager.open();
        Log.d(null, "MAX "+Integer.parseInt(dbManager.getMaxScore()));
        int maxScore = Integer.parseInt(dbManager.getMaxScore());
        if (score > maxScore) {
            scoreTxt.setText("\nCongrats!\n Your score " + decim.format(score)+"\n is New High Score!!");
        } else {
            scoreTxt.setText("Your Score : " + decim.format(score));
            highScore.setText("High Score is "+decim.format(maxScore));

        }
        dbManager.insert(score);
        dbManager.close();

        final Button replayBtn = (Button) findViewById(R.id.replayBtn);
        final Button histotyBtn = (Button) findViewById(R.id.resultHistoryBtn);
        final Button menuBtn = (Button) findViewById(R.id.menuBtn);

        replayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, PlayingActivity.class).putExtra("category", getIntent().getStringExtra("category")));
            }
        });

        histotyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, HistoryActivity.class));
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultActivity.this, CategoryActivity.class));
            }
        });
    }
}