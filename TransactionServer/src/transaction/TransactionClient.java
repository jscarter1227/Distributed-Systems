package transaction;

import java.io.IOException;
import java.util.Properties;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.*;
import java.util.*;


public class TransactionClient extends Thread{
	
	public TransactionClient(Properties props) throws IOException {
		int port = 0;
		String IP = null;
		
		port = Integer.parseInt(props.getProperty("PORT"));
		IP = props.getProperty("IP");
		
	}
	
	public void run() {
		TransactionServerProxy transaction = new TransactionServerProxy(IP, port);
	}

	
	public static Properties readPropertiesFile(String fileName) throws IOException {
		//Declare file stream, properties item
		  FileInputStream propsreader = null;
	      Properties prop = null;
	      //attempt to create file input stream, new properties object, and load properties into it
	      try {
		         propsreader = new FileInputStream(fileName);
		         prop = new Properties();
		         prop.load(propsreader);
		      } 
	      //catch file not found error
		  catch(FileNotFoundException fileNotFound) {
		         fileNotFound.printStackTrace();
		      } 
	      // catch IO error
		  catch(IOException e) {
		         e.printStackTrace();
		      } 
	      //close the Input stream
		  finally {
		         propsreader.close();
		      }
	      // return properties object
		  return prop;
		  }
	
	public static void main(String[] args) throws IOException {

        // We should probably implement a file reader at some point
		// Hard coding for now
		Properties props = new Properties();
		props.setProperty("IP", "localhost");
		props.setProperty("PORT", "10000");

        (new TransactionClient(props)).run();
	}

}
