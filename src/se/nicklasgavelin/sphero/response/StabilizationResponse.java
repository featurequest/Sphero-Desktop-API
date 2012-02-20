package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Respons efor the StabilizationCommand
 *
 * @author Nicklas Gavelin
 */
public class StabilizationResponse extends DeviceResponse
{
    /**
     * Create a StabilizationResponse from the received data
     *
     * @param data The received data
     */
    public StabilizationResponse( byte[] data )
    {
        super( DEVICE_COMMAND.STABILIZATION, data );
    }
}
