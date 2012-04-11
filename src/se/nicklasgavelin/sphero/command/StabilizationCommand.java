package se.nicklasgavelin.sphero.command;

/**
 * Command for turning on/off the Sphero stabilization.
 * I have no idea how this command works or if it does something
 * to the Sphero cause when running this command the Sphero then gets
 * unresponsive to some other commands.
 * 
 * NOTICE: Usage of this command results in failure to execute some future
 * commands such as the RollCommand
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class StabilizationCommand extends CommandMessage
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
		super( COMMAND_MESSAGE_TYPE.STABILIZATION );
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
		return new byte[] { (byte) ( this.on ? 1 : 0 ) };
	}
}
