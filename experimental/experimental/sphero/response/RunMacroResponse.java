package experimental.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand;
import se.nicklasgavelin.sphero.response.DeviceResponse;

public class RunMacroResponse extends DeviceResponse
{
    public RunMacroResponse( byte[] data )
    {
        super( DeviceCommand.DEVICE_COMMAND.RUN_MACRO, data );
    }
}