package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class SPD2 extends MacroCommand
{
	private double speed = 0.5D;
	public static final double MIN_SPEED = 0, MAX_SPEED = 1;

	// public SPD2( byte[] data )
	// {
	// }

	/**
	 * Create a SPD2 macro command with a given speed value
	 * 
	 * @param _speed
	 */
	public SPD2( double _speed )
	{
		super( MACRO_COMMAND.MAC_SPD2 );
		this.setSpeed( _speed );
	}

	/**
	 * Returns the current speed value
	 * 
	 * @return The current speed value
	 */
	public Double getSpeed()
	{
		return this.speed;
	}

	/**
	 * Set the current speed value to a new value
	 * 
	 * @param _speed The new speed value (MIN_SPEED - MAX_SPEED)
	 */
	public void setSpeed( Double _speed )
	{
		this.speed = Value.clamp( _speed, MIN_SPEED, MAX_SPEED );
	}

	@Override
	public byte[] getByteRepresentation()
	{
		int speedInt = (int) ( this.speed * 255.0D );
		ByteArrayBuffer bytes = new ByteArrayBuffer( getLength() );
		bytes.append( getCommandID() );
		bytes.append( speedInt );
		return bytes.toByteArray();
	}
}