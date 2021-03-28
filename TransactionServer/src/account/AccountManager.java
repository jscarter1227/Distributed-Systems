package account;

import java.util.ArrayList;

import transaction.Transaction;
import transaction.TransactionServer;
import lock.LockType;
import lock.Lock;

public class AccountManager {
	static ArrayList<Account> accountList;
	static int numAccounts;
	static int initialBalance;
	
	public AccountManager(int numberAccounts, int initBalance) {
		accountList = new ArrayList();
		numAccounts = numberAccounts;
		initialBalance = initBalance;
		
		for(int i = 0; i < numAccounts; i++) {
			accountList.add(new Account(i, initialBalance));
		}
		
	}
	
	public int write(int accountNumber, Transaction transaction, int balance) {
		// get the account
		Account account = getAccount(accountNumber);
		
		// set the write lock
		(TransactionServer.lockManager).setLock(account, transaction, LockType.WRITE_LOCK);
		
		// above call may wait or deadlock until it continues here
		account.setBalance(balance);
		return balance;
	}
	
	private Account getAccount(int accountNumber) {
		return accountList.get(accountNumber);
	}
	
   public ArrayList<Account> getAccounts() { 
      return accountList;
   }

	public int read(int accountNumber, Transaction transaction) {
		// get account
		Account account = getAccount(accountNumber);
		
		(TransactionServer.lockManager).setLock(account, transaction, LockType.READ_LOCK);
		
		return (getAccount(accountNumber)).getBalance();
	}
	

}
