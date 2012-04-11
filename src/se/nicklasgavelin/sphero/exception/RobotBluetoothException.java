package se.nicklasgavelin.sphero.exception;

/**
 * Thrown if the Robot fails to initialize (and discover available services)
 * over the Bluetooth connection given
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RobotBluetoothException extends Exception
{
	private static final long serialVersionUID = -3192808568570050687L;

	public RobotBluetoothException()
	{
		super();
	}

	public RobotBluetoothException( String s )
	{
		super( s );
	}
}
