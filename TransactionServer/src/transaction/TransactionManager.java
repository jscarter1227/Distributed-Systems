package transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import messages.Message;
import messages.msgTypes;
import lock.*;

public class TransactionManager implements msgTypes {

	private static int numTransactions;
	private static ArrayList<Transaction> transactionList;
	
	public TransactionManager() {
		numTransactions = 0;
		transactionList = new ArrayList();
	}
	
	public ArrayList<Transaction> getTransactions() { 
		return transactionList;
	}
	
	public void startTransactionWorker(Socket socket) {
		(new TransactionManagerWorker(socket)).start();
	}
	
	public class TransactionManagerWorker extends Thread {
		Socket client = null;
		ObjectInputStream readFromNet = null;
		ObjectOutputStream writeToNet = null;
		Message message = null;
		boolean running = true;
		
		Transaction transaction = null;
		int accountNum = 0;
		int accountBalance = 0;
		
		private TransactionManagerWorker(Socket socket) {
			client = socket;
			
			try {
				readFromNet = new ObjectInputStream(client.getInputStream());
				writeToNet = new ObjectOutputStream(client.getOutputStream());
			}
			catch(IOException e) {
				System.out.println("ERROR OPENING STREAMS");
				System.exit(1);
			}
		}
		
		public void run() {
			while(running) {
				try {
					message = (Message) readFromNet.readObject();
				}
				catch(IOException e) {
					System.out.println("[TransactionManager] IOException receiving message");
					System.exit(1);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				switch(message.getType()) {
					case OPEN_TRANSACTION:
						synchronized(transactionList) {
							transaction = new Transaction(numTransactions++);
							transactionList.add(transaction);
						}
						
						try {
							writeToNet.writeObject(transaction.getID());
						}
						catch(IOException e) {
							System.err.println("[TransactionManagerWorker.run] ERROR: In Open Transaction");
						}
						
						transaction.log("[TransactionManagerWorker.run] OPEN_TRANSACTION #" + transaction.getID());
						break;
						
					case CLOSE_TRANSACTION:
						TransactionServer.lockManager.unlock(transaction);
						transactionList.remove(transaction);
						
						// Close all open sockets, ect
						try {
							running = false;
							readFromNet.close();
							writeToNet.close();
							client.close();
						}
						catch(IOException e) {
							System.out.println("[TransactionManagerWorker.run] Error upon close");
						}
						
						transaction.log("[TransactionManagerWorker.run] CLOSE_TRANSACTION #" + transaction.getID());
						
						if(TransactionServer.transactionView) {
							System.out.println(transaction.getLog());
						}
						break;
					case READ_REQUEST:
						accountNum = message.accNumber;
						transaction.log("[TransactionManagerWorker.run] READ_REQUEST >>>>>>>>>>>>>>>>>>>> account #" + accountNum);
						accountBalance = TransactionServer.accountManager.read(accountNum, transaction);
						
						try {
							writeToNet.writeObject((Integer) accountBalance); 
						}
						catch(IOException e) {
							System.err.println("[TransactionManagerWorker.run] READ_REQUEST - Error writing balance");
						}
						
						transaction.log("[TransactionManagerWorker.run] READ_REQUEST <<<<<<<<<<<<<<<<<<<< account #"
										+ accountNum + ", balance $" + "accountBalance");
						break;
					case WRITE_REQUEST:
						Object[] content = (Object[]) message.getAccountInfo();
						accountNum = (Integer) content[0];
						accountBalance = (Integer) content[1];
						
						// These variables populated (sometimes) (Tested)
						transaction.log("[TransactionManagerWorker.run] WRITE_REQUEST >>>>>>>>>>>>>>>>>>>> account #"
										+ accountNum + ", new balance $" + accountBalance);	
						accountBalance = TransactionServer.accountManager.write(accountNum, transaction, accountBalance);

						try {
							writeToNet.writeObject((Integer) accountBalance);
						}
						catch(IOException e) {
							System.err.println("[TransactionManagerWorker.run] Error writing to net");
						}
						transaction.log("[TransactionManagerWorker.run] WRITE_REQUEST <<<<<<<<<<<<<<<<<<<< account #"
										+ accountNum + ", new balance $" + accountBalance);
						break;
					default:
						System.err.println("Stop, criminal scum. You violated the law.");
						System.err.println("Your stolen goods are now forfeit.");
						System.err.println("Pay the court a fine or serve your sentence");				
				}
			}
		}
	}
}
