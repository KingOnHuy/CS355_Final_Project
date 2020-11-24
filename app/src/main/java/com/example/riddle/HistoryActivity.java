package com.example.riddle;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class HistoryActivity extends AppCompatActivity {
    private DBManager dbManager;
    private SimpleCursorAdapter adapter;

    final String[] from = new String[]{
            DatabaseHelper.TIME,
            DatabaseHelper.SCORE,
    };

    final int[] to = new int[]{
            R.id.timeScoreColumn,
            R.id.scoreNumberColumn,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setTitle("History");
        setContentView(R.layout.activity_history);

        final ListView historyListView = (ListView) findViewById(R.id.historyList);
        historyListView.setEmptyView(findViewById(R.id.empty));

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();
        final TextView highScoreTxt = (TextView) findViewById(R.id.highScoreTxt);
        highScoreTxt.setText("High Score is " + dbManager.getMaxScore());

        adapter = new SimpleCursorAdapter(this, R.layout.list_score, cursor, from, to, 0);
        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(columnIndex==2) {
                    Double preco = cursor.getDouble(columnIndex);
                    TextView textView = (TextView) view;
                    textView.setText(String.format("%1$,.0f", preco));
                    return true;
                }
                return false;
            }
        });
        adapter.notifyDataSetChanged();

        historyListView.setAdapter(adapter);
        dbManager.close();
    }
}