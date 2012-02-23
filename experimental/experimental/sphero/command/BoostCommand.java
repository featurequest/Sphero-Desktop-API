package experimental.sphero.command;

import se.nicklasgavelin.util.Value;

/**
 *
 * @deprecated Experimental
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class BoostCommand extends se.nicklasgavelin.sphero.command.DeviceCommand
{
    private float heading;
    private int time;


    /**
     * Create a boost command with a given duration and a given heading
     *
     * @param duration Amount of time to boost (0-255)
     * @param heading  The heading to boost in
     */
    public BoostCommand( int duration, float heading )
    {
        super( DEVICE_COMMAND.BOOST );// SpheroCommandBoost, SpheroDeviceId );
        this.time = Value.clamp( duration, 0, 255 );
        this.heading = (( int ) heading % 360);
    }


    public float getHeading()
    {
        return this.heading;
    }


    public float getTime()
    {
        return this.time;
    }


    @Override
    protected byte[] getPacketData()
    {
        byte[] data = new byte[ 3 ];

        data[0] = ( byte ) (this.time);
        data[1] = ( byte ) (( int ) this.heading >> 8);
        data[2] = ( byte ) ( int ) this.heading;

        return data;
    }
}
