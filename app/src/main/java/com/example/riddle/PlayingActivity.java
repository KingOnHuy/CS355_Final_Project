package com.example.riddle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class PlayingActivity extends AppCompatActivity {
    private int score = 0;
    private CountDownTimer timer;

    private String category;
    private ArrayList<Integer> setImg;
    private ArrayList<String> keySet;
    private JSONObject jsonOfCategory;

    private String currentWord;

    // Time out 10 sec
    private long timeOut = 15000;
    private long timeRemain = 15000;
    private GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Let's play");
        setContentView(R.layout.activity_playing);

        category = getIntent().getStringExtra("category");

        final TextView scoreTxt = (TextView) findViewById(R.id.scoreText);
        final Button giveUpBtn = (Button) findViewById(R.id.giveUpBtn);
        final Button subminBtn = (Button) findViewById(R.id.subminBtn);
        final EditText wordInput = (EditText) findViewById(R.id.wordInput);

        InputStream is = getResources().openRawResource(R.raw.word);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        JSONObject jObj = null;
        try {
            jObj = new JSONObject(jsonString);
            try {
                jsonOfCategory = jObj.getJSONObject(category);
                Iterator<String> iterAllKeyInCate = jsonOfCategory.keys();
                keySet = new ArrayList<>();
                while (iterAllKeyInCate.hasNext()) {
                    keySet.add(iterAllKeyInCate.next());
                }
                randomImg();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setImgRiddle();

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
                goToResult("Give Up!");
            }
        });

        subminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentWord.equalsIgnoreCase(wordInput.getText().toString())) {
                    score += timeRemain;
                    timer.cancel();
                    startTimer(timeOut);
                    scoreTxt.setText("Score: " + score);
                    try {
                        randomImg();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    wordInput.setText("");
                    setImgRiddle();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "ผิดจร้า ลองใหม่", Toast.LENGTH_SHORT);
                    View view = toast.getView();
                    TextView text = (TextView) view.findViewById(android.R.id.message);
                    text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
                    text.setTextColor(Color.RED);
                    text.setTextSize(20);

                    toast.show();
                }
            }
        });

        startTimer(timeOut);
    }

    private void setImgRiddle() {
        gridview = (GridView) findViewById(R.id.listImgRiddle);
        gridview.setAdapter(new ImageAdapter(this, setImg));
    }

    private void randomImg() throws JSONException {
        int max = keySet.size() - 1;
        int min = 0;
        currentWord = keySet.get(new Random().nextInt(max - min + 1) + min);
        Log.d(null, "Current word : " + currentWord);
        JSONArray imgSet = jsonOfCategory.getJSONArray(currentWord);
        setImg = new ArrayList<>();
        for (int i = 0; i < imgSet.length(); i++) {
            setImg.add(getResources().getIdentifier(imgSet.getString(i), "drawable", getPackageName()));
        }
    }

    private void goToResult(String msg) {
        timer.cancel();
        Intent intent = new Intent(PlayingActivity.this, ResultActivity.class);
        intent.putExtra("score", score + "");
        intent.putExtra("msg", msg);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void startTimer(long setTime) {
        final TextView timerTxt = (TextView) findViewById(R.id.timerTxt);
        timer = new CountDownTimer(setTime, 100) {

            public void onTick(long millisUntilFinished) {
                timeRemain = millisUntilFinished;
                timerTxt.setText(millisUntilFinished / 1000.0 + " Seconds remaining");
            }

            public void onFinish() {
                timerTxt.setText("Timeout!");
                goToResult("Time Out!");
            }
        }.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            timer.cancel();
        } else {
            timer.cancel();
            startTimer(timeRemain);
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onPause() {
        timer.cancel();
//        finish();
        super.onPause();
    }
}