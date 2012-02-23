package experimental.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand;
import se.nicklasgavelin.sphero.response.DeviceResponse;

public class BoostResponse extends DeviceResponse
{
    public BoostResponse( byte[] data )
    {
        super( DeviceCommand.DEVICE_COMMAND.BOOST, data );
    }
}
