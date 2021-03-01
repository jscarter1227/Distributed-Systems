package chat;

import java.io.Serializable;

public class NodeInfo implements Serializable {
   String name;
   String ip;
   int port;
   //NodeInfo participantlist[];

   public NodeInfo( String input_ip, String input_name, int input_port)
   {
       ip = input_ip;
       name = input_name;
       port = input_port;
   }
   
   public String getAddress() {
	   return ip;
   }
   
   public int getPort() {
	   return port;
   }
   
   public String getName() {
	   return name;
   }
   
}
