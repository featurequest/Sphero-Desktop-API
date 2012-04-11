package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class SPD1 extends MacroCommand
{
	private double speed = 0.5D;
	public static final double MIN_SPEED = 0, MAX_SPEED = 1;

	// public SPD1( byte[] data )
	// {
	// }

	/**
	 * Create a SPD1 macro command with a given speed value
	 * 
	 * @param _speed The speed value (MIN_SPEED - MAX_SPEED)
	 */
	public SPD1( double _speed )
	{
		super( MACRO_COMMAND.MAC_SPD1 );
		this.setSpeed( _speed );
	}

	/**
	 * Returns the internal speed value
	 * 
	 * @return The internal speed value
	 */
	public Double getSpeed()
	{
		return this.speed;
	}

	/**
	 * Set the internal speed value to a new value
	 * 
	 * @param _speed The new speed value
	 */
	public void setSpeed( double _speed )
	{
		this.speed = Value.clamp( _speed, MIN_SPEED, MAX_SPEED );
		// if ( (_speed >= MIN_SPEED) && (_speed <= MAX_SPEED) )
		// this.speed = _speed;
	}

	@Override
	public byte[] getByteRepresentation()
	{
		Integer speedInt = (int) ( this.speed * 255.0D );
		ByteArrayBuffer bytes = new ByteArrayBuffer( getLength() );
		bytes.append( getCommandID() );
		bytes.append( speedInt.intValue() );
		return bytes.toByteArray();
	}
}