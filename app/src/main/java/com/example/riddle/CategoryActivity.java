package com.example.riddle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Category");
        setContentView(R.layout.activity_category);
        final Button cate1 = (Button) findViewById(R.id.province);
        final Button cate2 = (Button) findViewById(R.id.vegetable);
        final Button cate3 = (Button) findViewById(R.id.fruit);
        final Button cate4 = (Button) findViewById(R.id.food);
        cate1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, PlayingActivity.class).putExtra("category", "province"));
            }
        });
        cate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, PlayingActivity.class).putExtra("category", "vegetable"));
            }
        });
        cate3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, PlayingActivity.class).putExtra("category", "fruit"));
            }
        });
        cate4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, PlayingActivity.class).putExtra("category", "food"));
            }
        });
    }
}