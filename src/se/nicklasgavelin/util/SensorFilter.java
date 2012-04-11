package se.nicklasgavelin.util;

public class SensorFilter
{
	protected double x = 0.0D;
	protected double y = 0.0D;
	protected double z = 0.0D;
	protected boolean adaptive = false;

	public double getX()
	{
		return this.x;
	}

	public double getY()
	{
		return this.y;
	}

	public double getZ()
	{
		return this.z;
	}

	public void addDatum( double x, double y, double z )
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public float[] getValues()
	{
		return new float[] { (float) this.x, (float) this.y, (float) this.z };
	}
}