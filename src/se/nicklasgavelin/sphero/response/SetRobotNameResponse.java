package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Response for the SetRobotNameCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SetRobotNameResponse extends DeviceResponse
{
    /**
     * Create a SetRobotNameResponse from the received data
     *
     * @param data The received data
     */
    public SetRobotNameResponse( byte[] data )
    {
        super( DEVICE_COMMAND.SET_BLUETOOTH_NAME, data );
    }
}
