package experimental.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand;
import se.nicklasgavelin.sphero.response.DeviceResponse;

public class MacroResponse extends DeviceResponse
{
    public MacroResponse( byte[] data )
    {
        super( DeviceCommand.DEVICE_COMMAND.SAVE_MACRO, data );
    }
}
