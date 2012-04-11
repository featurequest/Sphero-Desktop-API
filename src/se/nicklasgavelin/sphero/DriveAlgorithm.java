package se.nicklasgavelin.sphero;

/**
 * Base abstract class for algorithms that are used to calculate heading, speed
 * and any other necessary parameter for driving the Sphero robot directly using
 * the Robot.drive method.
 * 
 * Directly copied from Orbotix code
 * 
 * @author Orbotix
 */
public abstract class DriveAlgorithm
{
	// Internal storage
	/**
	 * Number of maximum coordinates
	 */
	public static final int MAX_COORDINATES = 3;
	public double heading;
	public double headingOffset;
	public double adjustedHeading;
	public double speed;
	public double speedScale;
	public double[] stopPosition;
	public double[] deadZoneDelta;
	protected OnConvertListener convertListener;

	/**
	 * Create a drive algorithm and set default values
	 */
	public DriveAlgorithm()
	{
		this.headingOffset = 0.0D;
		this.speedScale = 1.0D;
		this.stopPosition = new double[ 3 ];
		this.deadZoneDelta = new double[ 3 ];
	}

	/**
	 * Set the listener for the conversion
	 * 
	 * @param listener The listener
	 */
	public void setOnConvertListener( OnConvertListener listener )
	{
		this.convertListener = listener;
	}

	/**
	 * Convert x, y, z values into heading and speed values
	 * 
	 * @param x The x coordinate
	 * @param y The y coordinate
	 * @param z The z coordinate
	 */
	public abstract void convert( double x, double y, double z );

	/**
	 * Notify the listener about a conversion success
	 */
	protected void postOnConvert()
	{
		if( this.convertListener == null )
			return;
		this.convertListener.onConvert( this.heading, this.speed, this.speedScale );
	}

	/**
	 * Modify to the correct heading (offset and maximum)
	 */
	protected void adjustHeading()
	{
		this.adjustedHeading = ( this.heading + this.headingOffset );
		if( this.adjustedHeading >= 360.0D )
			this.adjustedHeading %= 360.0D;
	}

	/**
	 * Listener class,
	 * listens for conversion events
	 */
	public static abstract interface OnConvertListener
	{
		/**
		 * Event called when the conversion of the values are done
		 * 
		 * @param paramDouble1 x
		 * @param paramDouble2 y
		 * @param paramDouble3 z
		 */
		public abstract void onConvert( double paramDouble1, double paramDouble2, double paramDouble3 );
	}
}