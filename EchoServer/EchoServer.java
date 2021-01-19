import java.net.*;
import java.io.*;
import java.lang.Thread;

public class EchoServer 
{
    public static void main(String[] args) 
    {
        // Ensure port number is recieved from commandline
        if(args.length != 1)
        {
            System.out.println("Error, port number needed.");
            return;
        }

        // Converts commandline number into port
        int port = Integer.parseInt(args[0]);
        try
        {
            // Creates serversocket, prints once initialized
            ServerSocket echoServer = new ServerSocket(port);
            System.out.println("Server Initialized on Port: " + port);

            while(true)
            {
                // Creating new thread
                Socket clientSocket = echoServer.accept();
                EchoThread echoThread = new EchoThread(clientSocket);
                (new Thread( new EchoThread(clientSocket) )).start();
                
            }
        }
        catch(IOException e) 
        {
            // Prints if exception found
            System.out.println("Exception caught:");
            System.out.println(e.getMessage());
        }
    }
}