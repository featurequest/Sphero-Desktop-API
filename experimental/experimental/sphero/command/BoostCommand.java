package experimental.sphero.command;

import se.nicklasgavelin.sphero.command.CommandMessage;
import se.nicklasgavelin.util.Value;

/**
 *
 * @deprecated Experimental
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class BoostCommand extends CommandMessage
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
        super( CommandMessage.COMMAND_MESSAGE_TYPE.BOOST); //DEVICE_COMMAND.BOOST );// SpheroCommandBoost, SpheroDeviceId );
        this.time = Value.clamp( duration, 0, 255 );
        this.heading = (( int ) heading % 360);
    }


    /**
     * Returns the internal heading value
     *
     * @return The internal heading value
     */
    public float getHeading()
    {
        return this.heading;
    }


    /**
     * Returns the internal duration value
     *
     * @return The internal duration value
     */
    public float getDuration()
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
