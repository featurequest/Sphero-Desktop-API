package se.nicklasgavelin.sphero.command;

/**
 * Command to make the Sphero jump to the main application,
 * will result in a lost connection to the Sphero!
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class JumpToMainCommand extends CommandMessage
{
	/**
	 * Create a JumpToMainCommand to send to the Sphero
	 */
	public JumpToMainCommand()
	{
		super( COMMAND_MESSAGE_TYPE.JUMP_TO_MAIN );
	}
}
