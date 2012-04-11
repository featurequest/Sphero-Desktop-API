package se.nicklasgavelin.util;

/**
 * @author Orbotix
 */
public class Vector3D
{
	public double x, y, z;

	public static double magnitude( double x, double y, double z )
	{
		return Math.sqrt( x * x + y * y + z * z );
	}

	public Vector3D( double x, double y, double z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double magnitude()
	{
		return Math.sqrt( this.x * this.x + this.y * this.y + this.z * this.z );
	}
}