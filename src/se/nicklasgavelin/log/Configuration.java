/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
package se.nicklasgavelin.log;

import com.intel.bluetooth.DebugLog;

/**
 * Handles configuration such as debug level and debugging
 *
 * @author Nicklas Gavelin
 */
public class Configuration
{
    /* Debug settings */
    public static boolean debugEnabled = true;
    public static boolean bluecoveDebugEnabled = false;
    public static Logging.Level debugLevel = Logging.Level.DEBUG;
    private static final Configuration c = new Configuration();


    private Configuration()
    {
        // Set bluecove debugging
        DebugLog.setDebugEnabled( bluecoveDebugEnabled );
        Logging.info( "Turning " + (bluecoveDebugEnabled ? "ON" : "OFF") + " bluecove debugger" );
    }
}
