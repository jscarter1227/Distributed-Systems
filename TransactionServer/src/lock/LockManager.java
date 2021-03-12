package lock;

import java.util.Enumeration;
import java.util.Hashtable;
import account.Account;
import transaction.Transaction;

public class LockManager {
	
	private Hashtable<Account, Lock> locks;
	
	public LockManager() {
		locks = new Hashtable<>();
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
	
	public synchronized void unlock(TransID trans) {
		Enumeration<E> e = theLocks.elements();
		while(e.hasMoreElements()) {
			Lock aLock = (Lock)(e.nextElement());
			if(/* trans is a holder of this lock*/) {
				aLock.release(trans);
			}
		}
	}

}
