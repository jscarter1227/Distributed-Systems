package account;

import java.util.ArrayList;

import transaction.TransactionServer;

public class AccountManager {
	ArrayList<Account> accountList;
	
	public AccountManager() {
		// TODO: Constructor
	}
	
	public int write(int accountNumber, Transaction transaction, int balance) {
		// get the account
		Account account = getAccount(accountNumber);
		
		// set the write lock
		(TransactionServer.lockManager).lock(account, transaction, WRITE_LOCK);
		
		// above call may wait or deadlock until it continues here
		account.setBalance(balance);
		return balance;
	}
	
	private Account getAccount(int accountNumber) {
		// TODO Look through list and return account that has number we are looking for
		return null;
	}

	public int read(int accountNumber, Transaction transaction) {
		// get account
		Account account = getAccount(accountNumber);
		
		(TransactionServer.lockManager).lock(account, transaction, READ_LOCK);
		
		return (getAccount(accountNumber)).getBalance);
	}
	

}
