package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Create a response for the RGBLEDCommand
 *
 * @author Nicklas Gavelin
 */
public class RGBLEDResponse extends DeviceResponse
{
    /**
     * Create a RGBLEDResponse from the received data
     *
     * @param data The received data
     */
    public RGBLEDResponse( byte[] data )
    {
        super( DEVICE_COMMAND.RGB_LED_OUTPUT, data );
    }
}
