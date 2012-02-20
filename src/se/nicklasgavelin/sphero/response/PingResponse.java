package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Respons efor the PingCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class PingResponse extends DeviceResponse
{
    /**
     * Create a PingResponse from the received data
     *
     * @param data The received data
     */
    public PingResponse( byte[] data )
    {
        super( DEVICE_COMMAND.PING, data );
    }
}
