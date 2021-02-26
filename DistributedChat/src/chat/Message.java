package chat;

public class Message {

}
package message;

import com.company.NodeInfo;

public class JoinMessage extends Message {
    final public String destinationIp;
    final public int destinationPort;

    public JoinMessage(MessageType messageType, NodeInfo source, String destinationIp, int destinationPort) {
        super(messageType, source);
        this.destinationIp = destinationIp;
        this.destinationPort = destinationPort;
    }
}

package message;

public enum MessageType {
    JOIN,
    LEAVE,
    CHAT
}
package message;

import com.company.NodeInfo;

import java.io.Serializable;

public class Message implements Serializable {
    public final MessageType messageType;
    public final NodeInfo source;
    public Message(MessageType messageType, NodeInfo source) {
        this.messageType = messageType;
        this.source = source;
    }
}

package message;

import com.company.NodeInfo;

public class ChatMessage extends Message {
    public final String payload;

    public ChatMessage(MessageType messageType, NodeInfo source, String payload) {
        super(messageType, source);
        this.payload = payload;
    }
}