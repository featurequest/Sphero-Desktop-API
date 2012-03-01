package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Create a response for the RGBLEDCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RGBLEDResponse extends ResponseMessage
{
    /**
     * Create a RGBLEDResponse from the received data
     *
     * @param data The received data
     */
    public RGBLEDResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );// super( DEVICE_COMMAND.RGB_LED_OUTPUT, data );
    }
}
