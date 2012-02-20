package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Response for the GetConfigurationBlockCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class GetConfigurationBlockResponse extends DeviceResponse
{
    /**
     * Create a GetCOnfigurationBlockResponse from the received data
     *
     * @param data The received data
     */
    public GetConfigurationBlockResponse( byte[] data )
    {
        super( DEVICE_COMMAND.GET_CONFIGURATION_BLOCK, data );
    }
}
