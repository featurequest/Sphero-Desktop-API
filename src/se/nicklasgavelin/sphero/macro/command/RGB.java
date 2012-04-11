package se.nicklasgavelin.sphero.macro.command;

import java.awt.Color;
import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * RGB Macro command
 * 
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RGB extends MacroCommand
{
	private int delay;// = Integer.valueOf( 0 );
	private int red;// = Integer.valueOf( 255 );
	private int green;// = Integer.valueOf( 255 );
	private int blue;// = Integer.valueOf( 255 );
	public static final int MIN_DELAY = 0, MAX_DELAY = 255, MIN_COLOR = 0,
			MAX_COLOR = 255;

	// public RGB( byte[] data )
	// {
	// }
	/**
	 * Create a RGB macro command with a given delay and color
	 * 
	 * @param c The color for the command
	 * @param _delay The delay before the command runs (MIN_DELAY - MAX_DELAY)
	 */
	public RGB( Color c, int _delay )
	{
		this( c.getRed(), c.getGreen(), c.getBlue(), _delay );

	}

	/**
	 * Create a RGB macro command with a given delay and color
	 * 
	 * @param _red 0-255 red color value
	 * @param _green 0-255 green color value
	 * @param _blue 0-255 blue color value
	 * @param _delay The delay before the command runs (MIN_DELAY - MAX_DELAY)
	 */
	public RGB( Integer _red, Integer _green, Integer _blue, Integer _delay )
	{
		super( MACRO_COMMAND.MAC_RGB );
		this.setColor( _red, _green, _blue );
		this.setDelay( _delay );
	}

	/**
	 * Returns the internal delay value
	 * 
	 * @return The internal delay value
	 */
	public int getDelay()
	{
		return this.delay;
	}

	/**
	 * Update the internal delay value
	 * 
	 * @param _delay The new delay value (MIN_DELAY - MAX_DELAY)
	 */
	public void setDelay( Integer _delay )
	{
		this.delay = Value.clamp( _delay, MIN_DELAY, MAX_DELAY );
		// if ( (_delay.intValue() >= 0) && (_delay.intValue() <= 255) )
		// this.delay = _delay;
	}

	/**
	 * Returns the colors set for the command as a java.awt.Color object
	 * 
	 * @return The internal color value
	 */
	public Color getColor()
	{
		/*
		 * Integer[] color = new Integer[ 3 ];
		 * color[1] = this.red;
		 * color[2] = this.green;
		 * color[3] = this.blue;
		 * 
		 * return color;
		 */
		return new Color( this.red, this.green, this.blue );
	}

	public int[] getColorValues()
	{
		return new int[] { this.red, this.green, this.blue };
	}

	public void setColor( Integer _red, Integer _green, Integer _blue )
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
		bytes.append( this.delay );

		return bytes.toByteArray();
	}
}