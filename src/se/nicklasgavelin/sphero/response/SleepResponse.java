package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Response for the SleepCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SleepResponse extends DeviceResponse
{
    /**
     * Create a SleepResponse from the received data
     *
     * @param data The received data
     */
    public SleepResponse( byte[] data )
    {
        super( DEVICE_COMMAND.GO_TO_SLEEP, data );
    }
}
