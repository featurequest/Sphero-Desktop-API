package se.nicklasgavelin.sphero.command;

/**
 * Command to set the robot in bootloader application. Will result in a lost
 * connection to the Sphero
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class JumpToBootloaderCommand extends DeviceCommand
{
    /**
     * Create a JumpToBootloaderCommand to send to the Sphero
     */
    public JumpToBootloaderCommand()
    {
        super( DEVICE_COMMAND.JUMP_TO_BOOTLOADER );
    }
}
