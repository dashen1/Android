package com.example.componentstudy.sqlite;

import androidx.annotation.IntRange;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;

import net.sqlcipher.database.SQLiteDatabase;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.componentstudy.R;

public class SQLiteActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    MyDatabaseHelper helper;

    // close db after op.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite);

        helper = new MyDatabaseHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase("hello");
        ContentValues values = new ContentValues();
        values.put("name", "Books");
        values.put("age", 26);
        values.put("sex", "boy");
        db.insert("Book", null, values);
        db.close();
    }

    public void query(View view) {

        SQLiteDatabase db = helper.getWritableDatabase("hello");
        Cursor cursor = null;
        try {
            cursor = db.query("Book", null, null, null, null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(getColumnIndex(cursor, "name"));
                    int age = cursor.getInt(getColumnIndex(cursor, "age"));
                    String sex = cursor.getString(getColumnIndex(cursor, "sex"));
                    Log.d(TAG, "name:" + name + " " + "age:" + age + " " + "sex:" + sex + " ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }


    }

    @IntRange(from = 0)
    public static int getColumnIndex(Cursor cursor, String columnName) throws Exception {
        int index = cursor.getColumnIndex(columnName);
        if (index <= -1) {
            throw new Exception(TAG + " can not find column \"" + columnName + "\" in cursor, please check!");
        }
        return index;
    }

}