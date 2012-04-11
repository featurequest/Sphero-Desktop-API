package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class RollSD1 extends MacroCommand
{
	private int heading;
	private double speed;
	public static final double MIN_SPEED = 0, MAX_SPEED = 1;
	public static final int MIN_HEADING = 0, MAX_HEADING = 359;

	// public RollSD1( byte[] data )
	// {
	// }
	/**
	 * Create a Roll SD1 macro command with a given speed and heading
	 * 
	 * @param _speed The speed (MIN_SPEED - MAX_SPEED)
	 * @param _heading The heading (MIN_HEADING - MAX_HEADING)
	 */
	public RollSD1( double _speed, int _heading )
	{
		super( MACRO_COMMAND.MAC_ROLL_SD1 );
		this.setSpeed( _speed );
		this.setHeading( _heading );
	}

	/**
	 * Returns the internal heading value
	 * 
	 * @return The internal heading value
	 */
	public int getHeading()
	{
		return this.heading;
	}

	/**
	 * Update the internal heading value
	 * 
	 * @param _heading The new heading value (MIN_HEADING - MAX_HEADING)
	 */
	public void setHeading( int _heading )
	{
		this.heading = Value.clamp( _heading, MIN_HEADING, MAX_HEADING );
		// if ( (_heading.intValue() >= 0) && (_heading.intValue() <= 359) )
		// this.heading = _heading;
	}

	/**
	 * Returns the internal speed value
	 * 
	 * @return The internal speed value
	 */
	public double getSpeed()
	{
		return this.speed;
	}

	/**
	 * Upate the internal speed value
	 * 
	 * @param _speed The internal speed value
	 */
	public void setSpeed( double _speed )
	{
		this.speed = Value.clamp( _speed, MIN_SPEED, MAX_SPEED );
		// if ( (_speed.doubleValue() >= 0.0D) && (_speed.doubleValue() <= 1.0D) )
		// this.speed = _speed;
	}

	@Override
	public byte[] getByteRepresentation()
	{
		ByteArrayBuffer bytes = new ByteArrayBuffer( getLength() );
		bytes.append( getCommandID() );
		bytes.append( (int) ( this.speed * 255.0D ) );
		bytes.append( this.heading >> 8 );
		bytes.append( this.heading & 0xFF );

		return bytes.toByteArray();
	}
}