package se.nicklasgavelin.sphero.exception;

/**
 * Thrown when a connection to a given robot can't be initialized successfully
 * due to either a Bluetooth connection error or some other error
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 * 
 */
public class RobotInitializeConnectionFailed extends RuntimeException
{
	private static final long serialVersionUID = -2076464336850241717L;

	public RobotInitializeConnectionFailed( String s )
	{
		super( s );
	}

	public RobotInitializeConnectionFailed()
	{
	}
}
