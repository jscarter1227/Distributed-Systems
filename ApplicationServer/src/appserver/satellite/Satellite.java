package appserver.satellite;

import appserver.job.Job;
import appserver.comm.ConnectivityInfo;
import appserver.job.UnknownToolException;
import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.job.Tool;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.PropertyHandler;

/**
 * Class [Satellite] Instances of this class represent computing nodes that execute jobs by
 * calling the callback method of tool a implementation, loading the tool's code dynamically over a network
 * or locally from the cache, if a tool got executed before.
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Satellite extends Thread {

    private ConnectivityInfo satelliteInfo = new ConnectivityInfo();
    private ConnectivityInfo serverInfo = new ConnectivityInfo();
    private HTTPClassLoader classLoader = null;
    private Hashtable<String, Tool> toolsCache = null;

    public Satellite(String satellitePropertiesFile, String classLoaderPropertiesFile, String serverPropertiesFile) {

        // read this satellite's properties and populate satelliteInfo object,
        // which later on will be sent to the server
        // ...
        try {
			Properties props = new PropertyHandler(satellitePropertiesFile);
			satelliteInfo.setName(props.getProperty("NAME"));
			satelliteInfo.setPort(Integer.parseInt(props.getProperty("PORT")));
		} catch (IOException e) {
			System.err.println("[Satellite] IOException when parsing satellite properties file");
			e.printStackTrace();
		}
        
        // read properties of the application server and populate serverInfo object
        // other than satellites, the as doesn't have a human-readable name, so leave it out
        // ...
        Properties serverProps;
		try {
			serverProps = new PropertyHandler(serverPropertiesFile);
	        serverInfo.setHost(serverProps.getProperty("HOST"));
	        serverInfo.setPort(Integer.parseInt(serverProps.getProperty("PORT")));
		} catch (IOException e) {
			System.err.println("[Satellite] IOException when parsing server properties file");
			e.printStackTrace();
		}

        
        // TODO: This during part 3? Otte said not to worry about this for now.
        // read properties of the code server and create class loader
        // -------------------
        // ...
        
        // create tools cache
        // -------------------
        // ...
        toolsCache = new Hashtable<String, Tool>();
    }

    @Override
    public void run() {
    	ServerSocket serverSocket = null;
    	Socket socket = null;
        // register this satellite with the SatelliteManager on the server
        // ---------------------------------------------------------------
        // ...
    	// TODO: Otte has this here but shouldnt it be after we create server socket?
        
        
        // create server socket
        // ---------------------------------------------------------------
        // ...
    	try {
			serverSocket = new ServerSocket(serverInfo.getPort());
			socket = new Socket(serverInfo.getHost(), serverInfo.getPort());
		} catch (IOException e) {
			System.err.println("[Satellite.run] Error creating socket");
			e.printStackTrace();
		}
        
        
        // start taking job requests in a server loop
        // ---------------------------------------------------------------
        // ...
    	while(true) {
    		try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		(new SatelliteThread(socket, this)).start();
    	}
    }

    // inner helper class that is instanciated in above server loop and processes single job requests
    private class SatelliteThread extends Thread {

        Satellite satellite = null;
        Socket jobRequest = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;

        SatelliteThread(Socket jobRequest, Satellite satellite) {
            this.jobRequest = jobRequest;
            this.satellite = satellite;
        }

        @Override
        public void run() {
        	boolean running = true;
            // setting up object streams
            // ...
    		try {
				writeToNet = new ObjectOutputStream(jobRequest.getOutputStream());
				readFromNet = new ObjectInputStream(jobRequest.getInputStream());
			} catch (IOException e) {
				System.err.println("[SatelliteThread.run] Error creating object streams");
				e.printStackTrace();
			}
    		
    		while(true) {
                // reading message
                // ...
    			try {
					message = (Message)readFromNet.readObject();
				} catch (ClassNotFoundException | IOException e) {
					System.err.println("[SatelliteThread.run] Error reading message");
					e.printStackTrace();
				}
    			
                switch (message.getType()) {
	                case JOB_REQUEST:
	                    // processing job request
	                    // ...
	                	Job requestedJob = (Job) message.getContent();
						try {
							Tool requestedTool = getToolObject(requestedJob.getToolName());
						} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
								| UnknownToolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	                	
						Object toClient = requestedTool(requestedJob.getParameters);

						writeToNet.writeObject(toClient);
						
	                    break;
	
	                default:
	                    System.err.println("[SatelliteThread.run] Warning: Message type not implemented");
	            }
    		}

        }
    }

    /**
     * Aux method to get a tool object, given the fully qualified class string
     * If the tool has been used before, it is returned immediately out of the cache,
     * otherwise it is loaded dynamically
     */
    public Tool getToolObject(String toolClassString) throws UnknownToolException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Tool toolObject = null;

        // this code is the same as dyncalculator just w diff stuff obvi
        if ((toolObject = toolsCache.get(toolClassString)) == null) 
        {
            //String operationClassString = configuration.getProperty(operationString);
            System.out.println("\nTool's Class: " + toolClassString);
            if (toolClassString == null) 
            {
                throw new UnknownToolException();
            }
            Class<?> toolClass = classLoader.loadClass(toolClassString);
            try {
				toolObject = (Tool) toolClass.getDeclaredConstructor().newInstance();
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				System.err.println("[getToolObject] getOperation() - InvocationTargetException");
				e.printStackTrace();
			}
            toolsCache.put(toolClassString, toolObject);
        } 
        else 
        {
            System.out.println("Tool: \"" + toolClassString + "\" already in Cache");
        }
        
        return toolObject;
    }

    public static void main(String[] args) {
        // start the satellite
        Satellite satellite = new Satellite(args[0], args[1], args[2]);
        satellite.run();
    }
}
