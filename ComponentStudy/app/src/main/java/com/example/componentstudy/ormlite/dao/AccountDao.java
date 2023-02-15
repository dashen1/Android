package com.example.componentstudy.ormlite.dao;

import android.content.Context;

import com.example.componentstudy.ormlite.AccountDatabaseHelper;
import com.example.componentstudy.ormlite.entity.Account;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.misc.TransactionManager;

import java.sql.SQLException;
import java.util.concurrent.Callable;

public class AccountDao {

    private Context context;

    private Dao<Account,String> accountDaoOperation;

    public AccountDao(Context context) {
        this.context = context;
        this.accountDaoOperation = AccountDatabaseHelper.getInstance(context).getAccountDao(Account.class);
    }

    public void insertOrUpdateAccount(Account account) throws SQLException {
        TransactionManager.callInTransaction(AccountDatabaseHelper.getInstance(context).getConnectionSource(), new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (accountDaoOperation.idExists(account.getName())){

                }else {
                    insertAccount(account);
                }
                return null;
            }
        });
    }

    private void insertAccount(Account account) throws SQLException {
        accountDaoOperation.create(account);
    }

    public Account queryAccount(String name) throws SQLException {
        return TransactionManager.callInTransaction(AccountDatabaseHelper.getInstance(context).getConnectionSource(), new Callable<Account>() {
            @Override
            public Account call() throws Exception {
                Account account = accountDaoOperation.queryForId(name);
                return account;
            }
        });
    }
}
