package experimental.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand;
import se.nicklasgavelin.sphero.response.DeviceResponse;

/**
 * @deprecated Experimental
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class BoostResponse extends DeviceResponse
{
    /**
     * Create a boost response from a byte array object
     * 
     * @param data The data for the response
     */
    public BoostResponse( byte[] data )
    {
        super( DeviceCommand.DEVICE_COMMAND.BOOST, data );
    }
}
