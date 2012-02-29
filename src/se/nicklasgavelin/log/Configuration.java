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

    /**
     * Flag for turning on/off debugging
     */
    public static boolean debugEnabled = true;

    /**
     * Name of logger
     */
    public static final String loggerName = "mohahha";

    /**
     * Value for turning on/off Bluecove library debugging
     */
    public static boolean bluecoveDebugEnabled = false;

    /**
     * The logging level during runtime
     */
    public static Logging.Level debugLevel = Logging.Level.DEBUG;

    static
    {
        // Set bluecove debugging
        DebugLog.setDebugEnabled( Configuration.bluecoveDebugEnabled );
        Logging.info( "Turning " + (Configuration.bluecoveDebugEnabled ? "ON" : "OFF") + " bluecove debugger" );
    }
}
