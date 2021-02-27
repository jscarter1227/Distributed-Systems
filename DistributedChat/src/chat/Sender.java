package chat;
import java.io.*;
import java.lang.System.*;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level; 
import java.util.logging.Logger;

import chat.Message.MessageType;

import java.util.logging.*; 

public class Sender extends Thread {
    Scanner userInput = new Scanner(System.in);
    String inputLine = null;
    int myPort = 0;
    String myIP =  null;
    NodeInfo myNode;
    boolean hasJoined;

    public Sender(NodeInfo me)
    {
        userInput = new Scanner(System.in);
        hasJoined = false;
        myPort = me.getPort();
        myIP = me.getAddress();
        myNode = me;
    }

    @Override
    public void run() 
    {
        ObjectOutputStream toReceiver;
        ObjectInputStream fromReceiver;

        while(true)
        {
            inputLine = userInput.nextLine();

            if(inputLine.startsWith("JOIN"))
            {
            	try 
            	{
	            	Socket socket = new Socket(myIP, myPort);
	            	toReceiver = new ObjectOutputStream(socket.getOutputStream());
	            	fromReceiver = new ObjectInputStream(socket.getInputStream());
	            	
	            	// create join message
	            	Message message = new Message(MessageType.JOIN, myNode);
	            	
	            	// send message
	            	toReceiver.writeObject(message);
	            	
	            	ArrayList receivedMessage = (ArrayList)fromReceiver.readObject();
	            	
	            	ChatNode.nodeList = receivedMessage;
	            	ChatNode.nodeList.add(myNode);
	            	// TODO: Tell everyone to update their ArrayList w/ this Node
	            	
	            	System.out.println("Joined chat.");
	            	socket.close();
            	}
            	catch(IOException e) {
            		System.err.println("Failed to join chat.");
            	} 
            	catch (ClassNotFoundException e) 
            	{
            		System.err.println("Failed to join chat.");
				}
            }
            else if(inputLine.startsWith("LEAVE"))
            {
            	// create leave message	
            	Message message = new Message(MessageType.LEAVE, myNode);
            	
            	for(NodeInfo node : ChatNode.nodeList) {
            		if(myIP != node.ip) {
            			try
            			{
            				Socket socket = new Socket(myIP, myPort);
            				toReceiver = new ObjectOutputStream( socket.getOutputStream() );
    						fromReceiver = new ObjectInputStream( socket.getInputStream() );
    						
    						// send leave
    						toReceiver.writeObject(message);
    						
    						socket.close();
            			}
            			catch(IOException e) 
            			{
            				System.err.println("Something broke :/");
            			}
            		}
            	}
            	
            	System.out.println("Disconnected.");
                 	
            }
            else
            {
            	// Create message
            	String messageBuilder = myNode.name + ": " + inputLine;
				Message message = new Message(messageBuilder);
				
            	for(NodeInfo node : ChatNode.nodeList) {
            		if(myIP != node.ip) {
            			try
            			{
            				Socket socket = new Socket(myIP, myPort);
            				toReceiver = new ObjectOutputStream( socket.getOutputStream() );
    						fromReceiver = new ObjectInputStream( socket.getInputStream() );
    						
    						// send leave
    						toReceiver.writeObject(message);
    						
    						socket.close();
            			}
            			catch(IOException e) 
            			{
            				System.err.println("Something broke :/");
            			}
            		}
            	}
            	System.out.println(myNode.name + ": " + inputLine);
            }
        }
    }
}