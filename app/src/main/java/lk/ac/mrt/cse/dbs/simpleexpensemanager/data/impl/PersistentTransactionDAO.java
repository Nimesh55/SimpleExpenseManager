package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.widget.Toast;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.ui.MainActivity;

public class PersistentTransactionDAO implements TransactionDAO {
    private final List<Transaction> transactions;
    private final DatabaseHelper dbHelper;

    public PersistentTransactionDAO(DatabaseHelper databaseHelper) {
        this.dbHelper = databaseHelper;
        this.transactions = this.dbHelper.getAllTransactionLogs();
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);

        boolean success = dbHelper.addTransaction(transaction);

        if(success){
            transactions.add(transaction);
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
