
package chat;

import java.io.Serializable;
import java.util.*;

public class Message implements Serializable { 
	
	static NodeInfo myNode = null;
	static String myMessage = null;
	
	
	public Message(String msg, NodeInfo node)
	{
		myNode = node;	
		myMessage = msg;
	}
	
	
	public NodeInfo getNode() {
		return myNode;
	}
	
	public String getMessage() {
		return myMessage;
	}
	
	
}