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
    	System.out.println("[LoadManager.satelliteAdded] " + satelliteName + " added");
    }


    public String nextSatellite() throws Exception {
        
        int numberSatellites = satellites.size();
        // implement policy that returns the satellite name according to a round robin methodology
        synchronized (satellites) {
            if(lastSatelliteIndex == numberSatellites - 1) {
                lastSatelliteIndex = 0;
            } else {
                lastSatelliteIndex += 1;
            }

        }
        // ... name of satellite who is supposed to take job
        return satellites.get(lastSatelliteIndex).toString();
    }
}
