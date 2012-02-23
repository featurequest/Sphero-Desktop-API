/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class AbortMacroResponse extends DeviceResponse
{
    public AbortMacroResponse( byte[] data )
    {
        super( DeviceCommand.DEVICE_COMMAND.ABORT_MACRO, data );
    }
}
