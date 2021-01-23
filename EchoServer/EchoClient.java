import java.net.*;
import java.io.*;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) throws IOException {

        //verify correct number of arguments
        if(args.length != 2) {

            //if incorrect args count, give error and exit.
            System.err.println( "EchoServer needs host name and port number");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try
        {
            Socket echoSocket = new Socket(host, port);
            DataOutputStream out = new DataOutputStream(echoSocket.getOutputStream());
            DataInputStream in = new DataInputStream(echoSocket.getInputStream());
            Scanner input = new Scanner(System.in);

            // While quit command has not been sent,
            while(true)
            {
                // Converts scanner object into string for the loop.
                char recieved;
                String currToken = input.nextLine();
                int tokenLen = currToken.length();

                for(int i = 0; i < tokenLen; i++)
                {
                    // send input to server, display echo output. 
                    out.writeByte((byte)currToken.charAt(i));
                    recieved = (char)in.readByte();
                    System.out.println("Echoed Character: " + recieved );
                }
            }
        }

        //Catch exceptions in IO, notify that connection closed as a result.
        catch (IOException e)
        {
            System.err.println("Connection Closed");
        }
    }
}
