package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Response for the SpinLeftCommand
 *
 * @author Nicklas Gavelin
 */
public class SpinLeftResponse extends DeviceResponse
{
    /**
     * Create a SpinLeftResponse from the received data
     *
     * @param data The received data
     */
    public SpinLeftResponse( byte[] data )
    {
        super( DEVICE_COMMAND.SPIN_LEFT, data );
    }
}
