package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class SaveTemporaryMacroResponse extends DeviceResponse
{
    public SaveTemporaryMacroResponse( byte[] data )
    {
        super( DeviceCommand.DEVICE_COMMAND.MACRO, data );
    }
}
