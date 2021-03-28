package lock;

import java.util.*;
import account.Account;
import transaction.Transaction;

public class Lock implements LockType{
	
	private Account account;
	private ArrayList<Transaction> locks;
	private HashMap<Transaction, Object[]> requestors;
	private int lockType;
	
	public Lock(Account acc) {
		locks = new ArrayList();
		requestors = new HashMap();
		account = acc;
		lockType = LockType.EMPTY_LOCK;	
	}
	
	public synchronized void acquire(Transaction trans, int incomingLockType) {
		trans.log("[Lock.acquire]                 | try " + getLockType(incomingLockType) + " on account #" + account.getAccountNum());
		
		// Check to see if there is a conflicting lock
		while(isConflicting(trans, incomingLockType)) {
			try {
				trans.log("[Lock.acquire]                 | ---> wait to set " + getLockType(incomingLockType) +  " on account #" + account.getAccountNum());
				addLockRequestor(trans, incomingLockType);
				wait();
				removeLockRequestor(trans);
			}
			catch (InterruptedException e) {
				System.out.println("\nwe missed it");
			}
		}
		
		// Ottes Psuedocode
		if(locks.isEmpty()) {
			locks.add(trans);
			lockType = incomingLockType;
			trans.addLock(this);
			trans.log("[Lock.acquire]                 | lock set to " + getLockType(incomingLockType) +  " on account #" + account.getAccountNum());
		}
		// Another transaction holds the lock
		else if(!locks.contains(trans)) {
			Iterator<Transaction> lockIterator = locks.iterator();
			Transaction otherTransaction;
			
			while(lockIterator.hasNext()) {
				otherTransaction = lockIterator.next();
			}
			
			// add transaction to list
			locks.add(trans);
			trans.addLock(this);
			trans.log("[Lock.acquire]                 | share " + getLockType(incomingLockType) +  " on account #" + account.getAccountNum() + ", with transaction(s)");
		}
		// This transaction is a holder and needs a more excluse lock, promote
		else if(locks.size() == 1 && lockType == READ_LOCK && incomingLockType == WRITE_LOCK ) {
			trans.log("[Lock.acquire]                 | promote " + getLockType(lockType) + " to " + getLockType(incomingLockType) +  " on account #" + account.getAccountNum());
			lockType = incomingLockType;
		}
		else {
			trans.log("[Lock.acquire]                 | ignore setting " + getLockType(lockType) + " to " + getLockType(incomingLockType) +  " on account #" + account.getAccountNum());
		}
	}
	
	private void addLockRequestor(Transaction trans, int incomingLockType) {
		int[] lockTypes = new int[2];
		lockTypes[0] = lockType;
		lockTypes[1] = incomingLockType;
		
		Iterator<Transaction> lockIterator = locks.iterator();
		Transaction otherTransaction;
		StringBuilder transactionString = new StringBuilder("");
		while(lockIterator.hasNext()) {
			otherTransaction = lockIterator.next();
			transactionString.append(" ").append(otherTransaction.getID());
		}
		
		Object[] lockInfo = new Object[2];
		lockInfo[0] = lockTypes;
		lockInfo[1] = transactionString.toString();
		
		requestors.put(trans, lockInfo);		
	}

	public synchronized void release(Transaction trans) {
		locks.remove(trans);
		
		if(locks.isEmpty()) {
			trans.log("[Lock.acquire]                 | release " + getLockType(lockType) + ",account #" + account.getAccountNum());
			lockType = LockType.EMPTY_LOCK;
		}
		
		notifyAll();
	}
	
	private boolean isConflicting(Transaction trans, int incomingLockType) {
		// short circuit logic to leave function if no locks exist
		if(locks.isEmpty()) {
			trans.log("[Lock.acquire]                 | current lock " + getLockType(incomingLockType) + 
					  " on account #" + account.getAccountNum() + " no holder, no conflict");
			return false;
		}
		// Current transaction only one in lockholders
		else if( locks.size() == 1 && locks.contains(trans)) {
			trans.log("[Lock.acquire]                 | current lock " + getLockType(incomingLockType) + 
					  " on account #" + account.getAccountNum() + " transaction is sole holder, no conflict");
			return false;
		}
		// Read on Read
		else if(lockType == READ_LOCK && incomingLockType == READ_LOCK ) {
			return false;
		}
		// Conflict Found
		else {
			trans.log("[Lock.acquire]                 | current lock " + getLockType(incomingLockType) + " held by transaction(s)");
			return true;
		}
	}
	
	private void removeLockRequestor(Transaction trans) {
		requestors.remove(trans);
	}
	
	public static String getLockType(int lockType) {
		if(lockType == WRITE_LOCK) {
			return "READ_LOCK";
		}
		else if(lockType == READ_LOCK) {
			return "WRITE_LOCK";					
		}
		return "EMPTY_LOCK";
	}
	
	public HashMap<Transaction, Object[]> getRequestors() {
		return requestors;
	}
	
	public Account getAccount() {
		return account;
	}
}
