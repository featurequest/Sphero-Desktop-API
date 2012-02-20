package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Response for the RollCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RollResponse extends DeviceResponse
{
    /**
     * Create a RollResponse from the received data
     *
     * @param data The received data
     */
    public RollResponse( byte[] data )
    {
        super( DEVICE_COMMAND.ROLL, data );
    }
}
