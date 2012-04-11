package se.nicklasgavelin.sphero;

import se.nicklasgavelin.util.Value;
import se.nicklasgavelin.util.Vector2D;

/**
 * Directly copied from Orbotix code
 * 
 * @author Orbotix
 */
public class TiltDriveAlgorithm extends DriveAlgorithm
{
	public static final double MAXIMUM_TILT = 0.4363323129985824D;

	@Override
	public void convert( double x, double y, double z )
	{
		x /= 9.810000000000001D;
		y /= 9.810000000000001D;
		z /= 9.810000000000001D;

		x = Math.cos( this.stopPosition[0] ) * x - Math.sin( this.stopPosition[1] ) * z;

		x = Value.clamp( x, -1.0D, 1.0D );
		y = Value.clamp( y, -1.0D, 1.0D );

		x = Value.window( x, 0.0D, this.deadZoneDelta[0] );
		y = Value.window( y, 0.0D, this.deadZoneDelta[1] );

		double pitch = 0.0D;
		if( ( Math.abs( x ) > 0.0D ) || ( Math.abs( y ) > 0.0D ) )
		{
			double arg = ( x * x + y * y ) / Vector2D.magnitude( x, y );
			arg = Value.clamp( arg, 0.0D, 1.0D );
			pitch = 1.570796326794897D - Math.acos( arg );
		}

		pitch = Value.clamp( pitch, 0.0D, 0.4363323129985824D );

		this.speed = ( this.speedScale * pitch / 0.4363323129985824D );

		if( this.speed == 0.0D )
			this.heading = 0.0D;
		else
			this.heading = Math.atan2( y, -x );

		if( this.heading < 0.0D )
			this.heading += 6.283185307179586D;

		this.heading *= 57.295779513082323D;
		postOnConvert();
	}
}