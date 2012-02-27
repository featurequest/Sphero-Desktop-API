package se.nicklasgavelin.sphero.response;


/**
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class RunMacroResponse extends ResponseMessage
{
    public RunMacroResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DeviceCommand.DEVICE_COMMAND.RUN_MACRO, data );
    }
}
