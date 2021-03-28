package transaction;

import java.io.*;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import account.Account;
import account.AccountManager;
import lock.Lock;
import lock.LockManager;

public class TransactionServer extends Thread {
	public ServerSocket serverSocket;
	public static boolean transactionView = true;
	public static AccountManager accountManager = null;
	public static TransactionManager transactionManager = null;
	public static LockManager lockManager = null;

	public TransactionServer(String serverProps) {

		try(InputStream propsFile = new FileInputStream(serverProps)){
			// create a properties object then load the file
			int numAccounts = 0;
			int initBalance = 0;
			Properties props = new Properties();
			props.load(propsFile);

            // get the properties for each of the varables declared above
			transactionView = Boolean.valueOf(props.getProperty("TRANSACTION_VIEW"));
			TransactionServer.transactionManager = new TransactionManager();
			System.out.println("[TransactionServer.TransactionServer] TransactionManager created");

			boolean applyLock = Boolean.valueOf(props.getProperty("APPLY_LOCKING"));
			TransactionServer.lockManager = new LockManager(applyLock);
			System.out.println("[TransactionServer.TransactionServer] LockManager Created");

			numAccounts = Integer.parseInt(props.getProperty("NUMBER_ACCOUNTS"));
			initBalance = Integer.parseInt(props.getProperty("INITIAL_BALANCE"));

			TransactionServer.accountManager = new AccountManager(numAccounts, initBalance);
			System.out.println("[TransactionServer.TransactionServer] AccountManager created");

			try {
				serverSocket = new ServerSocket(Integer.parseInt(props.getProperty("PORT")));
				System.out.println("[TransactionServer.TransactionServer] ServerSocket created");
			}
			catch(IOException e) {
				System.err.println("[TransactionServer.TransactionServer] Error creating socket");
				System.exit(1);
			}
		}
		catch(Exception e) {
			System.err.println("[TransactionServer.TransactionServer] Error reading props file");
			System.exit(1);
		}
	}

	public void run() {
		while(true) {
			try {
				//System.out.println("Waiting for socket connection");
				transactionManager.startTransactionWorker(serverSocket.accept());
				//System.out.println("Server socket recieved");
			}
			catch(IOException e)
			{
				System.err.println("[TransactionServer.run] Error in accept");
			}
		}
	}
	public static void main(String[] args) throws FileNotFoundException{

		(new TransactionServer("C:\\Users\\Travis\\Documents\\Github\\Distributed-Systems\\TransactionServer\\bin\\props\\TransactionServer.properties")).start();

		try {
			sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("[TransactionServer] Error sleeping");
		}

		System.out.println("======================================= DEADLOCKED ACCOUNTS INFORMATION =======================================");
    	Lock lock;
    	Transaction trans;
    	HashMap<Account, Lock> locks = lockManager.getLocksList();
    	Iterator<Lock> lockIterator = locks.values().iterator();

    	while(lockIterator.hasNext()) {
    		lock = lockIterator.next();
    		HashMap<Transaction, Object[]> lockRequestors = lock.getRequestors();
    		if(!lockRequestors.isEmpty()) {
    			System.out.println("Account #" + lock.getAccount().getAccountNum() + "is involved in deadlock:");
    			Iterator<Transaction> lockedTransactions = lockRequestors.keySet().iterator();

    			while(lockedTransactions.hasNext()) {
    				trans = lockedTransactions.next();
    				Object[] lockInfo = lockRequestors.get(trans);
    				int[] lockTypes = (int[]) lockInfo[0];
    				String lockHolders = (String) lockInfo[1];

    				System.out.println("\tTransaction #" + trans.getID() + " trying to set " + Lock.getLockType(lockTypes[1]) + ", waiting for release of " + Lock.getLockType(lockTypes[0]) +
    									", held by transaction(s)" + lockHolders);
    			}
    		}
    	}

		System.out.println("===================================== DEADLOCKED TRANSACTIONS INFORMATION =====================================");
		// Print deadlockeded accounts
		 Transaction transaction;
	    ArrayList<Transaction> locked_transactions = transactionManager.getTransactions();
	    Iterator<Transaction> transactionIterator = locked_transactions.iterator();

	      while(transactionIterator.hasNext()) {
	         transaction = transactionIterator.next();

	         System.out.println("\n" + transaction.getLog());
	      }

		System.out.println("================================================ BRANCH TOTAL =================================================");
	     Account account;
	     int branchTotal = 0;
	       ArrayList<Account> accounts = accountManager.getAccounts();
	       Iterator<Account> accountIterator = accounts.iterator();

	         while(accountIterator.hasNext()) {
	            account = accountIterator.next();

	            branchTotal += account.getBalance();
	         }

		System.out.println("---> " + branchTotal);

		System.exit(1);

	}


}
