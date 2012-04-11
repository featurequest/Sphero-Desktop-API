package se.nicklasgavelin.sphero.command;

/**
 * Command to make the Sphero go to sleep for a given number
 * of seconds.
 * 
 * NOTICE: Sending this command will result in a lost connection to the Sphero
 * as the Bluetooth connection is closed down after the sleep state is entered
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 * 
 */
public class SleepCommand extends CommandMessage
{
	private int time;

	/**
	 * Create a sleep command with a given wakeup time
	 * 
	 * @param time The wakeup time (given in seconds)
	 */
	public SleepCommand( int time )
	{
		super( COMMAND_MESSAGE_TYPE.GO_TO_SLEEP );
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
		data[0] = (byte) ( this.time >> 8 );
		data[1] = (byte) ( this.time );
		data[2] = 0; // TODO: Replace with macro id

		return data;
	}
}
