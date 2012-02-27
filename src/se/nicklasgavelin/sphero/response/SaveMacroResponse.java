/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.response;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class SaveMacroResponse extends ResponseMessage
{
    public SaveMacroResponse( ResponseHeader rh )//byte[] data )
    {
        super( rh );//super( DeviceCommand.DEVICE_COMMAND.SAVE_MACRO, data );
    }
}
