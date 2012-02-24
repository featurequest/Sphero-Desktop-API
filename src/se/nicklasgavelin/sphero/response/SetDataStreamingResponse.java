package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.command.DeviceCommand;

public class SetDataStreamingResponse extends DeviceResponse
{
    public SetDataStreamingResponse( byte[] data )
    {
        super( DeviceCommand.DEVICE_COMMAND.SET_DATA_STREAMING, data );
    }
}
