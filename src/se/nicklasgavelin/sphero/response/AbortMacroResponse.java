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
    public static int NO_MACRO_RUNNING = 0;
    private int macroId;


    public AbortMacroResponse( byte[] data )
    {
        super( DeviceCommand.DEVICE_COMMAND.ABORT_MACRO, data );

        if ( data == null )
        {
            setCorrupt( true );
            this.macroId = 0;
        }
        else
            this.macroId = data[ RESPONSE_HEADER_LENGTH];
    }


    public int getMacroId()
    {
        return this.macroId;
    }
}
