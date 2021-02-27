package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

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
			// Add node w/ IP
			// Print join message as well
		   ChatNode.nodeList.add(message.node);
	      System.out.println(message.node.getName() + ": has joined");
		   
	      try
	         {
	            writeToNet.writeObject(ChatNode.nodeList);
	         }
	      catch(IOException ex)
	         {
	            System.err.println("Message unable to read.");
	            System.exit(1);
	         }
	      
	      // update everyone elses lists
	      ChatNode.sender.update(message.node);
		}
		else if(message.type == MessageType.LEAVE) 
		{
			// Remove node w/ IP
			// Print left message as well
	      ChatNode.nodeList.remove(message.node);
         System.out.println(message.node.getName() + ": has left");

		}
		else if(message.type == MessageType.MESSAGE)
		{
			// Display chat
			System.out.println(message.message);
		}

	}

}
