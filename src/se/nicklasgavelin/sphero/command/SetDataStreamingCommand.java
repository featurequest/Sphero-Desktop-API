package se.nicklasgavelin.sphero.command;

/**
 * Set data streaming to received data from the Sphero device.
 * For masks perform bitwise or to get multiple sensor values.
 * 
 * Masks are found under the class SetDataStreamingCommand.DATA_STREAMING_MASKS
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class SetDataStreamingCommand extends CommandMessage
{
	// Internal storage
	private int mDivisor, mPacketFrames, mSensorMask, mSensorMask2, mPacketCount;

	/**
	 * Create a data streaming command without first setting the mask.
	 * Call the .addMask to add more masks to the packet. If no call to
	 * .addMask is performed before this packet is sent the mask that is set
	 * is the OFF mask (that will turn a current streaming off)
	 * 
	 * @param mDivisor Divisor to divide the default sampling rate of 400
	 *            Hz
	 * @param mPacketFrames Number of frames per packet
	 * @param mPacketCount Number of packets to receive from the moment data is
	 *            started being captured
	 */
	public SetDataStreamingCommand( int mDivisor, int mPacketFrames, int mPacketCount )
	{
		this( mDivisor, mPacketFrames, 0, mPacketCount );
	}

	/**
	 * Create a data streaming command
	 * 
	 * @param mDivisor Divisor to divide the default sampling rate of 400
	 *            Hz
	 * @param mPacketFrames Number of frames per packet
	 * @param mSensorMask Sensor mask for, bitwise or for multiple sensor
	 *            values
	 * @param mPacketCount Number of packets to receive from the moment data is
	 *            started being captured
	 */
	public SetDataStreamingCommand( int mDivisor, int mPacketFrames, int mSensorMask, int mPacketCount )
	{
		super( COMMAND_MESSAGE_TYPE.SET_DATA_STREAMING );

		// Set internal variables
		this.mDivisor = mDivisor;
		this.mPacketFrames = mPacketFrames;
		this.mSensorMask = mSensorMask;
		this.mPacketCount = mPacketCount;
	}

	/**
	 * Returns the internal packet count value
	 * 
	 * @return Packet count value
	 */
	public int getPacketCount()
	{
		return this.mPacketCount;
	}

	/**
	 * Returns the internal divisor value
	 * 
	 * @return Divisor value
	 */
	public int getDivisor()
	{
		return this.mDivisor;
	}

	/**
	 * Returns the internal packet frames value
	 * 
	 * @return Packet frames value
	 */
	public int getPacketFrames()
	{
		return this.mPacketFrames;
	}

	/**
	 * Add mask to the already existing one
	 * 
	 * @param mask The mask to add
	 */
	public void addMask( int mask )
	{
		this.mSensorMask |= mask;
	}

	/**
	 * Add mask to the already existing mask2
	 * 
	 * @param mask The mask to add
	 */
	public void addMask2( int mask )
	{
		this.mSensorMask2 |= mask;
	}

	/**
	 * Returns the internal sensor mask value
	 * 
	 * @return The sensor mask value
	 */
	public int getMask()
	{
		return this.mSensorMask;
	}

	/**
	 * Returns the internal sensor mask2 value
	 * 
	 * @return The sensor mask2 value
	 */
	public int getMask2()
	{
		return this.mSensorMask2;
	}

	@Override
	protected byte[] getPacketData()
	{
		byte[] data = new byte[ 13 ];

		data[0] = (byte) ( this.mDivisor >> 8 );
		data[1] = (byte) this.mDivisor;

		data[2] = (byte) ( this.mPacketFrames >> 8 );
		data[3] = (byte) this.mPacketFrames;

		data[4] = (byte) ( this.mSensorMask >> 24 );
		data[5] = (byte) ( this.mSensorMask >> 16 );
		data[6] = (byte) ( this.mSensorMask >> 8 );
		data[7] = (byte) this.mSensorMask;

		data[8] = (byte) this.mPacketCount;

		data[9] = (byte) ( this.mSensorMask2 >> 24 );
		data[10] = (byte) ( this.mSensorMask2 >> 16 );
		data[11] = (byte) ( this.mSensorMask2 >> 8 );
		data[12] = (byte) this.mSensorMask2;

		return data;
	}

	/* ******************
	 * INNER CLASSES
	 */

	/**
	 * Mask values for the data streaming
	 */
	public static final class DATA_STREAMING_MASKS
	{
		public static final int OFF = 0;

		/**
		 * Motor masks
		 */
		public static final class MOTOR_BACK_EMF
		{
			/**
			 * Both left and right filtered/raw
			 */
			public static final class ALL
			{
				public static final int FILTERED = LEFT.FILTERED | RIGHT.FILTERED,
						RAW = LEFT.RAW | RIGHT.RAW;
			}

			/**
			 * Left motor masks
			 */
			public static final class LEFT
			{
				public static final int FILTERED = 32, RAW = 2097152;
			}

			/**
			 * Right motor mask
			 */
			public static final class RIGHT
			{
				public static final int FILTERED = 64, RAW = 4194304;
			}
		}

		/**
		 * Magnetometer masks
		 */
		public static final class MAGNETOMETER
		{
			/**
			 * All axis, filtered/raw
			 */
			public static final class ALL
			{
				public static final int FILTERED = X.FILTERED | Y.FILTERED | Z.FILTERED,
						RAW = X.RAW | Y.RAW | Z.RAW;
			}

			/**
			 * X-axis
			 */
			public static final class X
			{
				public static final int FILTERED = 512, RAW = 33554432;
			}

			/**
			 * Y-axis
			 */
			public static final class Y
			{
				public static final int FILTERED = 256, RAW = 16777216;
			}

			/**
			 * Z-axis
			 */
			public static final class Z
			{
				public static final int FILTERED = 128, RAW = 8388608;
			}
		}

		/**
		 * Accelerometer masks
		 */
		public static final class ACCELEROMETER
		{
			/**
			 * All axis filtered/raw
			 */
			public static final class ALL
			{
				public static final int FILTERED = ( X.FILTERED | Y.FILTERED | Z.FILTERED ),
						RAW = ( X.RAW | Y.RAW | Z.RAW );
			}

			/**
			 * X-axis filtered/raw
			 */
			public static final class X
			{
				public static final int FILTERED = 32768, RAW = -2147483648;
			}

			/**
			 * Y-axis filtered/raw
			 */
			public static final class Y
			{
				public static final int FILTERED = 16384, RAW = 1073741824;
			}

			/**
			 * Z-axis filtered/raw
			 */
			public static final class Z
			{
				public static final int FILTERED = 8192, RAW = 536870912;
			}
		}

		/**
		 * Gyro masks
		 */
		public static final class GYRO
		{
			/**
			 * All axis filtered/raw
			 */
			public static final class ALL
			{
				public static final int FILTERED = X.FILTERED | Y.FILTERED | Z.FILTERED,
						RAW = X.RAW | Y.RAW | Z.RAW;
			}

			/**
			 * Gyro X-axis value masks
			 */
			public static final class X
			{
				public static final int FILTERED = 4096, RAW = 268435456;
			}

			/**
			 * Gyro Y-axis value masks
			 */
			public static final class Y
			{
				public static final int FILTERED = 2048, RAW = 134217728;
			}

			/**
			 * Gyro Z-axis value masks
			 */
			public static final class Z
			{
				public static final int FILTERED = 1024, RAW = 67108864;
			}
		}

		/**
		 * IMU masks
		 */
		public static final class IMU
		{
			/**
			 * All settings filtered/raw
			 */
			public static final class ALL
			{
				public static final int FILTERED = YAW.FILTERED | ROLL.FILTERED | PITCH.FILTERED;
			}

			/**
			 * IMU yaw value masks
			 */
			public static final class YAW
			{
				public static final int FILTERED = 65536;
			}

			public static final class ROLL
			{
				public static final int FILTERED = 131072;
			}

			public static final class PITCH
			{
				public static final int FILTERED = 262144;
			}
		}
	}
}
