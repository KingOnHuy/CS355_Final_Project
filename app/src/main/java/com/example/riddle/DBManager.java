package com.example.riddle;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public com.example.riddle.DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(long score) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.SCORE, score);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[]{
                DatabaseHelper._ID,
                DatabaseHelper.TIME,
                DatabaseHelper.SCORE,
        };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, DatabaseHelper.TIME + " DESC");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, long score) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.SCORE, score);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

    public String getMaxScore() {
        Cursor temp = database.query(DatabaseHelper.TABLE_NAME, new String [] {"MAX("+DatabaseHelper.SCORE+")"}, null, null, null, null, null);
        temp.moveToFirst();
        return temp.getString(0) == null ? "0" : temp.getString(0);
    }
}
