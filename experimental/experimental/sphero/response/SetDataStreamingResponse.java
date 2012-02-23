package experimental.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand;
import se.nicklasgavelin.sphero.response.DeviceResponse;

public class SetDataStreamingResponse extends DeviceResponse
{
	public SetDataStreamingResponse( byte[] data )
	{
		super( DeviceCommand.DEVICE_COMMAND.SET_DATA_STREAMING, data );
	}
}
