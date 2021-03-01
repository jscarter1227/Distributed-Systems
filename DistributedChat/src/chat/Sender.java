package chat;
import java.io.*;
import java.lang.System.*;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level; 
import java.util.logging.Logger;


import java.util.logging.*; 

public class Sender extends Thread {
    String inputLine = null;
    Scanner userInput;
    int myPort = 0;
    String myIP =  null;
    NodeInfo myNode;
    boolean hasJoined;

    public Sender(NodeInfo me)
    {
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
        	inputLine = null;
        	userInput = new Scanner(System.in);
            inputLine = userInput.nextLine();

            if(inputLine.startsWith("JOIN"))
            {
                String[] connectivityInfo = inputLine.split(" ");
                String joinAddress = null;
                int joinPort = 0;

                try
                {
                    joinAddress = connectivityInfo[1];
                    joinPort = Integer.parseInt(connectivityInfo[2]);
                }
                catch(ArrayIndexOutOfBoundsException ex)
                {
                	System.out.println("ArrayIndexOutOfBoundsException");
                }
                
            	try 
            	{
	            	Socket socket = new Socket(joinAddress, joinPort);
	            	toReceiver = new ObjectOutputStream(socket.getOutputStream());
	            	fromReceiver = new ObjectInputStream(socket.getInputStream());
	            	toReceiver.flush();
	            	// create join message

	            	Message message = new Message(1, myNode);
	            	// send message
	            	toReceiver.writeObject(message);
	            	toReceiver.flush();
	            	
	            	// retrieve list
	            	ArrayList<NodeInfo> receivedList = (ArrayList<NodeInfo>)fromReceiver.readObject();
	            	ChatNode.nodeList.addAll(receivedList);           	
	            	System.out.println("Joined chat.");
	            	
	            	toReceiver.close();
	            	fromReceiver.close();
	            	socket.close();
            	}
            	catch(IOException e) {
            		System.err.println("Failed to join chat.");
            	} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
            }
            else if(inputLine.startsWith("LEAVE") || inputLine.startsWith("SHUTDOWN"))
            {
            	// create leave message	
            	Message message = new Message(2, myNode);
            	
            	for(NodeInfo node : ChatNode.nodeList) {
        			try
        			{
        				Socket socket = new Socket(node.getAddress(), node.getPort());
        				toReceiver = new ObjectOutputStream( socket.getOutputStream() );
						
						// send leave
						toReceiver.writeObject(message);
						toReceiver.flush();
						toReceiver.close();
						socket.close();
        			}
        			catch(IOException e) 
        			{
        				System.err.println("Something broke :/");
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
            		if(myNode.ip != node.ip)
            		{
	        			try
	        			{
	        				Socket socket = new Socket(node.getAddress(), node.getPort());
	        				toReceiver = new ObjectOutputStream( socket.getOutputStream() );
							// send leave
							toReceiver.writeObject(message);
							toReceiver.flush();
							toReceiver.close();
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