package lock;

import java.util.Enumeration;
import java.util.*;
import account.Account;
import transaction.Transaction;

public class LockManager implements LockType{
	//initialize variables
	private HashMap<Account, Lock> locks;
	private static boolean toLock;
	
	public LockManager(boolean toLock) {
		locks = new HashMap<>();
		this.toLock = toLock;
	}
	

	public void setLock(Account account, Transaction trans, int lockType) {
		Lock foundLock;
		synchronized(this) {
			// get account of lock from foundlock
			foundLock = locks.get(account);
			// if lock not found, make a new one
			if(foundLock == null) {
				foundLock = new Lock(account);
				locks.put(account, foundLock);
			}
		}
		// acquire locks transaction and locktype
		foundLock.acquire(trans, lockType);
	}
	
	public synchronized void unlock(Transaction trans) {
		// iterate through locks
		Iterator<Lock> lockIterator = trans.getLocks().listIterator();
		Lock currentLock;
		// release them as it goes
		while(lockIterator.hasNext()) {
			currentLock = lockIterator.next();
			currentLock.release(trans);
		}
	}
	// get list of locks
	public HashMap<Account, Lock> getLocksList() {
		return locks;
	}

}
