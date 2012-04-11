package se.nicklasgavelin.util;

/**
 * @author Orbotix
 */
public class Vector2D
{
	public double x;
	public double y;

	public static double magnitude( double x, double y )
	{
		return Math.sqrt( x * x + y * y );
	}

	public Vector2D( double x, double y )
	{
		this.x = x;
		this.y = y;
	}

	public double magnitude()
	{
		return Math.sqrt( this.x * this.x + this.y * this.y );
	}
}