package transaction;

import java.util.ArrayList;
import lock.Lock;

public class Transaction {
	int ID;
	ArrayList<Lock> locks = null;
	StringBuffer log = new StringBuffer("");
	
	public Transaction(int incID) {
		ID = incID;
		locks = new ArrayList();
	}
	
	//get ID of transaction
	public int getID() {
		return ID;
	}
	
	//get locks arrayList
	public ArrayList<Lock> getLocks() {
		return locks;
	}
	
	// add lock to arrayList of locks
	public void addLock(Lock lock) {
		locks.add(lock);
	}
	//add to transaction's log
	public void log(String logString) {
		log.append("\n" + logString);
		
	}
	//get log of transaction
	public StringBuffer getLog() {
		return log;
	}
}
