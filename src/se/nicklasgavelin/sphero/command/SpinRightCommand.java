package se.nicklasgavelin.sphero.command;

import se.nicklasgavelin.sphero.command.RawMotorCommand.MOTOR_MODE;

/**
 * Command to spin the Sphero right with a given speed.
 *
 * @deprecated Sending this command will result in some future commands to fail
 * to execute on the Sphero for some reason. Has something to do with the logic
 * on the Sphero
 * @author Nicklas Gavelin
 */
public class SpinRightCommand extends DeviceCommand
{
    private int speed;


    /**
     * Create a spin left command
     *
     * @param speed The speed to spin at 0-255
     */
    public SpinRightCommand( int speed )
    {
        super( DEVICE_COMMAND.SPIN_RIGHT );
        this.speed = speed;
    }


    @Override
    protected byte[] getPacketData()
    {
        byte[] data = new byte[ 4 ];

        data[0] = ( byte ) MOTOR_MODE.REVERSE.getValue();
        data[1] = ( byte ) this.speed;
        data[2] = ( byte ) MOTOR_MODE.FORWARD.getValue();
        data[3] = ( byte ) this.speed;

        return data;
    }
}
