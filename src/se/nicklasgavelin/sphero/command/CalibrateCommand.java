package se.nicklasgavelin.sphero.command;

/**
 * A calibration command for the Sphero robot.
 * Will calibrate the heading the robot is facing towards to
 * make the roll command work properly
 *
 * @author Orbotix
 * @author Nicklas Gavelin
 */
public class CalibrateCommand extends DeviceCommand
{
    private float heading;


    /**
     * Create a calibrate command with a given heading
     *
     * @param heading The heading for the calibrate command
     */
    public CalibrateCommand( float heading )
    {
        super( DEVICE_COMMAND.CALIBRATE );
        this.heading = heading;
    }


    /**
     * Returns the heading set in the command
     *
     * @return The heading
     */
    public float getHeading()
    {
        return this.heading;
    }


    @Override
    protected byte[] getPacketData()
    {
        byte[] data = new byte[ 2 ];
        data[0] = ( byte ) (( int ) this.heading >> 8);
        data[1] = ( byte ) ( int ) this.heading;

        return data;
    }
}
