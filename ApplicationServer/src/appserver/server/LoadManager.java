package appserver.server;

import java.util.ArrayList;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class LoadManager {

    static ArrayList satellites = null;
    static int lastSatelliteIndex = -1;

    public LoadManager() {
        satellites = new ArrayList<String>();
    }

    public void satelliteAdded(String satelliteName) {
        // add satellite
    	satellites.add(satelliteName);
    }


    public String nextSatellite() throws Exception {
        
        int numberSatellites = satellites.size();
        int accessSatellite = 0;
        // implement policy that returns the satellite name according to a round robin methodology
        synchronized (satellites) {
            // Increase index
        	lastSatelliteIndex++;
        	// Increment accessSatellite so we know which one to access
        	accessSatellite = lastSatelliteIndex;
        	
        	// if the index is equal to the number of satellites, start over
        	if(lastSatelliteIndex == numberSatellites) {
        		lastSatelliteIndex = -1;
        	}
            
        }
        // ... name of satellite who is supposed to take job
        return satellites.get(accessSatellite).toString();
    }
}
