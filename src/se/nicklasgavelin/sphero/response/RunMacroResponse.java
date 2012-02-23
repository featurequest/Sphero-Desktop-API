package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class RunMacroResponse extends DeviceResponse
{
    public RunMacroResponse( byte[] data )
    {
        super( DeviceCommand.DEVICE_COMMAND.RUN_MACRO, data );
    }
}
