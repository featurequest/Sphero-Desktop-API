/*
 * Please read the LICENSE file that is included with the source
 * code.
 */

package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.Value;

/**
 * Example usage (Robot r):
 * 
 * MacroObject mo = new MacroObject();
 * mo.setMode( MacroObject.MacroObjectMode.CachedStreaming );
 * 
 * // Send fade (500 ms fade hold for 1 second)
 * mo.addCommand( new Fade( 255, 0, 0, 500 ) );
 * mo.addCommand( new Delay( 1000 ) );
 * 
 * // Send fade (500 ms fade hold for 1 second)
 * mo.addCommand( new Fade( 0, 255, 0, 500 ) );
 * mo.addCommand( new Delay( 1000 ) );
 * 
 * // Send fade (500 ms fade hold for 1 second)
 * mo.addCommand( new Fade( 0, 0, 255, 500 ) );
 * mo.addCommand( new Delay( 1000 ) );
 * 
 * r.sendCommand( mo );
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class Fade extends MacroCommand
{
	private int r, g, b;
	private int delay;
	public static final int MIN_COLOR_VALUE = 0, MAX_COLOR_VALUE = 255,
			MIN_DELAY_VALUE = 0, MAX_DELAY_VALUE = 65534;

	public Fade( int r, int g, int b, int delay )
	{
		super( MACRO_COMMAND.MAC_FADE );
		this.r = Value.clamp( r, MIN_COLOR_VALUE, MAX_COLOR_VALUE );
		this.g = Value.clamp( g, MIN_COLOR_VALUE, MAX_COLOR_VALUE );
		this.b = Value.clamp( b, MIN_COLOR_VALUE, MAX_COLOR_VALUE );

		this.delay = Value.clamp( delay, MIN_DELAY_VALUE, MAX_DELAY_VALUE );
	}

	@Override
	public byte[] getByteRepresentation()
	{
		byte[] data = new byte[ getLength() ];

		data[0] = getCommandID();

		data[1] = (byte) this.r;
		data[2] = (byte) this.g;
		data[3] = (byte) this.b;

		data[4] = (byte) ( this.delay >> 8 );
		data[5] = (byte) ( this.delay & 0xFF );

		return data;
	}
}
