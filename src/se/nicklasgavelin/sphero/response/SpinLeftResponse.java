package se.nicklasgavelin.sphero.response;

/**
 * Response for the SpinLeftCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SpinLeftResponse extends ResponseMessage
{
    /**
     * Create a SpinLeftResponse from the received data
     *
     * @param data The received data
     */
    public SpinLeftResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DEVICE_COMMAND.SPIN_LEFT, data );
    }
}
