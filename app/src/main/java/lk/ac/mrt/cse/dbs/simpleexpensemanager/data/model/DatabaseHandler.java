package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "170494F";

    private static final String TABLE_ACCOUNTS = "TABLE_ACCOUNTS";
    private static final String ACCOUNT_NO = "ACCOUNT_NO";
    private static final String BANK_NAME = "BANK_NAME";
    private static final String HOLDER_NAME = "HOLDER_NAME";
    private static final String BALANCE = "BALANCE";

    private static final String TABLE_TRANSACTIONS = "TABLE_TRANSACTIONS";
    private static final String TRANSACTION_ID = "TRANSACTION_ID ";
    private static final String DATE = "DATE";
    private static final String EXPENSE_TYPE = "EXPENSE_TYPE";
    private static final String AMOUNT = "AMOUNT";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ACCOUNTS_TABLE = "CREATE TABLE " + TABLE_ACCOUNTS + "("
                + ACCOUNT_NO + " TEXT PRIMARY KEY," + BANK_NAME + " TEXT," + HOLDER_NAME + " TEXT,"
                + BALANCE + " REAL" + ")";
        db.execSQL(CREATE_ACCOUNTS_TABLE);

        String CREATE_TRANSACTIONS_TABLE = "CREATE TABLE " + TABLE_TRANSACTIONS + "("
                + TRANSACTION_ID  + " INTEGER PRIMARY KEY AUTOINCREMENT," + ACCOUNT_NO + " TEXT," + DATE + " TEXT,"
                + EXPENSE_TYPE + " TEXT," + AMOUNT + " REAL, FOREIGN KEY (" + ACCOUNT_NO + ") REFERENCES "
                + TABLE_ACCOUNTS + "(" + ACCOUNT_NO + "))";
        db.execSQL(CREATE_TRANSACTIONS_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);

        // Create tables again
        onCreate(db);
    }
}
