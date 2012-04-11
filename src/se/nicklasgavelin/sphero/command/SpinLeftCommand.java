package se.nicklasgavelin.sphero.command;

import se.nicklasgavelin.sphero.command.RawMotorCommand.MOTOR_MODE;
import se.nicklasgavelin.util.Value;

/**
 * Command to spin the Sphero left
 *
 * NOTICE: Sending this command will result in some future commands to fail
 * to execute on the Sphero for some reason. 
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SpinLeftCommand extends CommandMessage
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
        super( COMMAND_MESSAGE_TYPE.SPIN_LEFT );
        this.speed = Value.clamp( speed, 0, 255 );
    }


    @Override
    protected byte[] getPacketData()
    {
        byte[] data = {
        		( byte ) MOTOR_MODE.FORWARD.getValue(),
        		( byte ) this.speed,
        		( byte ) MOTOR_MODE.REVERSE.getValue(),
        		( byte ) this.speed
		};

        return data;
    }
}
