
package chat;

import java.util.*;

public class Message { 
	public enum MessageType{JOIN, LEAVE, MESSAGE}
	
	MessageType type;
	NodeInfo node;
	String message;
	
	public Message(MessageType type, NodeInfo node)
	{
		this.type = type;
		this.node = node;	
		this.message = "";
	}
	
	public Message(String message)
	{
		this.type = MessageType.MESSAGE;
		this.message = new String(message);
	}
	
}