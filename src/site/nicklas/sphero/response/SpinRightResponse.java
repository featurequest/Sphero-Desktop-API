package site.nicklas.sphero.response;

import site.nicklas.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * The response for the SpinRightCommand
 * 
 * @author Nicklas Gavelin
 */
public class SpinRightResponse extends DeviceResponse
{
    /**
     * Create a SpinRightResponse from the received data
     * 
     * @param data The received data
     */
    public SpinRightResponse( byte[] data )
    {
        super( DEVICE_COMMAND.SPIN_RIGHT, data );
    }
}
