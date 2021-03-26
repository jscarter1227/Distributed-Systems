package transaction;

import java.io.*;
import java.net.ServerSocket;
import java.util.Properties;

import account.AccountManager;
import lock.LockManager;

public class TransactionServer extends Thread {
	public ServerSocket serverSocket;
	public static boolean transactionView = true;
	public static AccountManager accountManager = null;
	public static TransactionManager transactionManager = null;
	public static LockManager lockManager = null;
	
	public TransactionServer(String serverProps) {
		
		try(InputStream propsFile = new FileInputStream(serverProps)){
			// create a properties object then load the file
			int numAccounts = 0;
			int initBalance = 0;
			Properties props = new Properties();
			props.load(propsFile);

            // get the properties for each of the varables declared above
			transactionView = Boolean.valueOf(props.getProperty("TRANSACTION_VIEW"));
			TransactionServer.transactionManager = new TransactionManager();
			System.out.println("[TransactionServer.TransactionServer] TransactionManager created");
			
			boolean applyLock = Boolean.valueOf(props.getProperty("APPLY_LOCKING"));
			TransactionServer.lockManager = new LockManager(applyLock);
			System.out.println("[TransactionServer.TransactionServer] LockManager Created");
			
			numAccounts = Integer.parseInt(props.getProperty("NUMBER_ACCOUNTS"));
			initBalance = Integer.parseInt(props.getProperty("INITIAL_BALANCE"));
			
			TransactionServer.accountManager = new AccountManager(numAccounts, initBalance);
			System.out.println("[TransactionServer.TransactionServer] AccountManager created");
			
			try {
				serverSocket = new ServerSocket(Integer.parseInt(props.getProperty("PORT")));
				System.out.println("[TransactionServer.TransactionServer] ServerSocket created");
			}
			catch(IOException e) {
				System.err.println("[TransactionServer.TransactionServer] Error creating socket");
				System.exit(1);
			}
		}
		catch(Exception e) {
			System.err.println("[TransactionServer.TransactionServer] Error reading props file");
			System.exit(1);
		}
	}
	
	public void run() {
		while(true) {
			try {
				//System.out.println("Waiting for socket connection");
				transactionManager.startTransactionWorker(serverSocket.accept());
				//System.out.println("Server socket recieved");
			}
			catch(IOException e)
			{
				System.err.println("[TransactionServer.run] Error in accept");
			}
		}
	}
	public static void main(String[] args) throws FileNotFoundException{
		
		(new TransactionServer("C:\\Users\\nerd4\\Documents\\cs465\\New folder\\Distributed-Systems\\TransactionServer\\bin\\props\\TransactionServer.properties")).start();
		
		try {
			sleep(10000);
		} catch (InterruptedException e) {
			System.out.println("[TransactionServer] Error sleeping");
		}
		
		System.out.println("======================================= DEADLOCKED ACCOUNTS INFORMATION =======================================");
		//TODO: Print Deadlocked Accounts
		System.out.println("===================================== DEADLOCKED TRANSACTIONS INFORMATION =====================================");
		//TODO: Print deadlockeded accounts
		System.out.println("================================================ BRANCH TOTAL =================================================");
		int branchTotal = 20;
		System.out.println("---> " + branchTotal);
		
		System.exit(1);
		
	}
	

}
