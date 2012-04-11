package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SD1 extends MacroCommand
{
	private int delay;
	public static final int MIN_DELAY = 0, MAX_DELAY = 65534;

	// public SD1( byte[] data )
	// {
	// }
	/**
	 * Create SD1 macro command with a given delay
	 * 
	 * @param _delay The delay (MIN_DELAY - MAX_DELAY)
	 */
	public SD1( Integer _delay )
	{
		super( MACRO_COMMAND.MAC_SD1 );
		this.setDelay( _delay );
	}

	/**
	 * Returns the internal delay value
	 * 
	 * @return The internal delay value
	 */
	public Integer getDelay()
	{
		return this.delay;
	}

	/**
	 * Update the internal delay value
	 * 
	 * @param _delay The new delay value (MIN_DELAY - MAX_DELAY)
	 */
	public void setDelay( int _delay )
	{
		this.delay = Value.clamp( _delay, MIN_DELAY, MAX_DELAY );
		// if ( (_delay.intValue() >= MIN_DELAY) && (_delay.intValue() <= MAX_DELAY) )
		// this.delay = _delay;
	}

	@Override
	public byte[] getByteRepresentation()
	{
		ByteArrayBuffer bytes = new ByteArrayBuffer( getLength() );
		bytes.append( getCommandID() );
		bytes.append( this.delay >> 8 );
		bytes.append( this.delay & 0xFF );
		return bytes.toByteArray();
	}
}