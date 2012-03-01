package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * The response for the SpinRightCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SpinRightResponse extends ResponseMessage
{
    /**
     * Create a SpinRightResponse from the received data
     *
     * @param data The received data
     */
    public SpinRightResponse( ResponseMessage.ResponseHeader rh )//byte[] data )
    {
        super( rh ); //super( DEVICE_COMMAND.SPIN_RIGHT, data );
    }
}
