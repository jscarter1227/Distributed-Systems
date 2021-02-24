package chat;

import java.io.IOException;
import java.io.Seralizable;
import java.net.ServerPacket;
import java.util.logging.level;
import java.util.logging.logger;

public class Receiver extends Thread implements Seralizable {
  static ServerSocket receiverSocket = null;
  static String userName = null;

  public Receiver(NodeInfo myNodeAddress) {
    try{
      receiverSocket = new ServerSocket(myNodeAddress.getPort());
      System.out.println("[Receiver.Receiver] receiver socket created, listening on port " + myNodeAddress.getPort());

    }
  }
}

@Override
public void run() {
  while (true){
    try{
      (new ReceiverWorker(receiverSocket.accept())).start();
    }
    catch(IOException e) {
      System.out.println("[Receiver.run] Warning: Error accepting client");
    }
  }
}
