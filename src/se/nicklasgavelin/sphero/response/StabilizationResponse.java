package se.nicklasgavelin.sphero.response;

/**
 * Response error the StabilizationCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class StabilizationResponse extends ResponseMessage
{
    /**
     * Create a StabilizationResponse from the received data
     *
     * @param data The received data
     */
    public StabilizationResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DEVICE_COMMAND.STABILIZATION, data );
    }
}
