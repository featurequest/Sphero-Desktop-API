package se.nicklasgavelin.sphero.response;

/**
 * Response for the SetRobotNameCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SetRobotNameResponse extends ResponseMessage
{
    /**
     * Create a SetRobotNameResponse from the received data
     *
     * @param data The received data
     */
    public SetRobotNameResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DEVICE_COMMAND.SET_BLUETOOTH_NAME, data );
    }
}
