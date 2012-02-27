package se.nicklasgavelin.sphero.response;


public class SetDataStreamingResponse extends ResponseMessage
{
    public SetDataStreamingResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DeviceCommand.DEVICE_COMMAND.SET_DATA_STREAMING, data );
    }
}
