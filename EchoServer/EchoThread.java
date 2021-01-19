import java.net.Socket;
import java.io.*;

public class EchoThread implements Runnable
{   
    // socket object
    Socket clientSocket;

    // will create the individual thread for a client
    public EchoThread(Socket socket)
    {
        clientSocket = socket;
    }
    
    public void run() 
    {   
        // holds the char that was received by the client
        char charFromClient;

        try
        {
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);
            
            while(true)
            {   
                // takes a char from the client so it can be sent back
                charFromClient = (char)fromClient.read();
                // sends the char that the client sent back to the client
                toClient.println(charFromClient);
            }
        }

        // if there is any kind of error it will print out the appropriate error message
        catch (Exception e)
        {
            System.out.println("Exception caught:");
            System.out.println(e.getMessage());
        }
    }
}
