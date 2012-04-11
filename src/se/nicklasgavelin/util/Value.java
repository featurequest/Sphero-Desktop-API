package se.nicklasgavelin.util;

/**
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class Value
{
	public static double clamp( double value, double min, double max )
	{
		return (double) ( Value.clamp( (float) value, (float) min, (float) max ) );
	}

	public static float clamp( float value, float min, float max )
	{
		if( value > max )
			return max;
		if( value < min )
			return min;
		return value;
	}

	public static int clamp( int value, int min, int max )
	{
		return (int) ( Value.clamp( (double) value, (double) min, (double) max ) );
	}

	public static double window( double value, double windowValue, double delta )
	{
		if( ( Math.abs( value ) > Math.abs( windowValue ) - delta ) && ( Math.abs( value ) < Math.abs( windowValue ) + delta ) )
		{
			return windowValue;
		}
		return value;
	}
}