package lock;

import java.util.Enumeration;
import java.util.Hashtable;

public class LockManager {
	
	private Hashtable<K, V> theLocks;
	
	public void setLock(Object object, TransID trans, LockType lockType) {
		Lock foundLock;
		synchronized(this) {
			// find the lock associated with obj
			// if there isnt one, create it and add to the hash table
		}
		foundLocks.acquired(trans, lockType);
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
