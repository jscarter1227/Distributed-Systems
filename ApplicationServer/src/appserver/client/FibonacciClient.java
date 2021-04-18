package appserver.client;

import appserver.comm.Message;
import appserver.comm.MessageTypes;
import appserver.job.Job;
import utils.PropertyHandler;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;


public class FibonacciClient extends Thread {
	

	private static final int JOB_REQUEST = 1;
	String host = null;
	int port = 0;
	int fibNum;
	
	public FibonacciClient(String props, int num) {
		try {
            Properties serverProps = new PropertyHandler(props);
            host = serverProps.getProperty("HOST");
            port = Integer.parseInt(serverProps.getProperty("PORT"));
            fibNum = num;
            System.out.println("[FibonacciClient.FibonacciClient] Host: " + host);
            System.out.println("[FibonacciClient.FibonacciClient] Port: " + port);
		} catch (IOException e) {
            System.err.println("[FibonacciClient] IOException when parsing server properties file");
            e.printStackTrace();
        }
	}
	
	public void run() {
        try { 
            // connect to application server
            Socket server = new Socket(host, port);
            
            // hard-coded string of class, aka tool name ... plus one argument
            String classString = "appserver.job.impl.Fibonacci";
            
            // create job and job request message
            Job job = new Job(classString, fibNum);
            Message message = new Message(JOB_REQUEST, job);
            
            // sending job out to the application server in a message
            ObjectOutputStream writeToNet = new ObjectOutputStream(server.getOutputStream());
            writeToNet.writeObject(message);
            
            // reading result back in from application server
            // for simplicity, the result is not encapsulated in a message
            ObjectInputStream readFromNet = new ObjectInputStream(server.getInputStream());
            Integer result = (Integer) readFromNet.readObject();
            System.out.println("Fibonacci of " + fibNum + ": " + result);
        } catch (Exception ex) {
            System.err.println("[FibonacciClient.run] Error occurred");
            ex.printStackTrace();
        }
	}
	
	public static void main(String[] args) {
		for(int i = 46; i > 0; i--) {
			(new FibonacciClient("../../config/Server.properties", i)).start();
		}
	}

}
