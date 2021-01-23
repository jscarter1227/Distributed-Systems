import java.net.*;
import java.io.*;

public class EchoThread implements Runnable
{
    Socket clientSocket;
    public EchoThread(Socket socket)
    {
        clientSocket = socket;
    }

    //State is used to track the current progess towards completion of "QUIT" string
    enum State {
        DEFAULT,
        Q_RECIEVED,
        U_RECIEVED,
        I_RECIEVED,
        T_RECIEVED
    }

    public void run()
    {
        // To store client characters
        char charFromClient;
        State exitState = State.DEFAULT;

        try
        {
            // Create Reader/Writer to read clients input
            DataInputStream fromClient = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream toClient = new DataOutputStream(clientSocket.getOutputStream());

            // Exits loop once the final character in quit is recieved
            while(exitState != State.T_RECIEVED)
            {
                charFromClient = (char)fromClient.readByte();
                if(isValidChar(charFromClient))
                {
                    // Really primative state machine to see if correct sequence allows for exit
                    if(charFromClient == 'Q' || charFromClient == 'q')
                    {
                        exitState = State.Q_RECIEVED;
                    }
                    else if((charFromClient == 'U' || charFromClient == 'u') && exitState == State.Q_RECIEVED)
                    {
                        exitState = State.U_RECIEVED;
                    }
                    else if((charFromClient == 'I' || charFromClient == 'i') && exitState == State.U_RECIEVED)
                    {
                        exitState = State.I_RECIEVED;
                    }
                    else if((charFromClient == 'T' || charFromClient == 't') && exitState == State.I_RECIEVED)
                    {
                        // Final state, loop will now exit after recieving final state.
                        exitState = State.T_RECIEVED;
                        toClient.writeByte(charFromClient);
                        clientSocket.close();
                    }
                    else
                    {
                        // If next letter in the sequence is not achieved, reset to default
                        exitState = State.DEFAULT;
                    }
                    toClient.writeByte(charFromClient);
                }
                else
                {
                    // If character recieved is not valid, send back blank character.
                    // Will cause errors otherwise.
                    toClient.writeByte((char)' ');
                }
            }
            clientSocket.close();
            System.out.println("Client Connection Closed");
        }
        catch (Exception e)
        {
            System.out.println("Exception caught: " + e);
        }
    }

    private static boolean isValidChar(char testChar)
    {
        // Checks to see if the character passed in is a valid letter.
        return (testChar >= 'a' && testChar <= 'z') || (testChar >= 'A' && testChar <= 'Z');
    }
}
