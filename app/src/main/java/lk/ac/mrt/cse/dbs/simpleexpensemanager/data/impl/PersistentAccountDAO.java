package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.List;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;


public class PersistentAccountDAO implements AccountDAO {

    private DatabaseHandler db;

    public PersistentAccountDAO(DatabaseHandler db){
        this.db=db;
    }
    @Override
    public List<String> getAccountNumbersList() {
        List<String> accountNumbersList = new ArrayList<>();
        String query = "SELECT ACCOUNT_NO FROM TABLE_ACCOUNTS";

        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor cursor = sdb.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // Adding accountNumber to list
                accountNumbersList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // return accountNumbers list
        return accountNumbersList;

    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountsList = new ArrayList<>();
        String query = "SELECT * FROM TABLE_ACCOUNTS";

        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor cursor = sdb.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3));
                // Adding account to list
                accountsList.add(account);
            } while (cursor.moveToNext());
        }

        // return accounts list
        return accountsList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sdb = db.getReadableDatabase();
        Cursor cursor = sdb.query("TABLE_ACCOUNTS", new String[] { "ACCOUNT_NO", "BANK_NAME","HOLDER_NAME","BALANCE"}, "ACCOUNT_NO" + "=?",
                new String[] { String.valueOf(accountNo) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Account account = new Account(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getDouble(3));

        return account;
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sdb = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ACCOUNT_NO", account.getAccountNo());
        values.put("BANK_NAME", account.getBankName());
        values.put("HOLDER_NAME", account.getAccountHolderName());
        values.put("BALANCE", account.getBalance());

        sdb.insert("TABLE_ACCOUNTS", null, values);

        sdb.close(); // Closing database connection
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

            SQLiteDatabase sdb = db.getWritableDatabase();
            sdb.delete("TABLE_ACCOUNTS", "ACCOUNT_NO" + " = ?", new String[] { accountNo });
            sdb.close();

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        SQLiteDatabase sdb = db.getWritableDatabase();
        Account account = this.getAccount(accountNo);

        ContentValues values = new ContentValues();

        switch (expenseType) {
            case EXPENSE:
                values.put("BALANCE", account.getBalance() - amount);
                break;
            case INCOME:
                values.put("BALANCE", account.getBalance() + amount);
                break;
        }

        // updating row
        sdb.update("TABLE_ACCOUNTS", values, "ACCOUNT_NO" + " = ?",
                new String[] { accountNo });
    }
}
