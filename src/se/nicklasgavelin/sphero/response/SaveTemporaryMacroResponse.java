package se.nicklasgavelin.sphero.response;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class SaveTemporaryMacroResponse extends ResponseMessage
{
    public SaveTemporaryMacroResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DeviceCommand.DEVICE_COMMAND.MACRO, data );
    }
}
