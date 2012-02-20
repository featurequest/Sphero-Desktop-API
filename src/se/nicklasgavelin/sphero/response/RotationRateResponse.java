package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Response for the RotationRateResponse
 *
 * @author Nicklas Gavelin
 */
public class RotationRateResponse extends DeviceResponse
{
    /**
     * Create a RotationRateResponse from the received data
     *
     * @param data The received data
     */
    public RotationRateResponse( byte[] data )
    {
        super( DEVICE_COMMAND.ROTATION_RATE, data );
    }
}
