/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class FrontLED extends MacroCommand
{
	private float brightness;
	public static final float MIN_BRIGHTNESS = 0, MAX_BRIGHTNESS = 1.0F;

	/**
	 * Create a front LED macro command with a specified brightness
	 * 
	 * @param _brightness The brightness for the LED (MIN_BRIGHTNESS - MAX_BRIGHTNESS)
	 */
	public FrontLED( float _brightness )
	{
		super( MACRO_COMMAND.MAC_FRONT_LED );
		this.setBrightness( _brightness );
	}

	/**
	 * Update the internal brightness value
	 * 
	 * @param _brightness The internal brightness value (MIN_BRIGHTNESS - MAX_BRIGHTNESS)
	 */
	public void setBrightness( float _brightness )
	{
		this.brightness = Value.clamp( _brightness, MIN_BRIGHTNESS, MAX_BRIGHTNESS );
	}

	@Override
	public byte[] getByteRepresentation()
	{
		ByteArrayBuffer bab = new ByteArrayBuffer( getLength() );
		bab.append( getCommandID() );
		bab.append( (int) ( this.brightness * 255.0F ) );

		return bab.toByteArray();
	}
}
