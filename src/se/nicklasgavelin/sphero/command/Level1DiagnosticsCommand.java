package se.nicklasgavelin.sphero.command;

/**
 * Seems to be a diagnostic command, duh?, no idea what it does.
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class Level1DiagnosticsCommand extends DeviceCommand
{
    /**
     * Create a Level1DiagnosticsCommand
     */
    public Level1DiagnosticsCommand()
    {
        super( DEVICE_COMMAND.LEVEL_1_DIAGNOSTICS );
    }
}
