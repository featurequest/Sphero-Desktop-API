package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.Value;

/**
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class WaitUntilStop extends MacroCommand
{
	private int mDelay;
	public static final int MIN_DELAY = 0, MAX_DELAY = 65534;

	// public WaitUntilStop( byte[] data )
	// {
	// super( data );
	// }

	public WaitUntilStop( int delay )
	{
		super( MACRO_COMMAND.MAC_WAIT_UNTIL_STOP );
		this.setDelay( delay );
	}

	/**
	 * Update the internal delay value to a new one
	 * 
	 * @param delay The new delay value (MIN_DELAY - MAX_DELAY)
	 */
	public void setDelay( int delay )
	{
		this.mDelay = Value.clamp( delay, MIN_DELAY, MAX_DELAY );
	}

	@Override
	public byte[] getByteRepresentation()
	{
		byte[] macro = new byte[ getLength() ];
		macro[0] = getCommandID();
		macro[1] = (byte) ( this.mDelay >> 8 );
		macro[2] = (byte) ( this.mDelay & 0xFF );

		return macro;
	}
}