package com.example.riddle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PlayingActivity extends AppCompatActivity {
    private int score = 0;
    private CountDownTimer timer;

    // Time out 10 sec
    private long timerOut = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Let's play");
        setContentView(R.layout.activity_playing);

        final TextView scoreTxt = (TextView) findViewById(R.id.scoreText);
        final Button giveUpBtn = (Button) findViewById(R.id.giveUpBtn);
        final Button subminBtn = (Button) findViewById(R.id.subminBtn);
        final EditText wordInput = (EditText) findViewById(R.id.wordInput);

        wordInput.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    subminBtn.performClick();
                    return true;
                }
                return false;
            }
        });

        giveUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayingActivity.this, MainActivity.class));
            }
        });

        subminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score++;
                timer.cancel();
                startTimer();
                scoreTxt.setText("Score: " + score);
            }
        });

        startTimer();
    }

    private void startTimer() {
        final TextView timerTxt = (TextView) findViewById(R.id.timerTxt);
        timerTxt.setText(10 + " Seconds remaining");
        timer = new CountDownTimer(timerOut, 100) {

            public void onTick(long millisUntilFinished) {
                timerTxt.setText(millisUntilFinished / 1000.0 + " Seconds remaining");
            }

            public void onFinish() {
                timerTxt.setText("Timeout!");
                startActivity(new Intent(PlayingActivity.this, ResultActivity.class));
            }
        }.start();
    }

    @Override
    public void onDetachedFromWindow() {
        timer.cancel();
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onNavigateUp() {
        timer.cancel();
        return super.onNavigateUp();
    }
}