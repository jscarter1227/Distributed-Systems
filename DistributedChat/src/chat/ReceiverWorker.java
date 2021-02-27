package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

import chat.Message.MessageType;

public class ReceiverWorker extends Thread {
	Socket connection = null;
    Socket peerConnection = null;
    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;
    Message message = null;
    NodeInfo me = null;
    
	public ReceiverWorker(Socket socket, NodeInfo myNode)
	{
		me = myNode;
		connection = socket;
		try
		{
			readFromNet = new ObjectInputStream(connection.getInputStream());
			writeToNet = new ObjectOutputStream(connection.getOutputStream());
		}
		catch(IOException ex)
		{
			System.err.println("Message unable to read.");
			System.exit(1);
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
		
		if(message.type == MessageType.JOIN)
		{
			// TODO: Add node w/ IP
			// Print join message as well
		}
		else if(message.type == MessageType.LEAVE) 
		{
			// TODO: Remove node w/ IP
			// Print left message as well
		}
		else if(message.type == MessageType.MESSAGE)
		{
			// TODO: Display chat
			System.out.println(message.message);
		}

	}

}
