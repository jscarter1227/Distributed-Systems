package transaction;

import java.io.IOException;
import java.util.Properties;

import chat.ChatNode;

public class TransactionClient extends Thread{
	
	public TransactionClient(Properties props) throws IOException {
		int port = 0;
		String IP = null;
		
		port = Integer.parseInt(props.getProperty("PORT"));
		IP = props.getProperty("IP");
		
	}
	
	public void run() {
		TransactionServerProxy transaction = new TransactionServerProxy(IP, port);
	}
	
	public static void main(String[] args) throws IOException {

        // We should probably implement a file reader at some point
		// Hard coding for now
		Properties props = new Properties();
		props.setProperty("IP", "localhost");
		props.setProperty("PORT", "10000");

        (new TransactionClient(props)).run();
	}

}
