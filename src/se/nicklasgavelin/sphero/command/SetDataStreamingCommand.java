
package se.nicklasgavelin.sphero.command;

/**
 * Set data streaming to received data from the Sphero device.
 * For masks perform bitwise or to get multiple sensor values.
 *
 * Masks are found under the class SetDataStreamingCommand.DATA_STREAMING_MASKS
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class SetDataStreamingCommand extends CommandMessage
{
    // Internal storage
    private int mDivisor, mPacketFrames, mSensorMask, mPacketCount;


    /**
     * Create a data streaming command without first setting the mask.
     * Call the .addMask to add more masks to the packet. If no call to
     * .addMask is performed before this packet is sent the mask that is set
     * is the OFF mask (that will turn a current streaming off)
     *
     * @param mDivisor      Divisor to divide the default sampling rate of 400
     *                      Hz
     * @param mPacketFrames Number of frames per packet
     * @param mPacketCount  Number of packets to receive from the moment data is
     *                      started being captured
     */
    public SetDataStreamingCommand( int mDivisor, int mPacketFrames, int mPacketCount )
    {
        this( mDivisor, mPacketFrames, 0, mPacketCount );
    }


    /**
     * Create a data streaming command
     *
     * @param mDivisor      Divisor to divide the default sampling rate of 400
     * Hz
     * @param mPacketFrames Number of frames per packet
     * @param mSensorMask   Sensor mask for, bitwise or for multiple sensor
     *                      values
     * @param mPacketCount  Number of packets to receive from the moment data is
     *                      started being captured
     */
    public SetDataStreamingCommand( int mDivisor, int mPacketFrames, int mSensorMask, int mPacketCount )//int mDivisor, int mPacketFrames, int mSensorMask, int mPacketCount )
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
     * Returns the internal sensor mask value
     *
     * @return The sensor mask value
     */
    public int getMask()
    {
        return this.mSensorMask;
    }


    @Override
    protected byte[] getPacketData()
    {
        byte[] data = new byte[ 9 ];
        data[0] = ( byte ) (this.mDivisor >> 8);
        data[1] = ( byte ) this.mDivisor;
        data[2] = ( byte ) (this.mPacketFrames >> 8);
        data[3] = ( byte ) this.mPacketFrames;
        data[4] = ( byte ) (this.mSensorMask >> 24);
        data[5] = ( byte ) (this.mSensorMask >> 16);
        data[6] = ( byte ) (this.mSensorMask >> 8);
        data[7] = ( byte ) this.mSensorMask;
        data[8] = ( byte ) this.mPacketCount;

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
                public static final int FILTERED = LEFT.FILTERED | RIGHT.FILTERED, RAW = LEFT.RAW | RIGHT.RAW;
            }

            /**
             * Left motor masks
             */
            public static final class LEFT
            {
                public static final int FILTERED = 0x20, RAW = 0x200000;
            }


            /**
             * Right motor mask
             */
            public static final class RIGHT
            {
                public static final int FILTERED = 0x40, RAW = 0x400000;
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
                public static final int FILTERED = X.FILTERED | Y.FILTERED | Z.FILTERED, RAW = X.RAW | Y.RAW  | Z.RAW;
            }

            /**
             * X-axis
             */
            public static final class X
            {
                public static final int FILTERED = 0x200, RAW = 0x2000000;
            }


            /**
             * Y-axis
             */
            public static final class Y
            {
                public static final int FILTERED = 0x100, RAW = 0x1000000;
            }


            /**
             * Z-axis
             */
            public static final class Z
            {
                public static final int FILTERED = 0x80, RAW = 0x800000;
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
                public static final int FILTERED = X.FILTERED | Y.FILTERED  | Z.FILTERED, RAW = X.RAW | Y.RAW | Z.RAW;
            }

            /**
             * X-axis filtered/raw
             */
            public static final class X
            {
                public static final int FILTERED = 0x8000, RAW = 0x80000000;
            }


            /**
             * Y-axis filtered/raw
             */
            public static final class Y
            {
                public static final int FILTERED = 0x4000, RAW = 0x40000000;
            }


            /**
             * Z-axis filtered/raw
             */
            public static final class Z
            {
                public static final int FILTERED = 0x2000, RAW = 0x20000000;
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
                public static final int FILTERED = X.FILTERED | Y.FILTERED  | Z.FILTERED, RAW = X.RAW | Y.RAW | Z.RAW;
            }

            /**
             * Gyro X-axis value masks
             */
            public static final class X
            {
                public static final int FILTERED = 0x1000, RAW = 0x10000000;
            }

            /**
             * Gyro Y-axis value masks
             */
            public static final class Y
            {
                public static final int FILTERED = 0x800, RAW = 0x8000000;
            }

            /**
             * Gyro Z-axis value masks
             */
            public static final class Z
            {
                public static final int FILTERED = 0x400, RAW = 0x4000000;
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
                public static final int FILTERED = YAW.FILTERED | ROLL.FILTERED  | PITCH.FILTERED;
            }

            /**
             * IMU yaw value masks
             */
            public static final class YAW
            {
                public static final int FILTERED = 0x10000;
            }


            public static final class ROLL
            {
                public static final int FILTERED = 0x20000;
            }

            public static final class PITCH
            {
                public static final int FILTERED = 0x40000;
            }
        }
    }
}
