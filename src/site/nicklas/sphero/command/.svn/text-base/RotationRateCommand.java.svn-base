package site.nicklas.sphero.command;

import site.nicklas.sphero.util.Value;

/**
 * Command for setting the Sphero rotation rate. I've seen no visible result
 * after sending this command.
 *
 * @author Nicklas Gavelin
 */
public class RotationRateCommand extends DeviceCommand
{
    private float rate;


    /**
     * Create a rotation rate command.
     *
     * @param rate Rotation rate between 0-1
     */
    public RotationRateCommand( float rate )
    {
        super( DEVICE_COMMAND.ROTATION_RATE );
        this.rate = ( float ) Value.clamp( rate, 0.0D, 1.0D );
    }


    /**
     * Returns the set rotation rate
     *
     * @return The set rotation rate
     */
    public float getRate()
    {
        return this.rate;
    }


    @Override
    protected byte[] getPacketData()
    {
        byte[] data = new byte[ 1 ];
        data[0] = ( byte ) ( int ) (this.rate * 255.0D);

        return data;
    }
}
