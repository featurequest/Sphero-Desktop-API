package site.nicklas.sphero.command;

import site.nicklas.sphero.command.RawMotorCommand.MOTOR_MODE;
import site.nicklas.sphero.util.Value;

/**
 * Command to spin the Sphero left
 *
 * @deprecated Sending this command will result in some future commands to fail
 * to execute on the Sphero for some reason. Has something to do with the logic
 * on the Sphero
 * @author Nicklas Gavelin
 */
public class SpinLeftCommand extends DeviceCommand
{
    private int speed;


    /**
     * Create a spin left command that will spin the Sphero left with a given
     * speed
     *
     * @param speed The speed to spin at 0-255
     */
    public SpinLeftCommand( int speed )
    {
        super( DEVICE_COMMAND.SPIN_LEFT );
        this.speed = Value.clamp( speed, 0, 255 );
    }


    @Override
    protected byte[] getPacketData()
    {
        byte[] data = new byte[ 4 ];

        data[0] = ( byte ) MOTOR_MODE.FORWARD.getValue();
        data[1] = ( byte ) this.speed;
        data[2] = ( byte ) MOTOR_MODE.REVERSE.getValue();
        data[3] = ( byte ) this.speed;

        return data;
    }
}
