package se.nicklasgavelin.sphero;

import se.nicklasgavelin.util.Value;

/**
 * RC Drive algorithm used for driving the Sphero robot.
 * Directly copied from Orbotix code.
 * 
 * @author Orbotix
 */
public class RCDriveAlgorithm extends DriveAlgorithm
{
	private static final double MAX_TURN_RATE = 30.0D;

	@Override
	public void convert( double x, double y, double z )
	{
		y = Value.clamp( y, -1.0D, 1.0D );
		double curHeading = this.heading;
		curHeading += y * MAX_TURN_RATE;

		// Check heading min
		if( curHeading < 0.0D )
			curHeading += 360.0D;

		// Check heading max
		if( curHeading >= 360.0D )
			curHeading -= 360.0D;

		// Update heading
		this.heading = curHeading;

		// Update x value
		x = Value.clamp( x, 0.0D, 1.0D );

		// Update current speed
		this.speed = ( this.speedScale * ( x * x ) );

		// Notify any listeners
		postOnConvert();
	}
}