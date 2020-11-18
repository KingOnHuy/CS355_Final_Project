package com.example.riddle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("Result");

        final TextView scoreTxt = (TextView) findViewById(R.id.resultScore);
        final TextView outMsg = (TextView) findViewById(R.id.outMsg);
        int score = Integer.parseInt(getIntent().getStringExtra("score"));
        outMsg.setText(getIntent().getStringExtra("msg"));
        scoreTxt.setText("Score: " + score);

        dbManager = new DBManager(this);

        dbManager.open();
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
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
            }
        });
    }
}