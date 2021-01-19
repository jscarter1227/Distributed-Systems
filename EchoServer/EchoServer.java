import java.net.*;
import java.io.*;
import java.lang.Thread;

public class EchoServer 
{
    public static void main(String[] args) 
    {
        if(args.length != 1)
        {
            System.out.println("Error, port number needed.");
            return;
        }

        int port = Integer.parseInt(args[0]);
        try
        {
            ServerSocket echoServer = new ServerSocket(port);
            System.out.println("Server Initialized on Port: " + port);

            while(true)
            {
                Socket clientSocket = echoServer.accept();
                EchoThread echoThread = new EchoThread(clientSocket);
                (new Thread( new EchoThread(clientSocket) )).start();
                
            }
        }
        catch(IOException e) 
        {
            System.out.println("Exception caught:");
            System.out.println(e.getMessage());
        }
    }
}