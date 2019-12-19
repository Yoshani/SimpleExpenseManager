package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.DatabaseHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;


public class PersistentTransactionDAO implements TransactionDAO  {
    private DatabaseHandler dbh;

    public PersistentTransactionDAO(DatabaseHandler db){
        dbh=db;
    }
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = dbh.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("DATE", new SimpleDateFormat("dd/MM/yyyy").format(date));
        values.put("ACCOUNT_NO", accountNo);
        values.put("EXPENSE_TYPE", expenseType.name());
        values.put("AMOUNT", amount);

        db.insert("TABLE_TRANSACTIONS", null, values);

        db.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionsList = new ArrayList<>();
        String query = "SELECT * FROM TABLE_TRANSACTIONS";

        SQLiteDatabase sdb = dbh.getReadableDatabase();
        Cursor cursor = sdb.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Date sDate= null;
                try {
                    sDate = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(2));
                }catch (ParseException e){}

                switch (cursor.getString(3)) {
                    case "EXPENSE":
                        Transaction transaction = new Transaction(sDate, cursor.getString(1), ExpenseType.EXPENSE, cursor.getDouble(4));
                        transactionsList.add(transaction);
                        break;
                    case "INCOME":
                        transaction = new Transaction(sDate, cursor.getString(1), ExpenseType.INCOME, cursor.getDouble(4));
                        transactionsList.add(transaction);
                        break;
                }

            } while (cursor.moveToNext());
        }

        // return transactions list
        return transactionsList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionsList = new ArrayList<>();
        String query = "SELECT * FROM TABLE_TRANSACTIONS LIMIT" + limit;

        SQLiteDatabase sdb = dbh.getReadableDatabase();
        Cursor cursor = sdb.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Date sDate= null;
                try {
                    sDate = new SimpleDateFormat("dd/MM/yyyy").parse(cursor.getString(2));
                }catch (ParseException e){}

                switch (cursor.getString(3)) {
                    case "EXPENSE":
                        Transaction transaction = new Transaction(sDate, cursor.getString(1), ExpenseType.EXPENSE, cursor.getDouble(4));
                        transactionsList.add(transaction);
                        break;
                    case "INCOME":
                        transaction = new Transaction(sDate, cursor.getString(1), ExpenseType.INCOME, cursor.getDouble(4));
                        transactionsList.add(transaction);
                        break;
                }

            } while (cursor.moveToNext());
        }

        // return limited transactions list
        return transactionsList;
    }


}
