/*
 * Please read the LICENSE file that is included with the source
 * code.
 */

package se.nicklasgavelin.configuration;

import java.awt.Color;
import java.util.Properties;
import se.nicklasgavelin.log.Logging;
import se.nicklasgavelin.sphero.RobotSetting;
import se.nicklasgavelin.sphero.command.RawMotorCommand;

/**
 * Used for returning current configuration settings
 * Settings are stored in project.properties in the same packet as this class.
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class ProjectProperties extends Properties
{
	private static final long serialVersionUID = 4819632381205752349L;
	private static ProjectProperties instance;

	/**
	 * Create project properties
	 */
	private ProjectProperties()
	{
		super();

		try
		{
			// Load the property file
			this.load( ProjectProperties.class.getResourceAsStream( "project.properties" ) );
		}
		catch( Exception e )
		{
			// Unable to load property file, sorry :(
		}
	}

	/**
	 * Returns default robot settings
	 * 
	 * @return Default robot settings
	 */
	public RobotSetting getRobotSetting()
	{
		return new RobotSetting( new Color( Integer.parseInt( this.getProperty( "sphero.color.rgb.red", "255" ) ), Integer.parseInt( this.getProperty( "sphero.color.rgb.green", "255" ) ), Integer.parseInt( this.getProperty( "sphero.color.rgb.blue", "255" ) ) ), Integer.parseInt( this.getProperty( "sphero.pinginterval", "255" ) ), Float.parseFloat( this.getProperty( "sphero.color.brightness", "1" ) ), Integer.parseInt( this.getProperty( "sphero.motor.heading", "0" ) ), Integer.parseInt( this.getProperty( "sphero.motor.speed", "0" ) ), Integer.parseInt( this.getProperty( "sphero.macro.size", "0" ) ), Integer.parseInt( this.getProperty( "sphero.macro.storage", "0" ) ), Integer.parseInt( this.getProperty( "sphero.macro.minsize", "128" ) ), Boolean.parseBoolean( this.getProperty( "sphero.motor.stop", "true" ) ), Float.parseFloat( this.getProperty( "sphero.macro.rotationrate", "0" ) ), RawMotorCommand.MOTOR_MODE.valueOf( this.getProperty( "sphero.motor.motormode", RawMotorCommand.MOTOR_MODE.FORWARD.toString() ) ) );
	}

	/**
	 * Returns size of received buffer
	 * 
	 * @return The size of the received buffer
	 */
	public int getBufferSize()
	{
		return Integer.parseInt( this.getProperty( "sphero.socket.buffersize", "256" ) );
	}

	/**
	 * Returns the current debug state
	 * 
	 * @return True for on, false for off
	 */
	public boolean getDebugEnabled()
	{
		return Boolean.parseBoolean( this.getProperty( "debug.enabled", "false" ) );
	}

	/**
	 * Set debug status
	 * 
	 * @param enabled The new debug status (true for on, false otherwise)
	 */
	public void setDebugEnabled( boolean enabled )
	{
		this.setProperty( "debug.enabled", Boolean.toString( enabled ) );
	}

	/**
	 * Returns the current bluecove debug state
	 * 
	 * @return The current bluecove debug state
	 */
	public boolean getBluecoveDebugEnabled()
	{
		return Boolean.parseBoolean( this.getProperty( "debug.bluecove.enabled", "false" ) );
	}

	/**
	 * Set bluecove debug status
	 * 
	 * @param enabled New debug status
	 */
	public void setBluecoveDebugEnabled( boolean enabled )
	{
		this.setProperty( "debug.bluecove.enabled", Boolean.toString( enabled ) );
	}

	/**
	 * Returns the logger name
	 * 
	 * @return The logger name
	 */
	public String getLoggerName()
	{
		return this.getProperty( "debug.loggername", "se.nicklasgavelin" );
	}

	/**
	 * Set the name of the logger
	 * WILL NOT WORK AFTER THE LOGGER HAVE BEEN INITIALIZED
	 * 
	 * @param name The new name for the logger
	 */
	public void setLoggerName( String name )
	{
		this.setProperty( "debug.loggername", name );
	}

	/**
	 * Returns the current debug level,
	 * default level is Logging.Level.FATAL
	 * 
	 * @return The set debug level
	 */
	public Logging.Level getDebugLevel()
	{
		return Logging.Level.valueOf( this.getProperty( "debug.level", Logging.Level.FATAL.toString() ) );
	}

	/**
	 * Returns the properies instance
	 * 
	 * @return The property instance
	 */
	public static ProjectProperties getInstance()
	{
		// Check if we have a previous instance
		if( ProjectProperties.instance == null )
			ProjectProperties.instance = new ProjectProperties();

		return ProjectProperties.instance;
	}
}
