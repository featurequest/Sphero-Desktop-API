package experimental.sphero.response;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * @deprecated Experimental
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class BoostResponse extends ResponseMessage
{
    /**
     * Create a boost response from a byte array object
     *
     * @param rh The response header containing the data
     */
    public BoostResponse( ResponseHeader rh )
    {
        super( rh );
        //super( DeviceCommand.DEVICE_COMMAND.BOOST, data );
    }
}
