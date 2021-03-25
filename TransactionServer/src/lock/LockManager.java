package lock;

import java.util.Enumeration;
import java.util.*;
import account.Account;
import transaction.Transaction;

public class LockManager implements LockType{
	
	private HashMap<Account, Lock> locks;
	private static boolean toLock;
	
	public LockManager(boolean toLock) {
		locks = new HashMap<>();
		this.toLock = toLock;
	}
	
	public void setLock(Account account, Transaction trans, int lockType) {
		Lock foundLock;
		synchronized(this) {
			foundLock = locks.get(account);
			
			if(foundLock == null) {
				foundLock = new Lock(account);
				locks.put(account, foundLock);
			}
		}
		foundLock.acquire(trans, lockType);
	}
	
	public synchronized void unlock(Transaction trans) {
		Iterator<Lock> lockIterator = trans.getLocks().listIterator();
		Lock currentLock;
		
		while(lockIterator.hasNext()) {
			currentLock = lockIterator.next();
			currentLock.release(trans);
		}
	}

}
