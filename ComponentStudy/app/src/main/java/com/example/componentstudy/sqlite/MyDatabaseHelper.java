package com.example.componentstudy.sqlite;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static String TAG = "MyDatabaseHelper";

    private static MyDatabaseHelper mHelper = null;

    public static  final String ID="_id";
    public static  final String NAME="name";
    public static  final String AGE="age";
    public static  final String SEX="sex";
    public static  final String TABLE_NAME="Book";



    public MyDatabaseHelper(@Nullable Context context) {
        super(context,"persons", null,1);
        SQLiteDatabase.loadLibs(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String sql = "CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +" ("+
//                "_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
//                "name VARCHAR NOT NULL,"+
//                "author VARCHAR NOT NULL,"+
//                "pages INTEGER NOT NULL,"+
//                "prices DOUBLE NOT NULL);";
//        db.execSQL(sql);

        db.execSQL("CREATE TABLE "+TABLE_NAME+"("+
                ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +NAME+" TEXT NOT NULL,"
                +AGE+" INTEGER,"
                +SEX+" TEXT"+")");
        Log.d(TAG,"database is created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Book");
        onCreate(db);
    }
}
