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
     * @param rh The response header containing the response data
     */
    public JumpToBootloaderResponse( ResponseHeader rh )
    {
        super( rh );
    }
}
