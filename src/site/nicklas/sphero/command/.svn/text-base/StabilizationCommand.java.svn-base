package site.nicklas.sphero.command;

/**
 * Command for turning on/off the Sphero stabilization.
 * I have no idea how this command works or if it does something
 * to the Sphero cause when running this command the Sphero then gets
 * unresponsive to some other commands.
 *
 * @deprecated Usage of this command results in failure to execute some future
 * commands such as the RollCommand
 * @author Nicklas Gavelin
 */
public class StabilizationCommand extends DeviceCommand
{
    private boolean on;


    /**
     * Create a stabilization command with the given on value
     * for the stabilization.
     *
     * @param on True for on, false for off
     */
    public StabilizationCommand( boolean on )
    {
        super( DEVICE_COMMAND.STABILIZATION );
    }


    /**
     * Returns the on state
     *
     * @return The on state
     */
    public boolean getOn()
    {
        return this.on;
    }


    @Override
    protected byte[] getPacketData()
    {
        byte[] data = new byte[ 1 ];
        data[0] = ( byte ) (this.on ? 1 : 0);

        return data;
    }
}
