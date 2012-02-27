package se.nicklasgavelin.sphero.response;

/**
 * Response for the Level1DiagnosticsResponse
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class Level1DiagnosticsResponse extends ResponseMessage
{
    /**
     * Create a Level1DiagnosticsResponse from the received data
     *
     * @param data The received data
     */
    public Level1DiagnosticsResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DEVICE_COMMAND.LEVEL_1_DIAGNOSTICS, data );
    }
}
