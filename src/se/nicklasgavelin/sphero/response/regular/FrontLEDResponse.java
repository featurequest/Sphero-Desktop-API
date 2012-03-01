package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * A front led response that is for the FrontLEDCommand that must
 * be received to create this
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class FrontLEDResponse extends ResponseMessage
{
    /**
     * Create a FrontLEDResponse from a given received data
     */
    public FrontLEDResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DEVICE_COMMAND.FRONT_LED_OUTPUT, data );
    }
}
