package chat;

import java.util.*;
import java.io.*;
import java.net.*;

public class Sender implements Runnable{
    // constants to be used
    final int JOIN = 1;
    final int LEAVE = 2;
    final int NOTE = 3;
    final char DOLLARSIGN = 'S';

    // user connection information
    private String IP;
    private String PORT;

    // leave and not information
    ConnectionInfo currentConnection;

    // global information for class
    String data;
    
    // data streams
    ObjectInputStream fromReceiver;
    ObjectOutputStream toReceiver;

    // Sender constructor
    Seender(String data){
        // will be used later on
        this.data = data;
    }

    // main thread method
    public void run(){
        // determine the type of message that will be sent
        int messageType = getMessageType(data);

        // handles join message
        if(messageType == JOIN && getConnInfo(data)){
            try{
                // create data streams and connection socket
                Socket socket = new Socket(IP, Integer.parseInt(PORT));
                toReceiver = new ObjectOutputStream(socket.getOutputStream());
                fromReceiver = new ObjectInputStream(socket.getInputStream());

                // create message
                Join newMessage = new Join(ChatNode.personalInfo);

                // send message
                toReceiver.writeObject(newMessage);

                // create a way to receive the message
                UpdateList receivedMessage = (UpdateList)fromReceiver.readObject();

                // update the list
                ChatNode.connectionList = receivedMessage.connectionList;

                // print success message
                System.out.println("You have joined the chat! Have fun!\n");

                // close the socket
                socket.close();
            }

            catch(IOException e){
                System.out.println("Failure on joining chat :( please try again!");
            }

            catch(NumberFormatException e){
                System.out.println("Port number is invalid.");
            }

            catch(ClassNotFoundException e){
                e.printStackTrace();
            }
        }

        // handles leave message
        else if(messageType == LEAVE){
            // creates leave message
            Leave leaveMessage = new Leave(ChatNode.personalInfo);
            
            // declaration of variables
            int index;
            ConnectionInfo currentConnection;

            for(index = 0; index < ChatNode.connectionList.size(); index++){
                // get current connection
                currentConnection = ChatNode.connectionList.get(index);

                // make sure its not our own connection
                if(!currentConnection.personalIP.equals(ChatNode.personalInfo.personalIP)){
                    try{
                        // create data streams and connection socket
                        Socket socket = new Socket(currentConnection.personalIP, currentConnection.personalPORT);
                        toReceiver = new ObjectOutputStream(socket.getOutputStream());
                        fromReceiver = new ObjectInputStream(socket.getInputStream());

                        // send message
                        toReceiver.writeObject(leaveMessage);

                        // close the socket
                        socket.close();
                    }
                    catch(IOException e){
                        System.out.println("Uh oh! Some went wrong with connection to: " + currentConnection.logicalName);
                    }
                }
            }

            // display message
            System.out.println("You disconnected from the chat");
        }

        //  handles note message
        else{
            // create note message
            Note newNote = new Note(data, ChatNode.personalInfo);

            // declaration of variables
            int index;
            ConnectionInfo currentConnection;

            for(index = 0; index < ChatNode.connectionList.size(); index++){
                // get current connection
                currentConnection = ChatNode.connectionList.get(index);

                // make sure its not our own connection
                if(!currentConnection.personalIP.equals(ChatNode.personalInfo.personalIP)){
                    try{
                        // create data streams and connection socket
                        Socket socket = new Socket(currentConnection.personalIP,currentConnection.personalPORT);
                        toReceiver = new ObjectOutputStream(socket.getOutputStream());
                        fromReceiver = new ObjectInputStream(socket.getInputStream());

                        // send message
                        toReceiver.writeObject(newNote);
                    }
                    catch{
                        System.out.println("Uh oh! Something went wrong with connection to: " + currentConnection.logicalName);
                    }
                }
            }
            // display message
            System.out.println(ChatNode.personalInfo + ": " + data);
        }

    }

    // will iterate over current string to find connection information
    Boolean getConnInfo(String data){
        // declaration of variables
        String currentString = "";
        char currentChar;
        int index;
        int dollarCounter = 0;

        for(index = 0; index < data.length(); index++){
            // get current character
            currentChar = data.charAt(index);

            // check for dollarsign
            if(currentChar == DOLLARSIGN){
                // check for IP
                if(dollarCounter == 3){
                    IP = currentString;
                }
                // check for PORT
                if(dollarCounter == 4){
                    PORT = currentString;
                }
                // increment dollar counter
                dollarCounter++;
                // reset string
                currentString = "";
            }

            // check if string is too long
            if(dollarCounter == 4){
                return false;
            }
            // add current character
            currentString += currentChar;
        }

        // check for incorrect string
        if(dollarCounter != 4){
            return false;
        }

        // this means there was success
        return true;
    }

    int getMessageType(String data){
        // declaration of variables
        String currentString="";
        char currentChar;
        int index;

        for(index = 0; index < data.length(); index++){
            // get current char
            currentChar = data.charAt(index);

            // check if the current char is the delimiter char
            if(currentChar == DOLLARSIGN){
                // check for JOIN
                if(currentString.equalsIgnoreCase("join")){
                    return JOIN;
                }
                // check for LEAVE
                else if(currentString.equalsIgnoreCase("leave")){
                    return LEAVE;
                }
                //reset current string
                currentString ="";
            }
            // otherwise add char to the current string
            else{
                currentString += currentChar;
            }
        }
        return NOTE;
    }
}