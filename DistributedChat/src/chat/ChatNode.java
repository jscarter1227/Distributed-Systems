package chat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.*;
import java.util.*;
import java.util.logging.Level; 
import java.util.logging.Logger; 
import java.util.logging.*; 



public class ChatNode
{
	public static ArrayList<NodeInfo> nodeList;
    public static NodeInfo me = null;

    static Receiver receiver = null;
    static Sender sender = null;

    public ChatNode(String propertiesFile) throws IOException
    {
    	FileInputStream propReader = null;
    	Properties connectionInfo = null;
        int myPort = 0;
        String myName = null;
        String myIP = null; 

        //open properties file
        try
        {
        	propReader = new FileInputStream(propertiesFile);
        	connectionInfo = new Properties();
        	connectionInfo.load(propReader);
        }
        catch (IOException exception)
        {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, "Could not open properties file", exception);
            System.exit(1);
        }
        finally {
        	propReader.close();
        }

        // get node receiver port
        try 
        {
            myPort = Integer.parseInt(connectionInfo.getProperty("MY_PORT"));
        }
        catch (NumberFormatException exception)
        {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, "Could not read receiver port", exception);
            System.exit(1);
        }

        // get node receiver name
        myName = connectionInfo.getProperty("MY_NAME");
        if (myName == null)
        {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, "Could not read receiver name");
            System.exit(1);
        }

        // get node receiver port
        try 
        {
            myIP = (InetAddress.getLocalHost().getHostAddress());
        }
        catch (UnknownHostException exception)
        {
            Logger.getLogger(ChatNode.class.getName()).log(Level.SEVERE, "Could not find my IP", exception);
            System.exit(1);
        }

        me = new NodeInfo(myIP, myName, myPort);
    }

    void run()
    {
        //start receiver
        (receiver = new Receiver(me)).start();

        //start sender
        (sender = new Sender(me)).start();
    }

    public static void main(String[] args) throws IOException
    {
        String propsFile = null;
        try 
        {
            propsFile = args[0];
        }
        catch (ArrayIndexOutOfBoundsException exception)
        {
            propsFile = "C:\\Users\\nerd4\\Documents\\cs465\\New folder\\Distributed-Systems\\DistributedChat\\config\\ChatNodeDefault.properties";
        }

        (new ChatNode(propsFile)).run();
    }
}
