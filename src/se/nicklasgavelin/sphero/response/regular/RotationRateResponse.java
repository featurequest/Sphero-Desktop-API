package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Response for the RotationRateResponse
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RotationRateResponse extends ResponseMessage
{
    /**
     * Create a RotationRateResponse from the received data
     *
     * @param data The received data
     */
    public RotationRateResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DEVICE_COMMAND.ROTATION_RATE, data );
    }
}
