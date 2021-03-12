package lock;

import java.util.ArrayList;
import java.util.Vector;
import account.Account;
import transaction.Transaction;

public class Lock {
	
	private Account account;
	private ArrayList<Transaction> locks;
	private int lockType;
	
	public Lock(Account acc) {
		locks = new ArrayList();
		account = acc;
		lockType = LockType.EMPTY_LOCK;	
	}
	
	public synchronized void acquire(Transaction trans, int locktype) {
		while(/* TODO: Another transaction holds the lock in the conflicting mode*/) {
			try {
				wait();
			} catch (InterruptedException e) {
				
			}
		}
		if(holders.isEmpty()) {
			holders.addElement(trans);
			lockType = aLockType;
		} else if (/*another transaction holds the lock, share it*/) {
			if(/*this transaction not a holder*/) {
				holders.addElement(trans);
			}
		} else if(/* this transaction is a holder but needs a more exclusive lock*/) {
			lockType.promote();
		}
	}
	
	public synchronized void release(TransID trans) {
		holders.removeElement(trans);
		// set locktype to none
		notifyAll();
	}

}
