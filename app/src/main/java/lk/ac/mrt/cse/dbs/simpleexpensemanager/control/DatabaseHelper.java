package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String ACCOUNT_TABLE = "accounts";
    public static final String COLUMN_ACCOUNT_NO = "accountNo";
    public static final String COLUMN_BANK_NAME = "bankName";
    public static final String COLUMN_ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String COLUMN_BALANCE = "balance";
    public static final String TRANSACTION_TABLE = "transactions";
    public static final String COLUMN_EXPENSE_TYPE = "expenseType";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_AMOUNT = "amount";
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public DatabaseHelper(@Nullable Context context) {
        super(context, "190050K.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createAccountTableStatement = "CREATE TABLE " + ACCOUNT_TABLE + "(" + COLUMN_ACCOUNT_NO + " TEXT PRIMARY KEY, " + COLUMN_BANK_NAME + " TEXT, " + COLUMN_ACCOUNT_HOLDER_NAME + " TEXT, " + COLUMN_BALANCE + " REAL )";
        String createTransactionTableStatement = "CREATE TABLE " + TRANSACTION_TABLE + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_DATE + " TEXT, " +COLUMN_ACCOUNT_NO+ " TEXT, " + COLUMN_EXPENSE_TYPE + " TEXT, " + COLUMN_AMOUNT + " REAL, FOREIGN KEY (" +COLUMN_ACCOUNT_NO+") REFERENCES "+ACCOUNT_TABLE+"("+COLUMN_ACCOUNT_NO+"))";

        sqLiteDatabase.execSQL(createAccountTableStatement);
        sqLiteDatabase.execSQL(createTransactionTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addTransaction(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        SimpleDateFormat formatter =new SimpleDateFormat("dd-MM-yyyy");

        cv.put(COLUMN_DATE, formatter.format(transaction.getDate()));
        cv.put(COLUMN_ACCOUNT_NO,transaction.getAccountNo());
        cv.put(COLUMN_EXPENSE_TYPE, String.valueOf(transaction.getExpenseType()));
        cv.put(COLUMN_AMOUNT, df.format(transaction.getAmount()));

        long insert = db.insert(TRANSACTION_TABLE, null, cv);

        if(insert==-1){
            return false;
        }else{
            return true;
        }
    }

    public boolean addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ACCOUNT_NO, account.getAccountNo());
        cv.put(COLUMN_BANK_NAME, account.getBankName());
        cv.put(COLUMN_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
        cv.put(COLUMN_BALANCE, df.format(account.getBalance()));

        long insert = db.insert(ACCOUNT_TABLE, null,cv);
        if(insert==-1){
            return false;
        }else {
            return true;
        }
    }

    public List<Transaction> getAllTransactionLogs(){
        List<Transaction> transactionsList = new ArrayList<>();

        String query = "SELECT * FROM "+TRANSACTION_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        SimpleDateFormat formatter =new SimpleDateFormat("dd-MM-yyyy");

        if(cursor.moveToFirst()){
            do {
                String stringDate = cursor.getString(1);
                Date date = null;
                try {
                    date = formatter.parse(stringDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String accountNo = cursor.getString(2);
                ExpenseType expenseType = ExpenseType.valueOf(cursor.getString(3));
                double amount = Double.parseDouble(df.format(cursor.getDouble(4)));
                Transaction newTransaction = new Transaction(date, accountNo, expenseType, amount);
                transactionsList.add(newTransaction);

            }while (cursor.moveToNext());
        }else{

        }

        cursor.close();
        db.close();

        return transactionsList;
    }


    public Map<String, Account> getAllAccounts(){
        Map<String, Account> accountsList = new HashMap<>();

        String query = "SELECT * FROM "+ACCOUNT_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do {
                String accountNo = cursor.getString(0);
                String bankName = cursor.getString(1);
                String accountHolderName = cursor.getString(2);
                Double balance = Double.valueOf(df.format(cursor.getDouble(3)));

                Account newAccount = new Account(accountNo, bankName, accountHolderName, balance);
                accountsList.put(accountNo, newAccount);

            }while(cursor.moveToNext());
        }else{

        }

        cursor.close();
        db.close();

        return accountsList;

    }

    public boolean removeAccount(String accountNo){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+ACCOUNT_TABLE+" WHERE "+COLUMN_ACCOUNT_NO+" = "+accountNo;
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }

    public boolean updateBalance(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+ACCOUNT_TABLE+" SET "+COLUMN_BALANCE+" = "+account.getBalance()+" WHERE "+COLUMN_ACCOUNT_NO+" = '"+account.getAccountNo()+"'";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            return true;
        }else {
            return false;
        }
    }
}
