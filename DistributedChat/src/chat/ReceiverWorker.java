package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;


public class ReceiverWorker extends Thread {
	Socket connection = null;
    Socket peerConnection = null;
    ObjectInputStream readFromNet = null;
    ObjectOutputStream writeToNet = null;
    Message message = null;
    NodeInfo me = null;
    
	public ReceiverWorker(Socket socket)
	{
		//me = myNode;
		connection = socket;
		try
		{
			readFromNet = new ObjectInputStream(connection.getInputStream());
			writeToNet = new ObjectOutputStream(connection.getOutputStream());
		}
		catch(IOException ex)
		{
			System.err.println("Error with object stream..");
			System.exit(1);
		}
	}
	
	@Override
	public void run()
	{	   
		try
		{
			// Read message from input stream
			// Problem - why do multiple of the same objects send?
			message = (Message) readFromNet.readObject();
		}
		catch(ClassNotFoundException ex)
		{
			System.err.println("Error with converting readFromNet to object");
		} catch (IOException e) {
			System.err.println("IO Exception in recieverworker");
		}
						
		// Check type of message before operations
		if(message.getMessage().equals("JOIN"))
		{
			// Add node
			// Print join message as well
			ChatNode.nodeList.add(message.getNode());
			System.out.println(message.getNode().getName() + ": has joined");
			
			try
			{
				writeToNet.writeObject(ChatNode.nodeList);
			}
			catch(IOException ex)
			{
				System.err.println("Message unable to read. :(");
				System.exit(1);
			}

		}
		else if(message.getMessage().equals("LEAVE")) 
		{
		  // Remove node w/ IP
		  // Print left message as well
			ChatNode.nodeList.remove(message.getNode());
			System.out.println(message.getNode().getName() + ": has left");
		}
		else
		{
			// Display chat
			System.out.println(message.getMessage());
		}
		
		
	}

}
