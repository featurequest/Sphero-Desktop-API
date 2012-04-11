package se.nicklasgavelin.util;

public class SensorLowPassFilter extends SensorFilter
{
	private static final float accelerometerMinStep = 0.02F;
	private static final float accelerometerNoiseAttenuation = 3.0F;
	private float filterConstant;

	public SensorLowPassFilter( float rate, float cutoffFrequency )
	{
		float dt = 1.0F / rate;
		float RC = 1.0F / cutoffFrequency;
		this.filterConstant = ( dt / ( dt + RC ) );
	}

	@Override
	public void addDatum( double x, double y, double z )
	{
		double alpha = this.filterConstant;

		if( this.adaptive )
		{
			float d = (float) Value.clamp( Math.abs( Vector3D.magnitude( this.x, this.y, this.z ) - Vector3D.magnitude( x, y, z ) ) / accelerometerMinStep - 1.0D, 0.0D, 1.0D );

			alpha = ( 1.0F - d ) * this.filterConstant / accelerometerNoiseAttenuation + d * this.filterConstant;
		}

		double filtered_x = this.x;
		double filtered_y = this.y;
		double filtered_z = this.z;
		filtered_x = x * alpha + filtered_x * ( 1.0D - alpha );
		filtered_y = y * alpha + filtered_y * ( 1.0D - alpha );
		filtered_z = z * alpha + filtered_z * ( 1.0D - alpha );
		this.x = filtered_x;
		this.y = filtered_y;
		this.z = filtered_z;
	}
}