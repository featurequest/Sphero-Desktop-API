package se.nicklasgavelin.sphero.response;

/**
 * The response for the JumpToMainCommand.
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class JumpToMainResponse extends ResponseMessage
{
    /**
     * Create the JumpToMainResponse from the received data
     *
     * @param data The received data
     */
    public JumpToMainResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DEVICE_COMMAND.JUMP_TO_MAIN, data );
    }
}
