package site.nicklas.sphero.response;

import site.nicklas.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Response for the JumpToBootloaderCommand
 * 
 * @author Nicklas Gavelin
 */
public class JumpToBootloaderResponse extends DeviceResponse
{
    /**
     * Create the JumpToBootloaderResponse from the received data
     * 
     * @param data The received data
     */
    public JumpToBootloaderResponse( byte[] data )
    {
        super( DEVICE_COMMAND.JUMP_TO_BOOTLOADER, data );
    }
}
