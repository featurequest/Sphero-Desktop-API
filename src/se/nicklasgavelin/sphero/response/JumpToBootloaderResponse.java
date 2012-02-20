package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Response for the JumpToBootloaderCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
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
