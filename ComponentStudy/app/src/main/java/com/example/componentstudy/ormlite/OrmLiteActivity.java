package com.example.componentstudy.ormlite;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.componentstudy.R;
import com.example.componentstudy.ormlite.dao.AccountDao;
import com.example.componentstudy.ormlite.entity.Account;

import java.sql.SQLException;

public class OrmLiteActivity extends AppCompatActivity {

    AccountDao accountDao;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ormlite);

        Account account = new Account();
        account.setName("coder");
        account.setPassword("123");

        accountDao = new AccountDao(OrmLiteActivity.this);
        try {
            accountDao.insertOrUpdateAccount(account);
            Toast.makeText(this, "insert account success!", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void orm_query(View view) {
        try {
            Account account = accountDao.queryAccount("coder");
            Toast.makeText(this, "name: "+account.getName()+" password: "+account.getPassword(), Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
