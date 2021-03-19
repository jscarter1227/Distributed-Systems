package messages;

import java.io.*;

public class Message implements msgTypes {
    public int type;
    public int amount;
    public int accNumber; // account number

    // read constructor
    public Message(int type, int accNumber){
        this.amount = 0;
        this.type = type;
        this.accNumber = accNumber;
    }

    // write message constructor
    public Message(int type, int accNumber, int amount){
        this.amount = amount;
        this.accNumber = accNumber;
        this.type = type;
    }

    // open/close Transaction message constructor 
    public Message(int type){
        this.amount = 0;
        this.accNumber = 0;
        this.type = type;
    }

    public int getType(){
        return this.type;
    }

    public int getAccountNumber(){
        return this.accNumber;
    }

    public Object[] getAccountInfo(){
        Object[] accountInfo = {accNumber, amount};
        return accountInfo;
    }
}
