package site.nicklas.sphero.response;

import site.nicklas.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * The response for the JumpToMainCommand.
 *
 * @author Nicklas Gavelin
 */
public class JumpToMainResponse extends DeviceResponse
{
    /**
     * Create the JumpToMainResponse from the received data
     *
     * @param data The received data
     */
    public JumpToMainResponse( byte[] data )
    {
        super( DEVICE_COMMAND.JUMP_TO_MAIN, data );
    }
}
