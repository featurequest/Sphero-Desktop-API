package se.nicklasgavelin.sphero.macro.command;

import java.awt.Color;
import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RGBSD2 extends MacroCommand
{
	private int red, green, blue;
	public static final int MIN_COLOR = 0, MAX_COLOR = 255;

	// public RGBSD2( byte[] data )
	// {
	// }

	/**
	 * Create a RGB SD2 macro command with the set values
	 * 
	 * @param _red The red value
	 * @param _green The green value
	 * @param _blue The blue value
	 */
	public RGBSD2( int _red, int _green, int _blue )
	{
		super( MACRO_COMMAND.MAC_RGB_SD2 );
		this.setColor( _red, _green, _blue );
	}

	/**
	 * Create a RGB SD2 macro command with a given color value
	 * 
	 * @param c The color value
	 */
	public RGBSD2( Color c )
	{
		this( c.getRed(), c.getGreen(), c.getBlue() );
	}

	/**
	 * Returns the RGB color value
	 * 
	 * @return The RGB color value
	 */
	public int[] getColorValues()
	{
		return new int[] { this.red, this.green, this.blue };
	}

	/**
	 * Returns the RGB color
	 * 
	 * @return The RGB color
	 */
	public Color getColor()
	{
		return new Color( this.red, this.green, this.blue );
	}

	/**
	 * Update the internal color value
	 * 
	 * @param c The new color value
	 */
	public void setColor( Color c )
	{
		this.setColor( c.getRed(), c.getGreen(), c.getBlue() );
	}

	/**
	 * Update the internal color value
	 * 
	 * @param _red The new red value
	 * @param _green The new green value
	 * @param _blue The new blue value
	 */
	public void setColor( int _red, int _green, int _blue )
	{
		this.red = Value.clamp( _red, MIN_COLOR, MAX_COLOR );
		this.green = Value.clamp( _green, MIN_COLOR, MAX_COLOR );
		this.blue = Value.clamp( _blue, MIN_COLOR, MAX_COLOR );
		// if ( (_red.intValue() >= 0) && (_red.intValue() <= 255) )
		// {
		// this.red = _red;
		// }
		// if ( (_blue.intValue() >= 0) && (_blue.intValue() <= 255) )
		// {
		// this.blue = _blue;
		// }
		// if ( (_green.intValue() >= 0) && (_green.intValue() <= 255) )
		// this.green = _green;
	}

	@Override
	public byte[] getByteRepresentation()
	{
		ByteArrayBuffer bytes = new ByteArrayBuffer( getLength() );
		bytes.append( getCommandID() );
		bytes.append( this.red );
		bytes.append( this.green );
		bytes.append( this.blue );

		return bytes.toByteArray();
	}
}