package chat;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Receiver extends Thread implements Serializable {
	
  static ServerSocket receiverSocket = null;
  static String userName = null;

  public Receiver(NodeInfo myNodeAddress) {
    try
    {
      receiverSocket = new ServerSocket(myNodeAddress.getPort());
      System.out.println("[Receiver.Receiver] receiver socket created, listening on port " + myNodeAddress.getPort());
    }
    catch (IOException ex)
    {
    	Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, "Error!");
    }
  }


	@Override
	public void run() {
	  while (true) 
	  {
	    try
	    {
	      (new ReceiverWorker(receiverSocket.accept())).start();
	    }
	    catch(IOException e) 
	    {
	      System.out.println("[Receiver.run] Warning: Error accepting client");
	    }
	  }
	}
}
