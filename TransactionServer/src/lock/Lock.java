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
		
		// Check to see if there is a conflicting lock
		while(isConflicting(trans, incomingLockType)) {
			try {
				addLockRequestor(trans, incomingLockType);
				wait();
				removeLockRequestor(trans);
			}
			catch (InterruptedException e) {
				
			}
		}
		
		// Ottes Psuedocode
		if(locks.isEmpty()) {
			locks.add(trans);
			lockType = incomingLockType;
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
		}
		// This transaction is a holder and needs a more excluse lock, promote
		else if(locks.size() == 1 && lockType == READ_LOCK && incomingLockType == WRITE_LOCK ) {
			lockType = incomingLockType;
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
			lockType = LockType.EMPTY_LOCK;
		}
		
		notifyAll();
	}
	
	private boolean isConflicting(Transaction trans, int incomingLockType) {
		// short circuit logic to leave function if no locks exist
		if(locks.isEmpty()) {
			return false;
		}
		// Current transaction only one in lockholders
		else if( locks.size() == 1 && locks.contains(trans)) {
			return false;
		}
		// Read on Read
		else if(lockType == READ_LOCK && incomingLockType == READ_LOCK ) {
			return false;
		}
		// Conflict Found
		else {
			return true;
		}
	}
	
	private void removeLockRequestor(Transaction trans) {
		requestors.remove(trans);
	}
}
