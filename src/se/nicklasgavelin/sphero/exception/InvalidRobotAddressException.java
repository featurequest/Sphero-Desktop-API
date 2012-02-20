package se.nicklasgavelin.sphero.exception;

/**
 * Thrown if the Robot class is tried to be initialized with
 * a Bluetooth device that is not covered by the API.
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class InvalidRobotAddressException extends Exception
{
    public InvalidRobotAddressException()
    {
        super();
    }

    public InvalidRobotAddressException( String s )
    {
        super( s );
    }
}
