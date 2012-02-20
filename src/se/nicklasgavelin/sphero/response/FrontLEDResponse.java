package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * A front led response that is for the FrontLEDCommand that must
 * be received to create this
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class FrontLEDResponse extends DeviceResponse
{
    /**
     * Create a FrontLEDResponse from a given received data
     */
    public FrontLEDResponse( byte[] data )
    {
        super( DEVICE_COMMAND.FRONT_LED_OUTPUT, data );
    }
}
