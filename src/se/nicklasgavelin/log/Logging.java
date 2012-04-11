package se.nicklasgavelin.log;

import com.intel.bluetooth.DebugLog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import se.nicklasgavelin.configuration.ProjectProperties;

/**
 * Manages the logging of the application.
 * If the log4j logging class can't be found the logging will be
 * disabled by default. Otherwise the logging will follow
 * the settings in the Configuration class
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 * @version 2.1
 * 
 *          Notice: Based on the debug logger in Bluecove
 */
public class Logging
{
	private static final Logging log = new Logging();
	private static boolean initialized = false;
	private static Collection<Appender> logAppenders;
	private static boolean log4exists = true;
	private static final String log4logger = "site.nicklas.log.Log4JLogger";
	private static final String from = Logging.class.getName();
	private static final Collection<String> fromCollection = new ArrayList<String>();
	private static final Logger logger = Logger.getLogger( ProjectProperties.getInstance().getLoggerName() );// Configuration.loggerName
																												// );

	static
	{
		fromCollection.add( from );
	}

	/**
	 * Different debug levels,
	 * mirrors that of log4j levels
	 */
	public static enum Level
	{
		/**
		 * Debugging
		 */
		DEBUG( java.util.logging.Level.FINE ),
		/**
		 * Information
		 */
		INFO( java.util.logging.Level.FINEST ),
		/**
		 * Warnings
		 */
		WARN( java.util.logging.Level.WARNING ),
		/**
		 * Errors
		 */
		ERROR( java.util.logging.Level.SEVERE ),
		/**
		 * Fatal errors
		 */
		FATAL( java.util.logging.Level.SEVERE );
		private static int nextVal = 0;
		private int val;
		private java.util.logging.Level l;

		private Level( java.util.logging.Level _l )
		{
			this.initialize();
			this.l = _l;
		}

		private void initialize()
		{
			this.val = Level.nextVal++;
		}

		protected int getValue()
		{
			return this.val;
		}

		protected java.util.logging.Level getLevel()
		{
			return this.l;
		}
	}

	/**
	 * Create a logging object
	 */
	private Logging()
	{
	}

	/**
	 * Returns the log instance
	 * 
	 * @return The log instance
	 */
	public static Logging getInstance()
	{
		return log;
	}

	/*
	 * ************************************
	 * CLASSES
	 */
	/**
	 * Log appender interface
	 */
	protected static interface Appender
	{
		/**
		 * Log message
		 * 
		 * @param l The log level
		 * @param message The log message
		 * @param t Throwable object to log
		 */
		public void log( Level l, String message, Throwable t );

		public boolean isLogEnabled( Level l );
	}

	/*
	 * *******************
	 * INITIALIZE
	 */

	/**
	 * Initialize the debugger
	 */
	private static void initialize()
	{
		// Check if we have initialized earlier
		if( initialized )
			return;

		// Set initialized
		initialized = true;

		logAppenders = new ArrayList<Appender>();

		// Check if we can use log4j as debugger
		try
		{
			Appender log4jAppender = (Appender) Class.forName( Logging.log4logger ).newInstance();
			add( log4jAppender );

			// System.out.println( "[" + Logging.class.getCanonicalName() +
			// "] Redirecting log to log4j (" + Configuration.debugEnabled + ")" );
		}
		catch( Throwable e )
		{
			log4exists = false;

			setLevel();

			Logging.debug( "[" + Logging.class.getCanonicalName() + "] Turning off debug as no log4j instance could be created" );
		}
	}

	private static void setLevel()
	{
		// Fetch project settings
		ProjectProperties pp = ProjectProperties.getInstance();

		Logger topLogger = java.util.logging.Logger.getLogger( pp.getLoggerName() );// Configuration.loggerName
																					// );
		topLogger.setLevel( pp.getDebugLevel().getLevel() ); // Configuration.debugLevel.getLevel()
																// );

		// Set bluecove log status
		DebugLog.setDebugEnabled( pp.getBluecoveDebugEnabled() );

		// Handler for console (reuse it if it already exists)
		Handler consoleHandler = null;
		// see if there is already a console handler
		for( Handler handler : topLogger.getHandlers() )
		{
			if( handler instanceof ConsoleHandler )
			{
				// found the console handler
				consoleHandler = handler;
				break;
			}
		}

		if( consoleHandler == null )
		{
			// there was no console handler found, create a new one
			consoleHandler = new ConsoleHandler();
			topLogger.addHandler( consoleHandler );
		}

		// set the console handler to fine:
		consoleHandler.setLevel( pp.getDebugLevel().getLevel() );// Configuration.debugLevel.getLevel()
																	// );
	}

	/**
	 * Enable or disable logging manually
	 * 
	 * @param enabled True to enable, false to disable
	 */
	public static void setDebugEnabled( boolean enabled )
	{
		initialize();
		// Configuration.debugEnabled = enabled;
		ProjectProperties.getInstance().setDebugEnabled( enabled );
	}

	/**
	 * Call all log appenders
	 * 
	 * @param l The log level
	 * @param msg The message
	 * @param t Any throwable to log
	 */
	private static void callAppenders( Level l, String msg, Throwable t )
	{
		// Perform initialization if not already done
		initialize();

		ProjectProperties pp = ProjectProperties.getInstance();

		// Check if we have debug enabled or if the level is fatal
		if( ( !pp.getDebugEnabled() && !l.equals( Level.FATAL ) ) )
			return;

		// Check if we want the messages of this level to be logged
		if( l.getValue() < pp.getDebugLevel().getValue() )
			return;

		if( !log4exists )
		{
			// Native debug
			nativeDebug( l, msg, t );
		}
		else
		{
			Iterator<Appender> i = logAppenders.iterator();

			while( i.hasNext() )
			{
				Appender la = i.next();
				la.log( l, msg, t );
			}
		}
	}

	/**
	 * Debug method that is run instead of appenders if no appenders
	 * could be found
	 * 
	 * @param l The level of the message
	 * @param msg The message
	 * @param t The throwable object or null
	 */
	private static void nativeDebug( Level l, String msg, Throwable t )
	{
		// Fetch location for the message
		UtilsJavaSE.StackTraceLocation s = UtilsJavaSE.getLocation( fromCollection );
		logger.setLevel( ProjectProperties.getInstance().getDebugLevel().getLevel() );
		Logging.logger.logp( l.getLevel(), s.className, s.methodName, "\t" + msg + "\n", t );
	}

//	/**
//	 * Returns the location from which the message was created
//	 * 
//	 * @param s The stack location
//	 * 
//	 * @return The location or ""
//	 */
//	private static String fromLocation( UtilsJavaSE.StackTraceLocation s )
//	{
//		if( s == null )
//			return "";
//		return s.className + "." + s.methodName + "(" + s.fileName + ":" + s.lineNumber + ")";
//	}

	/**
	 * Add an appender to call when running callAppenders
	 * 
	 * @param loggerAppender The appender to add
	 */
	private static void add( Appender loggerAppender )
	{
		logAppenders.add( loggerAppender );
	}

	/*
	 * ***********************************
	 * LOGGING
	 */

	/**
	 * Print a debug message
	 * 
	 * @param msg The debug message
	 */
	public static void debug( String msg )
	{
		callAppenders( Level.DEBUG, msg, null );
	}

	/**
	 * Print a debug message with a throwable object
	 * 
	 * @param msg The message to log
	 * @param t The throwable object to log
	 */
	public static void debug( String msg, Throwable t )
	{
		callAppenders( Level.DEBUG, msg, t );
	}

	/**
	 * Print a debug message with a specific extra value
	 * 
	 * @param msg The message to log
	 * @param v The value to log
	 */
	public static void debug( String msg, String v )
	{
		callAppenders( Level.DEBUG, msg + " " + v, null );
	}

	/**
	 * Print a debug message with a specific object
	 * 
	 * @param msg The message to log
	 * @param o The object to log
	 */
	public static void debug( String msg, Object o )
	{
		callAppenders( Level.DEBUG, msg + " " + o.toString(), null );
	}

	/**
	 * Print an error message
	 * 
	 * @param msg The error message
	 */
	public static void error( String msg )
	{
		callAppenders( Level.ERROR, msg, null );
	}

	/**
	 * Print an error message with a specific throwable object
	 * 
	 * @param msg The message to log
	 * @param t The throwable object to log
	 */
	public static void error( String msg, Throwable t )
	{
		callAppenders( Level.ERROR, msg, t );
	}

	/**
	 * Print an error message and a specific value
	 * 
	 * @param msg The message to log
	 * @param v The value to log
	 */
	public static void error( String msg, String v )
	{
		callAppenders( Level.ERROR, msg + " " + v, null );
	}

	/**
	 * Print an error message and a specific object
	 * 
	 * @param msg The message to log
	 * @param o The object to log
	 */
	public static void error( String msg, Object o )
	{
		callAppenders( Level.ERROR, msg + " " + o.toString(), null );
	}

	/**
	 * Print an info message
	 * 
	 * @param msg The info message
	 */
	public static void info( String msg )
	{
		callAppenders( Level.INFO, msg, null );
	}

	/**
	 * Print a specific info message and a throwable object
	 * 
	 * @param msg The message to log
	 * @param t The throwable object
	 */
	public static void info( String msg, Throwable t )
	{
		callAppenders( Level.INFO, msg, t );
	}

	/**
	 * Print a specific info message and a value
	 * 
	 * @param msg The message to log
	 * @param v The value to log
	 */
	public static void info( String msg, String v )
	{
		callAppenders( Level.INFO, msg + " " + v, null );
	}

	/**
	 * Log a warning message with an object
	 * 
	 * @param msg The warning message
	 * @param o The object to log
	 */
	public static void info( String msg, Object o )
	{
		callAppenders( Level.INFO, msg + " " + o.toString(), null );
	}

	/**
	 * Log a warning message
	 * 
	 * @param msg The warning message
	 */
	public static void warn( String msg )
	{
		callAppenders( Level.WARN, msg, null );
	}

	/**
	 * Log a warning message and a throwable object
	 * 
	 * @param msg The warning message
	 * @param t The throwable object
	 */
	public static void warn( String msg, Throwable t )
	{
		callAppenders( Level.WARN, msg, t );
	}

	/**
	 * Log a warning message with a value
	 * 
	 * @param msg The warning message
	 * @param v The value to log
	 */
	public static void warn( String msg, String v )
	{
		callAppenders( Level.WARN, msg + " " + v, null );
	}

	/**
	 * Log a warning message with an object
	 * 
	 * @param msg The warning message
	 * @param o The object to log
	 */
	public static void warn( String msg, Object o )
	{
		callAppenders( Level.WARN, msg + " " + o.toString(), null );
	}

	/**
	 * Log a fatal message
	 * 
	 * @param msg The fatal message
	 */
	public static void fatal( String msg )
	{
		callAppenders( Level.FATAL, msg, null );
	}

	/**
	 * Log a fatal message with a value
	 * 
	 * @param msg The fatal message
	 * @param v The value to log
	 */
	public static void fatal( String msg, String v )
	{
		callAppenders( Level.FATAL, msg + " " + v, null );
	}

	/**
	 * Log a fatal message with a throwable object
	 * 
	 * @param msg The fatal message
	 * @param e Throwable to log
	 */
	public static void fatal( String msg, Throwable e )
	{
		callAppenders( Level.FATAL, msg, e );
	}

	/**
	 * Log a fatal message with an object
	 * 
	 * @param msg The fatal message
	 * @param o The object to log
	 */
	public static void fatal( String msg, Object o )
	{
		callAppenders( Level.FATAL, msg + " " + o.toString(), null );
	}
}
