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
public class SaveMacroResponse extends DeviceResponse
{
    public SaveMacroResponse( byte[] data )
    {
        super( DeviceCommand.DEVICE_COMMAND.SAVE_MACRO, data );
    }
}
