package se.nicklasgavelin.sphero.command;

/**
 * @deprecated Doesn't do anything atm
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SetDataStreamingCommand extends CommandMessage
{
    public static final int DATA_STREAMING_MASK_OFF = 0;
    public static final int DATA_STREAMING_MASK_LEFT_MOTOR_BACK_EMF_FILTERED = 32;
    public static final int DATA_STREAMING_MASK_RIGHT_MOTOR_BACK_EMF_FILTERED = 64;
    public static final int DATA_STREAMING_MASK_MAGNETOMETER_Z_FILTERED = 128;
    public static final int DATA_STREAMING_MASK_MAGNETOMETER_Y_FILTERED = 256;
    public static final int DATA_STREAMING_MASK_MAGNETOMETER_X_FILTERED = 512;
    public static final int DATA_STREAMING_MASK_GYRO_Z_FILTERED = 1024;
    public static final int DATA_STREAMING_MASK_GYRO_Y_FILTERED = 2048;
    public static final int DATA_STREAMING_MASK_GYRO_X_FILTERED = 4096;
    public static final int DATA_STREAMING_MASK_ACCELEROMETER_Z_FILTERED = 8192;
    public static final int DATA_STREAMING_MASK_ACCELEROMETER_Y_FILTERED = 16384;
    public static final int DATA_STREAMING_MASK_ACCELEROMETER_X_FILTERED = 32768;
    public static final int DATA_STREAMING_MASK_IMU_YAW_ANGLE_FILTERED = 65536;
    public static final int DATA_STREAMING_MASK_IMU_ROLL_ANGLE_FILTERED = 131072;
    public static final int DATA_STREAMING_MASK_IMU_PITCH_ANGLE_FILTERED = 262144;
    public static final int DATA_STREAMING_MASK_LEFT_MOTOR_BACK_EMF_RAW = 2097152;
    public static final int DATA_STREAMING_MASK_RIGHT_MOTOR_BACK_EMF_RAW = 4194304;
    public static final int DATA_STREAMING_MASK_MAGNETOMETER_Z_RAW = 8388608;
    public static final int DATA_STREAMING_MASK_MAGNETOMETER_Y_RAW = 16777216;
    public static final int DATA_STREAMING_MASK_MAGNETOMETER_X_RAW = 33554432;
    public static final int DATA_STREAMING_MASK_GYRO_Z_RAW = 67108864;
    public static final int DATA_STREAMING_MASK_GYRO_Y_RAW = 134217728;
    public static final int DATA_STREAMING_MASK_GYRO_X_RAW = 268435456;
    public static final int DATA_STREAMING_MASK_ACCELEROMETER_Z_RAW = 536870912;
    public static final int DATA_STREAMING_MASK_ACCELEROMETER_Y_RAW = 1073741824;
    public static final int DATA_STREAMING_MASK_ACCELEROMETER_X_RAW = -2147483648;
    private int mDivisor, mPacketFrames, mSensorMask, mPacketCount;


    /**
     * Create a data streaming command
     *
     * @param mDivisor      Divisor to divice the default sampling rate of 400
     *                      Hz
     * @param mPacketFrames Number of packet frames
     * @param mSensorMask   Sensor mask for, bitwise or
     * @param mPacketCount  Number of packets to receive
     */
    public SetDataStreamingCommand( int mDivisor, int mPacketFrames, int mSensorMask, int mPacketCount )
    {
        super( COMMAND_MESSAGE_TYPE.SET_DATA_STREAMING );

        // Set internal variables
        this.mDivisor = mDivisor;
        this.mPacketCount = mPacketCount;
        this.mSensorMask = mSensorMask;
        this.mPacketCount = mPacketCount;
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
}
