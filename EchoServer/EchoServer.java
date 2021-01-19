import java.net.*;
import java.io.*;
import java.lang.Thread;

public class EchoServer 
{
    public static void main(String[] args) 
    {
        // need to check if a port number is provided
        if(args.length != 1)
        {
            System.out.println("Error, port number needed.");
            return;
        }
         
        int port = Integer.parseInt(args[0]);

        // if no problems, the server should work as expected
        try
        {   
            // creates an echoServer object that will be listening on this port
            ServerSocket echoServer = new ServerSocket(port);
            System.out.println("Server Initialized on Port: " + port);
            
            // creates the threads so multiple clients can connect to server
            while(true)
            {
                Socket clientSocket = echoServer.accept();
                EchoThread echoThread = new EchoThread(clientSocket);
                (new Thread( new EchoThread(clientSocket) )).start();
                
            }
        }

        // if there is any kind of error it will print out the appropriate error message
        catch(IOException e) 
        {
            System.out.println("Exception caught:");
            System.out.println(e.getMessage());
        }
    }
}
