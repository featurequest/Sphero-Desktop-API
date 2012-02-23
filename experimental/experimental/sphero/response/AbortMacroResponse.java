package experimental.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand;
import se.nicklasgavelin.sphero.response.DeviceResponse;

/**
 * Response for abort macro
 *
 * @author Nicklas Gavelin
 *
 */
public class AbortMacroResponse extends DeviceResponse
{
    public static final int NO_MACRO_RUNNING = 0;
    private final int macroId;


    /**
     * Create response for abort macro
     *
     * @param data The data
     */
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


    /**
     * Returns the macro id.
     *
     * @return The macro id, 0 if no macro is running
     *         (AbortMacroResponse.NO_MACRO_RUNNING)
     */
    public int getMacroId()
    {
        return this.macroId;
    }
}
