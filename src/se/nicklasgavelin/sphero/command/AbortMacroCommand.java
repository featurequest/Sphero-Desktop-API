package se.nicklasgavelin.sphero.command;

/**
 * Ask robot to abort a running macro
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class AbortMacroCommand extends CommandMessage
{
	/**
	 * Create a abort macro command
	 */
	public AbortMacroCommand()
	{
		super( COMMAND_MESSAGE_TYPE.ABORT_MACRO );
	}
}
