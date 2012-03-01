package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Response for the RollCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RollResponse extends ResponseMessage
{
    /**
     * Create a RollResponse from the received data
     *
     * @param data The received data
     */
    public RollResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DEVICE_COMMAND.ROLL, data );
    }
}
