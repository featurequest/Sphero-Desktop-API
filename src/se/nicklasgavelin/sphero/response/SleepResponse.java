package se.nicklasgavelin.sphero.response;

/**
 * Response for the SleepCommand
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SleepResponse extends ResponseMessage
{
    /**
     * Create a SleepResponse from the received data
     *
     * @param data The received data
     */
    public SleepResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DEVICE_COMMAND.GO_TO_SLEEP, data );
    }
}
