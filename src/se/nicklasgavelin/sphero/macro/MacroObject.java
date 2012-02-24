package se.nicklasgavelin.sphero.macro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import se.nicklasgavelin.util.ByteArrayBuffer;

/**
 * Still experimental
 *
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class MacroObject
{
    private Collection<MacroCommand> commands;
    private MacroObjectMode mode = MacroObjectMode.Normal;


    public MacroObject()
    {
        this.commands = new ArrayList<MacroCommand>();
    }


    public MacroObject( Collection<MacroCommand> _commands )
    {
        this.commands.addAll( _commands );
    }


    /**
     * Returns the currently stored commands
     *
     * @return The currently stored commands
     */
    public Collection<MacroCommand> getCommands()
    {
        return this.commands;
    }


    /**
     * Add a command to send
     *
     * @param command The command to add
     */
    public void addCommand( MacroCommand command )
    {
        this.commands.add( command );
    }


    /**
     * Generate the data for all the commands,
     * the maximum size of the macro is 256 and a Roll stop command will be
     * appended
     * if the size of the concatenated commands exceed 248 bytes
     *
     * @return The macro commands as a byte array
     */
    public byte[] generateMacroData()
    {
        // Create a buffer
        ByteArrayBuffer data = new ByteArrayBuffer( 256 );
        int currentLength = 0;

        // Go through all our commands that we got
        for ( Iterator i = this.commands.iterator(); i.hasNext(); )
        {
             // Fetch the next command
            MacroCommand command = ( MacroCommand ) i.next();

            // Check if we still got space left
            if ( command.getLength() + currentLength > 248  )
            {
                // TODO: Will this really work??? The command will not be added if the byte array is full
                Roll stop = new Roll( Double.valueOf( 0.0D ), Integer.valueOf( 0 ), Integer.valueOf( 0 ) );
                data.append( stop.getByteRepresentation(), 0, stop.getLength() );
                break;
            }

            // Append the command to our buffer
            data.append( command.getByteRepresentation() );

            // Update the current length
            currentLength += command.getLength();
        }

        // Append a end command for the macro
        data.append( MacroCommand.MACRO_COMMAND.MAC_END.getValue() );

        // Return the created macro data

        return data.toByteArray();
    }


    public MacroObjectMode getMode()
    {
        return this.mode;
    }


    public void setMode( MacroObjectMode _mode )
    {
        this.mode = _mode;
    }

    public static enum MacroObjectMode
    {
        Normal,
        CachedStreaming;
    }
}
