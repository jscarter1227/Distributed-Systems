import java.net.*;
import java.io.*;

public class EchoThread implements Runnable
{
    Socket clientSocket;
    public EchoThread(Socket socket)
    {
        clientSocket = socket;
    }

    public void run() 
    {
        // To store client characters
        char charFromClient;

        try
        {
            // Create Reader/Writer to read clients input
            BufferedReader fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter toClient = new PrintWriter(clientSocket.getOutputStream(), true);

            while(true)
            {
                charFromClient = (char)fromClient.read();
                toClient.println(charFromClient);
                
                //TODO: Program exit
            }
        }
        catch (Exception e)
        {
            System.out.println("Exception caught:");
            System.out.println(e.getMessage());
        }
    }
}