package account;

import java.util.ArrayList;

import transaction.Transaction;
import transaction.TransactionServer;
import lock.LockType;
import lock.Lock;

public class AccountManager {
	ArrayList<Account> accountList;
	
	public AccountManager(int numberAccounts, int initialBalance) {
		accountList = new ArrayList();
		
	}
	
	public int write(int accountNumber, Transaction transaction, int balance) {
		// get the account
		Account account = getAccount(accountNumber);
		
		// set the write lock
		(TransactionServer.lockManager).lock(account, transaction, LockType.WRITE_LOCK);
		
		// above call may wait or deadlock until it continues here
		account.setBalance(balance);
		return balance;
	}
	
	private Account getAccount(int accountNumber) {
		return accountList.get(accountNumber);
	}

	public int read(int accountNumber, Transaction transaction) {
		// get account
		Account account = getAccount(accountNumber);
		
		(TransactionServer.lockManager).lock(account, transaction, LockType.READ_LOCK);
		
		return (getAccount(accountNumber)).getBalance();
	}
	

}
