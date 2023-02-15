package com.example.componentstudy.ormlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.componentstudy.ormlite.entity.Account;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class AccountDatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAg = "WeatherDatabaseHelper";

    private static final String DATABASE_NAME="account.db";
    private static final int DATABASE_VERSION = 1;

    private static volatile AccountDatabaseHelper instance;

    public AccountDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Account.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onCreate(database, connectionSource);
    }

    public static AccountDatabaseHelper getInstance(Context context){
        context = context.getApplicationContext();
        if (instance==null){
            synchronized (AccountDatabaseHelper.class){
                if (instance==null){
                    instance = new AccountDatabaseHelper(context);
                }
            }
        }
        return instance;
    }

    @Override
    public void close() {
        super.close();
        DaoManager.clearCache();
    }

    public <D extends Dao<T,?>,T> D getAccountDao(Class<T> clazz){
        try {
            return getDao(clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
