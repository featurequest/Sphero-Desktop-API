package se.nicklasgavelin.sphero.command;

/**
 * Seems to be a diagnostic command, duh?, no idea what it does.
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class Level1DiagnosticsCommand extends CommandMessage
{
	/**
	 * Create a Level1DiagnosticsCommand
	 */
	public Level1DiagnosticsCommand()
	{
		super( COMMAND_MESSAGE_TYPE.LEVEL_1_DIAGNOSTICS );
	}
}
