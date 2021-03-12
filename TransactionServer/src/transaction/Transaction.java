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
	
	public int getID() {
		return ID;
	}
	
	public ArrayList<Lock> getLocks() {
		return locks;
	}
	
	public void addLock(Lock lock) {
		lock.add(lock);
	}

	public void log(String logString) {
		log.append(logString);
		
	}
	
	public StringBuffer getLog() {
		return log;
	}
}
