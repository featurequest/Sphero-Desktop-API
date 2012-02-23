package experimental.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand;
import se.nicklasgavelin.sphero.response.DeviceResponse;

public class SaveMacroResponse extends DeviceResponse
{
    public SaveMacroResponse( byte[] data )
    {
        super( DeviceCommand.DEVICE_COMMAND.SAVE_MACRO, data );
    }
}