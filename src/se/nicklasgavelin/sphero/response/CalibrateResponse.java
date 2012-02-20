package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Calibration response received after sending a CalibrateCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class CalibrateResponse extends DeviceResponse
{
    /**
     * Create a response from the received data
     *
     * @param data The received data
     */
    public CalibrateResponse( byte[] data )
    {
        super( DEVICE_COMMAND.CALIBRATE, data );
    }
}
