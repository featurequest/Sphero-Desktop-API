/*
 * Please read the LICENSE file that is included with the source
 * code.
 */

package se.nicklasgavelin.util;

/**
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class Color
{
	public static java.awt.Color fromHex( String hexValue )
	{
		String v;

		// Check which type is used for the hex value
		if( hexValue.startsWith( "0x" ) )
			v = hexValue.substring( 2 );
		else if( hexValue.startsWith( "#" ) )
			v = hexValue.substring( 1 );
		else
			v = hexValue;

		// Check if the hex value is correct length
		if( v.length() != 6 )
			return null;

		// Start conversion
		int i = Integer.valueOf( v.toUpperCase(), 16 );
		int r = ( i >> 16 ) & 0xFF;
		int g = ( i >> 8 ) & 0xFF;
		int b = i & 0xFF;

		System.out.println( r + ", " + g + ", " + b );

		return new java.awt.Color( r, g, b );
	}
}
