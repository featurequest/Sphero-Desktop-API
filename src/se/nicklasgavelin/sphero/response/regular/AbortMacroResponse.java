/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class AbortMacroResponse extends ResponseMessage
{
    public static final int NO_MACRO_RUNNING = 0;
    public static final int MACRO_ID_INDEX = RESPONSE_HEADER_LENGTH;
    private int macroId;


    public AbortMacroResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//DeviceCommand.DEVICE_COMMAND.ABORT_MACRO, data );

//        if ( rh == null )
//        {
//            setCorrupt( true );
//            this.macroId = 0;
//        }
//        else
            this.macroId = rh.getPacketData().toByteArray()[ RESPONSE_HEADER_LENGTH];
    }


    /**
     * Returns the internal macro id
     *
     * @return The macro id that was aborted
     */
    public int getMacroId()
    {
        return this.macroId;
    }
}
