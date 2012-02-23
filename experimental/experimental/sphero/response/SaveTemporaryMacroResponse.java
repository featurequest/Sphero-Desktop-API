package experimental.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand;
import se.nicklasgavelin.sphero.response.DeviceResponse;

public class SaveTemporaryMacroResponse extends DeviceResponse
{
	public SaveTemporaryMacroResponse( byte[] data )
	{
		super( DeviceCommand.DEVICE_COMMAND.MACRO, data );
	}
}
