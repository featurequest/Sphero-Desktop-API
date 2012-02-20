package se.nicklasgavelin.sphero.command;

/**
 * Command to make the Sphero go to sleep for a given number
 * of seconds.
 *
 * NOTICE: Sending this command will result in a lost connection to the Sphero
 * as the Bluetooth connection is being closed down
 *
 * @author Nicklas Gavelin
 *
 */
public class SleepCommand extends DeviceCommand
{
    private int time;


    /**
     * Create a sleep command with a given wakeup time
     *
     * @param time The wakeup time (given in seconds)
     */
    public SleepCommand( int time )
    {
        super( DEVICE_COMMAND.GO_TO_SLEEP );
        this.time = time;
    }


    /**
     * Returns the wakeup time for the command
     *
     * @return The wakeup time
     */
    public int getWakeUpTime()
    {
        return this.time;
    }


    @Override
    protected byte[] getPacketData()
    {
        byte[] data = new byte[ 3 ];
        data[0] = ( byte ) (this.time >> 8);
        data[1] = ( byte ) (this.time);
        data[2] = 0; // TODO: Replace with macro id

        return data;
    }
}
