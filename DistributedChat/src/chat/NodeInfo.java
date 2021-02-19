package chat;

public class NodeInfo {
   String name;
   String ip;
   int port;
   NodeInfo participantlist[];

   public NodeInfo( String input_ip, String input_name, int input_port)
   {
       ip = input_ip;
       name = input_name;
       port = input_port;
   }
}
