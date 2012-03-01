package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Create a respons for the RawMotorCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RawMotorResponse extends ResponseMessage
{
    /**
     * Create a RawMotorResponse from the received data
     *
     * @param data The received data
     */
    public RawMotorResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DEVICE_COMMAND.RAW_MOTOR, data );
    }
}
