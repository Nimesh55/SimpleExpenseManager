package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager {
    private transient DatabaseHelper databaseHelper;
    private transient Context context;

    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.databaseHelper = new DatabaseHelper(context);
        this.context = context;
        this.setup();
    }

    @Override
    public void setup() throws ExpenseManagerException {

        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(this.databaseHelper,this.context);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(this.databaseHelper, this.context);
        setAccountsDAO(persistentAccountDAO);

    }
}
