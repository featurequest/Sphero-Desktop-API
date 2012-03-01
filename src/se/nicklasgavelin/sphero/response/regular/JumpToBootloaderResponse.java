package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;


/**
 * Response for the JumpToBootloaderCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class JumpToBootloaderResponse extends ResponseMessage
{
    /**
     * Create the JumpToBootloaderResponse from the received data
     *
     * @param data The received data
     */
    public JumpToBootloaderResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DEVICE_COMMAND.JUMP_TO_BOOTLOADER, data );
    }
}
