package com.example.riddle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class PlayingActivity extends AppCompatActivity {
    private int score = 0;
    private CountDownTimer timer;

    private ArrayList<Integer> setImg;
    private ArrayList<String> keySet;
    private JSONObject jsonOfCategory;

    private String currentWord;

    // Time out 10 sec
    private long timerOut = 15000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Let's play");
        setContentView(R.layout.activity_playing);

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
                jsonOfCategory = jObj.getJSONObject(getIntent().getStringExtra("name"));
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
                    score++;
                    timer.cancel();
                    startTimer();
                    scoreTxt.setText("Score: " + score);
                    try {
                        randomImg();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    wordInput.setText("");
                    setImgRiddle();
                } else {
                    Toast.makeText(getApplicationContext(),"ผิดจร้า ลองใหม่",Toast.LENGTH_SHORT).show();
                }
            }
        });

        startTimer();
    }

    private void setImgRiddle() {
        GridView gridview = (GridView) findViewById(R.id.listImgRiddle);
        gridview.setAdapter(new ImageAdapter(this, setImg));
    }

    private void randomImg() throws JSONException {
        int max = keySet.size();
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
        startActivity(intent);
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
                goToResult("Time Out!");
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