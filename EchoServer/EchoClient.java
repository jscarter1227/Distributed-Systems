import java.net.*;
import java.io.*;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
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
            while(true)
            {
                System.out.print("Input Character: ");
                out.writeByte((byte)input.next().charAt(0));
                System.out.println("Echoed Character: " + (char)in.readByte() + "\n");
            }
        }
        catch (IOException e) 
        {
            System.err.println("Connection Closed");
        }
    }
}
