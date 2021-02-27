package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

public class ReceiverWorker extends Thread {
	Socket connection = null;
    Socket peerConnection = null;
    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;
    Object message = null;
    
	public ReceiverWorker(Socket socket)
	{
		connection = socket;
		try
		{
			readFromNet = new ObjectInputStream(connection.getInputStream());
			writeToNet = new ObjectOutputStream(connection.getOutputStream());
		}
		catch(IOException ex)
		{
			System.err.println("Message unable to read.");
			System.exit(0);
		}
	}
	
	public void run()
	{
		try
		{
			message = (Message)readFromNet.readObject();
		}
		catch(Exception ex)
		{
			System.err.println("Message unable to read.");
		}
		
		
	}

}
