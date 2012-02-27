package se.nicklasgavelin.sphero.response;

/**
 * Respons efor the PingCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class PingResponse extends ResponseMessage
{
    /**
     * Create a PingResponse from the received data
     *
     * @param data The received data
     */
    public PingResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DEVICE_COMMAND.PING, data );
    }
}
