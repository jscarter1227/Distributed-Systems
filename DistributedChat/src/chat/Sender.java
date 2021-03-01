package chat;
import java.io.*;
import java.lang.System.*;
import java.net.Socket;
import java.util.*;
import java.util.logging.Level; 
import java.util.logging.Logger;


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
	            	System.out.println(myNode.getName());
	            	Message message = new Message(1, myNode);
	            	// send message
	            	toReceiver.writeObject(message);
	            	toReceiver.flush();
	            	ArrayList receivedList = (ArrayList)fromReceiver.readObject();
	            	ChatNode.nodeList = receivedList;
//	            	// TODO: Tell everyone to update their ArrayList w/ this Node
	            	
	            	System.out.println("Joined chat.");
	            	socket.close();
            	}
            	catch(IOException e) {
            		System.err.println("Failed to join chat.");
            	} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

            }
            else if(inputLine.startsWith("LEAVE"))
            {
            	// create leave message	
            	Message message = new Message(2, myNode);
            	
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
            	//System.out.println(myNode.name + ": " + inputLine);
            }
        }
    }

   public void update(NodeInfo newNode)
      {
         ObjectOutputStream toReceiver;
         ObjectInputStream fromReceiver;
         Message message = new Message(1, newNode);
         
         // TODO Auto-generated method stub
         for(NodeInfo node : ChatNode.nodeList) {
            if(myIP != node.ip) {
               try
               {
                  Socket socket = new Socket(node.getAddress(), node.getPort());
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
      }
}