/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.ExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest {
    private ExpenseManager expenseManager;
    private DatabaseHelper dbHelper;

    @Before
    public void setup() throws ExpenseManagerException {
        Context context = ApplicationProvider.getApplicationContext();
        expenseManager = new PersistentExpenseManager(context);
        dbHelper = new DatabaseHelper(context);
    }

    @Test
    public void testAddAccount() {
        expenseManager.addAccount("123test", "ABC","HolderTest", 15000);
        List<String> accountHolders = expenseManager.getAccountNumbersList();
        assertTrue(accountHolders.contains("123test"));
    }


    @Test
    public void testLogTransaction() {

        Date date = Calendar.getInstance().getTime();
        Transaction transaction = new Transaction(date ,"123test", ExpenseType.EXPENSE, Double.parseDouble(new DecimalFormat("0.00").format(1200)));
        dbHelper.addTransaction(transaction);

        List<Transaction> transactions = dbHelper.getAllTransactionLogs();
        Boolean found = false;

        for (Transaction trans: transactions) {
            if(trans.getAccountNo().equals("123test")){
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

}