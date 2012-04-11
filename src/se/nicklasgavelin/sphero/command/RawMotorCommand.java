package se.nicklasgavelin.sphero.command;

/**
 * Command to steer the direction and speed of the internal motors of the
 * Sphero.
 * 
 * NOTICE: Sending this command will result in some future commands to fail
 * to execute on the Sphero for some reason. Has something to do with the logic
 * on the Sphero
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RawMotorCommand extends CommandMessage
{
	/**
	 * Possible motor modes
	 * 
	 * @author Nicklas Gavelin
	 */
	public static enum MOTOR_MODE
	{
		FORWARD( 1 ), REVERSE( 2 );
		private int mode;

		private MOTOR_MODE( int mode )
		{
			this.mode = mode;
		}

		/**
		 * Return the integer value representation of the motor mode
		 * 
		 * @return The integer representation
		 */
		public int getValue()
		{
			return this.mode;
		}

		/**
		 * Create a motor mode from an integer value
		 * 
		 * @param mode The integer value representation of the motor mode
		 * 
		 * @return A motor mode or null if no motor mode could be found for the
		 *         value given
		 */
		public static MOTOR_MODE valueOf( int mode )
		{
			switch ( mode )
			{
				case 1:
					return FORWARD;
				case 2:
					return REVERSE;
			}

			return null;
		}
	}

	// Internal objects
	private MOTOR_MODE leftMode, rightMode;
	private int leftSpeed, rightSpeed;

	/**
	 * Create a raw motor command with given directions for the motors and given
	 * speeds
	 * 
	 * @param leftMode The left mode
	 * @param leftSpeed The left speed (0-255)
	 * @param rightMode The right mode
	 * @param rightSpeed The right speed (0-255)
	 */
	public RawMotorCommand( MOTOR_MODE leftMode, int leftSpeed, MOTOR_MODE rightMode, int rightSpeed )
	{
		super( COMMAND_MESSAGE_TYPE.RAW_MOTOR );

		this.leftMode = leftMode;
		this.rightMode = rightMode;
		this.leftSpeed = leftSpeed;
		this.rightSpeed = rightSpeed;
	}

	/**
	 * Returns the left motor mode
	 * 
	 * @return The left motor mode
	 */
	public MOTOR_MODE getLeftMode()
	{
		return this.leftMode;
	}

	/**
	 * Returns the right motor mode
	 * 
	 * @return The right motor mode
	 */
	public MOTOR_MODE getRightMode()
	{
		return this.rightMode;
	}

	/**
	 * Returns the left motor speed
	 * 
	 * @return The left motor speed
	 */
	public int getLeftSpeed()
	{
		return this.leftSpeed;
	}

	/**
	 * Returns the right motor speed
	 * 
	 * @return The right motor speed
	 */
	public int getRightSpeed()
	{
		return this.rightSpeed;
	}

	@Override
	protected byte[] getPacketData()
	{
		byte[] data = new byte[ 4 ];

		data[0] = (byte) this.leftMode.getValue();
		data[1] = (byte) this.leftSpeed;
		data[2] = (byte) this.rightMode.getValue();
		data[3] = (byte) this.rightSpeed;

		return data;
	}
}
