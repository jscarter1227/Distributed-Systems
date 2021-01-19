import java.net.Socket;
import java.net.ServerSocket;

public class EchoServer 
{
    public static void main(String[] args) 
    {
        EchoServer echoServer = new ServerSocket(port);

        while(true)
        {
            (new Thread( new EchoThread(echoServer.accept()) )).start()
        }
    }
}