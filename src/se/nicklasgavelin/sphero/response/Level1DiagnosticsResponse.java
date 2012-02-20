package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Response for the Level1DiagnosticsResponse
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class Level1DiagnosticsResponse extends DeviceResponse
{
    /**
     * Create a Level1DiagnosticsResponse from the received data
     *
     * @param data The received data
     */
    public Level1DiagnosticsResponse( byte[] data )
    {
        super( DEVICE_COMMAND.LEVEL_1_DIAGNOSTICS, data );
    }
}
