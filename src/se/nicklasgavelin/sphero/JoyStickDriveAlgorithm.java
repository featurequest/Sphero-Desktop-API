package se.nicklasgavelin.sphero;

import se.nicklasgavelin.util.Value;

/**
 * Directly copied from Orbotix code
 * 
 * @author Orbotix
 */
public class JoyStickDriveAlgorithm extends DriveAlgorithm
{
	private final double center_x;
	private final double center_y;

	public JoyStickDriveAlgorithm( double padWidth, double padHeight )
	{
		this.center_x = ( padWidth / 2.0D );
		this.center_y = ( padHeight / 2.0D );
	}

	@Override
	public void convert( double x, double y, double unused )
	{
		double x_length = x - this.center_x;
		double y_length = this.center_y - y;

		if( this.center_x > this.center_y )
			x_length *= this.center_y / this.center_x;
		else if( this.center_x < this.center_y )
			y_length *= this.center_x / this.center_y;

		if( ( this.center_x > 0.0D ) && ( this.center_y > 0.0D ) )
		{
			this.speed = ( Math.sqrt( x_length * x_length + y_length * y_length ) / Math.min( this.center_x, this.center_y ) );
			this.speed = ( Value.clamp( this.speed, 0.0D, 1.0D ) * this.speedScale );
		}
		else
			this.speed = 0.0D;

		this.heading = Math.atan2( x_length, y_length );
		if( this.heading < 0.0D )
			this.heading += 6.283185307179586D;

		this.heading *= 57.295779513082323D;

		postOnConvert();
	}
}