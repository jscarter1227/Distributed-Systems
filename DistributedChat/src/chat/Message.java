
package chat;

import java.io.Serializable;
import java.util.*;

public class Message implements Serializable { 
	//public enum MessageType{JOIN, LEAVE, MESSAGE}
	final int JOIN = 1;
	final int LEAVE = 2;
	final int MESSAGE = 3;
	
	static int myType;
	static NodeInfo myNode = null;
	static String myMessage = null;
	
	
	public Message(int type, NodeInfo node)
	{
		myType = type;
		myNode = node;	
		myMessage = " ";
	}
	
	public Message(String message)
	{
		myType = 3;
		myNode = null;
		myMessage = new String(message);
	}
	
	public int getType()
	{
		return myType;
	}

	public NodeInfo getNode() {
		return myNode;
	}
	
	public String getMessage() {
		return myMessage;
	}
	
	
}