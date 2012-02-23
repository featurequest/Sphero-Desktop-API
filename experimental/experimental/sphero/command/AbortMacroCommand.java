package experimental.sphero.command;

import se.nicklasgavelin.sphero.command.DeviceCommand;

/**
 * Ask robot to abort a running macro
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology@
 */
public class AbortMacroCommand extends DeviceCommand
{
    /**
     * Create a abort macro command
     */
    public AbortMacroCommand()
    {
        super( DEVICE_COMMAND.ABORT_MACRO );
    }
}
