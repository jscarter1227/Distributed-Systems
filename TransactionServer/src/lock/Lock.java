package lock;

import java.util.Vector;

public class Lock {
	
	private Object object;
	private Vector<E> holders;
	private LockType lockType;
	
	public synchronized void acquire(TransID trans, LockType aLocktype) {
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
