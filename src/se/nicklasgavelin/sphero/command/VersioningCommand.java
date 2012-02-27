package se.nicklasgavelin.sphero.command;

/**
 * Command to request version information from the Sphero robot.
 * The response received after sending this command contains the version
 * information
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class VersioningCommand extends CommandMessage
{
    public VersioningCommand()
    {
        super( COMMAND_MESSAGE_TYPE.VERSIONING );
    }
}
