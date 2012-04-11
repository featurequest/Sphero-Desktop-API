package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class RollSD1SPD2 extends MacroCommand
{
	private int heading;
	public static final int MIN_HEADING = 0, MAX_HEADING = 359;

	// public RollSD1SPD2( byte[] data )
	// {
	// }
	/**
	 * Create a roll sd1 spd2 macro command with a given heading
	 * 
	 * @param _heading The heading (MIN_HEADING - MAX_HEADING)
	 */
	public RollSD1SPD2( int _heading )
	{
		super( MACRO_COMMAND.MAC_ROLL_SPD2_SD1 );
		this.setHeading( _heading );
	}

	/**
	 * Returns the internal heading value
	 * 
	 * @return The heading value
	 */
	public Integer getHeading()
	{
		return this.heading;
	}

	public void setHeading( int _heading )
	{
		this.heading = Value.clamp( _heading, MIN_HEADING, MAX_HEADING );
		// if ( (_heading.intValue() >= 0) && (_heading.intValue() <= 359) )
		// this.heading = _heading;
	}

	@Override
	public byte[] getByteRepresentation()
	{
		ByteArrayBuffer bytes = new ByteArrayBuffer( getLength() );
		bytes.append( getCommandID() );
		bytes.append( this.heading >> 8 );
		bytes.append( this.heading & 0xFF );
		return bytes.toByteArray();
	}
}