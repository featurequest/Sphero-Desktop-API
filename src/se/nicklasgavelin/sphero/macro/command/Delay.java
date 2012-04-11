package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;

/**
 * Macro command for waiting a specific delay before running the next macro
 * command, add this macro command BETWEEN two different macro commands
 * to add a delay between them.
 * 
 * Example (Light up with RED color for 4 seconds, then with GREEN color for 2 seconds):
 * mo.addCommand( new RGBSD2( 255, 0, 0 ) );
 * mo.addCommand( new Delay(4000) );
 * mo.addCommand( new RGBSD2( 0, 255, 0 ) );
 * mo.addCommand( new Delay(2000) );
 * 
 * 
 * 
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class Delay extends MacroCommand
{
	private Integer delay = Integer.valueOf( 1000 );
	public static final int MAX_DELAY = 65534, MIN_DELAY = 0;

	// public Delay( byte[] data )
	// {
	// }

	/**
	 * Create a delay command with a given delay
	 * 
	 * @param _delay The delay for the command (0-65535)
	 */
	public Delay( Integer _delay )
	{
		super( MACRO_COMMAND.MAC_DELAY );
		this.setDelay( _delay );
	}

	public Integer getDelay()
	{
		return this.delay;
	}

	public final void setDelay( Integer _delay )
	{
		if( ( _delay.intValue() >= MIN_DELAY ) && ( _delay.intValue() <= MAX_DELAY ) )
			this.delay = _delay;
	}

	@Override
	public byte[] getByteRepresentation()
	{
		ByteArrayBuffer bytes = new ByteArrayBuffer( getLength() );
		bytes.append( getCommandID() );
		bytes.append( this.delay.intValue() >> 8 );
		bytes.append( this.delay.intValue() & 0xFF );
		return bytes.toByteArray();
	}
}