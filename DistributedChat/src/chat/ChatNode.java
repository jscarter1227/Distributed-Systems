package chat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

//otte made this by hand woof
import utils.PropertyHandler;

public class ChatNode
{
    // TODO: we need to make a participant list to share between sender and receiver
    public static NodeInfo me = null;

    static Receiver receiver = null;
    static Sender sender = null;

    public ChatNode(String propertiesFile)
    {
        Properties properties = null;
        int myPort = 0;
        String myName = null;
        String myIP = null; 

        //open properties file
        try
        {
           //TODO: this is from otte's code is correct and I assume this constructor needs to return Properties which is a java util
            properties = new PropertyHandler(propertiesFile);
        }
        catch (IOException exception)
        {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, "Could not open properties file", exception);
            System.exit(1);
        }

        // get node receiver port
        try 
        {
            myPort = Integer.parseInt(properties.getProperty("MY_PORT"));
        }
        catch (NumberFormatException exception)
        {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, "Could not read receiver port", exception);
            System.exit(1);
        }

        // get node reveiver name
        myName = properties.getProperty("MY_NAME");
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
            // lol otte included something that shouldn't happen
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
        (sender = new Sender()).start();
    }

    public static void main(String[] args)
    {
        String propsFile = null;

        try 
        {
            propsFile = args[0];
        }
        catch (ArrayIndexOutOfBoundsException exception)
        {
            propsFile = "../../config/ChatNodeDefault.properties";
        }

        (new ChatNode(propsFile)).run();
    }
}
