package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private final List<Transaction> transactions;
    private final DatabaseHelper dbHelper;
    private final Context context;

    public PersistentTransactionDAO(DatabaseHelper databaseHelper, Context context) {
        this.dbHelper = databaseHelper;
        this.context = context;
        this.transactions = this.dbHelper.getAllTransactionLogs();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, Double.parseDouble(new DecimalFormat("0.00").format(amount)));

        if(dbHelper.addTransaction(transaction)){
            transactions.add(transaction);
            Toast.makeText(this.context,"Transaction added successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this.context,"Error occur when adding transaction", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return dbHelper.getAllTransactionLogs();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        int size = transactions.size();
        if (size <= limit) {
            return transactions;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactions.subList(size - limit, size);
    }
}
