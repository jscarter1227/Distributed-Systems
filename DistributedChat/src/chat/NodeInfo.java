package chat;

import java.io.Serializable;

public class NodeInfo implements Serializable {
   String name;
   String ip;
   int port;
   //NodeInfo participantlist[];

   public NodeInfo( String input_ip, String input_name, int input_port)
   {
	   // Each node will have an IP, name, and port.
       ip = input_ip;
       name = input_name;
       port = input_port;
   }
   
   // gets IP from a node
   public String getAddress() {
	   return ip;
   }
   
   // gets port from a node
   public int getPort() {
	   return port;
   }
   
   // gets the name of a node.
   public String getName() {
	   return name;
   }
   
}
