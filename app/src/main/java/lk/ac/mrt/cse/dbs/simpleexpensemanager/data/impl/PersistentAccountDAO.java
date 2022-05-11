package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private final Map<String, Account> accounts;
    private final DatabaseHelper dbHelper;
    private final Context context;

    public PersistentAccountDAO(DatabaseHelper databaseHelper, Context context) {
        this.dbHelper = databaseHelper;
        this.context = context;
        this.accounts = dbHelper.getAllAccounts();
    }

    @Override
    public List<String> getAccountNumbersList() {
        return new ArrayList<>(accounts.keySet());
    }

    @Override
    public List<Account> getAccountsList() {
        return new ArrayList<>(accounts.values());
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return accounts.get(accountNo);
    }

    @Override
    public void addAccount(Account account) {
        if(dbHelper.addAccount(account)){
            accounts.put(account.getAccountNo(), account);
//            Toast.makeText(this.context,"Account added successfully", Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(this.context,"Error occur when adding account", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        if(dbHelper.removeAccount(accountNo)){
            accounts.remove(accountNo);
//            Toast.makeText(this.context,"Account removed successfully", Toast.LENGTH_SHORT).show();
        }else{
//            Toast.makeText(this.context,"Error occur when removing account", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        if (!accounts.containsKey(accountNo)) {
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
        Account account = accounts.get(accountNo);
        // specific implementation based on the transaction type
        switch (expenseType) {
            case EXPENSE:
                account.setBalance(account.getBalance() - amount);
                break;
            case INCOME:
                account.setBalance(account.getBalance() + amount);
                break;
        }
        if(dbHelper.updateBalance(account)){
            accounts.put(accountNo, account);
        }

    }
}
