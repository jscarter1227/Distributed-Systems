
package chat;

import java.io.Serializable;
import java.util.*;

public class Message implements Serializable{ 
	public enum MessageType{JOIN, LEAVE, MESSAGE}
	
	MessageType type = null;
	NodeInfo node = null;
	String message = null;
	
	public Message(MessageType type, NodeInfo node)
	{
		this.type = type;
		this.node = node;	
		this.message = " ";
	}
	
	public Message(String message)
	{
		this.type = MessageType.MESSAGE;
		this.node = null;
		this.message = new String(message);
	}
	
}