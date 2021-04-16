package appserver.server;

import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.comm.ConnectivityInfo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import utils.PropertyHandler;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Server {

    // Singleton objects - there is only one of them. For simplicity, this is not enforced though ...
    static SatelliteManager satelliteManager = null;
    static LoadManager loadManager = null;
    static ServerSocket serverSocket = null;

    public Server(String serverPropertiesFile) {
        int port = 0;
        // create satellite manager and load manager
        satelliteManager = new SatelliteManager();
        loadManager = new LoadManager();
        
        try {
            // read server properties and create server socket
            Properties props = new PropertyHandler(serverPropertiesFile);
            port = Integer.parseInt(props.getProperty("PORT"));
        } catch (Exception e) {
            System.err.println("[Server] IOException when parsing server properties file");
            e.printStackTrace();
        }
        
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("[Server] Error creating serversocket");
            e.printStackTrace();
        }
    }

    public void run() {
        // serve clients in server loop ...
        // when a request comes in, a ServerThread object is spawned
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                (new ServerThread(socket)).start();
            } catch (IOException e) {
                System.err.println("[Server.run] Error creating socket");
                e.printStackTrace();
            }
        }
    }

    // objects of this helper class communicate with satellites or clients
    private class ServerThread extends Thread {

        Socket client = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;

        private ServerThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            // set up object streams
            try {
                writeToNet = new ObjectOutputStream(client.getOutputStream());
                readFromNet = new ObjectInputStream(client.getInputStream());
            } catch (Exception e) {
                System.err.println("[ServerThead.run] Error creating streams");
            }
            
            // Read message
            try {
                message = (Message)readFromNet.readObject();
            } catch (ClassNotFoundException | IOException e) {
                System.err.println("[ServerThread.run] Error reading message");
            }
            
            
            // process message
            switch (message.getType()) {
                case REGISTER_SATELLITE:
                    // read satellite info
                    ConnectivityInfo satelliteInfo = (ConnectivityInfo) message.getContent();
                    
                    // register satellite
                    synchronized (Server.satelliteManager) {
                        satelliteManager.registerSatellite(satelliteInfo);
                    }

                    // add satellite to loadManager
                    synchronized (Server.loadManager) {
                        loadManager.satelliteAdded(satelliteInfo.getName());
                    }

                    break;

                case JOB_REQUEST:
                    System.err.println("\n[ServerThread.run] Received job request");
                    ConnectivityInfo nextSatellite;
                    String satelliteName = null;
                    synchronized (Server.loadManager) {
                        // get next satellite from load manager
                        try {
                            satelliteName = loadManager.nextSatellite();
                        } catch (Exception e) {
                            System.err.println("\n[ServerThread.run] Error getting next satellite");
                            e.printStackTrace();
                        }
                        
                        // get connectivity info for next satellite from satellite manager
                        nextSatellite = satelliteManager.getSatelliteForName(satelliteName);
                    }

                    Socket satellite = null;
                    // connect to satellite
                    try {
                        satellite = new Socket(nextSatellite.getHost(), nextSatellite.getPort());
                    } catch (Exception e) {
                        System.err.println("\n[ServerThread.run] Error opening satellite socket");
                        e.printStackTrace();
                    }
                    
                    ObjectOutputStream outputStream;
                    ObjectInputStream inputStream;
                    try {
                        // open object streams
                        outputStream = new ObjectOutputStream(satellite.getOutputStream());
                        inputStream = new ObjectInputStream(satellite.getInputStream());
                        // forward message (as is) to satellite,
                        outputStream.writeObject(message);
                        // receive result from satellite
                        Object result = inputStream.readObject();
                        // write result back to client
                        writeToNet.writeObject(result);
                    } catch (Exception e) {
                        System.err.println("\n[ServerThread.run] Error reading/writing message");
                    }


                    break;

                default:
                    System.err.println("[ServerThread.run] Warning: Message type not implemented");
            }
        }
    }

    // main()
    public static void main(String[] args) {
        // start the application server
        Server server = null;
        if(args.length == 1) {
            server = new Server(args[0]);
        } else {
            server = new Server("../../config/Server.properties");
        }
        server.run();
    }
}
