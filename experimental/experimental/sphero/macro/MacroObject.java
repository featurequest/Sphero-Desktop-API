package experimental.sphero.macro;

import experimental.sphero.command.AbortMacroCommand;
import experimental.sphero.command.RunMacroCommand;
import experimental.sphero.command.SaveMacroCommand;
import experimental.sphero.command.SaveTemporaryMacroCommand;
import java.util.ArrayList;
import java.util.Iterator;
import se.nicklasgavelin.sphero.Robot;
import se.nicklasgavelin.util.ByteArrayBuffer;

/**
 * Still experimental
 *
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class MacroObject
{
    private ArrayList<MacroCommand> commands = new ArrayList();
    private ArrayList<MacroCommand> lastCommands = new ArrayList();
    private ArrayList<Integer> ballMemory = new ArrayList();
//    private MacroObjectMode mode = MacroObjectMode.Normal;
//    private boolean running = false;
    private boolean registeredObserver = false;
    private Robot mRobot;
//
//
//    public MacroObjectMode getMode()
//    {
//        return this.mode;
//    }
//
//
//    public void setMode( MacroObjectMode _mode )
//    {
//        this.mode = _mode;
//    }
//
//

    /**
     * Returns the currently stored commands
     *
     * @return The currently stored commands
     */
    public ArrayList<MacroCommand> getCommands()
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
     * Set which robot this macro command belongs to
     *
     * @param robot The robot the macro command belongs to
     */
    public void setRobot( Robot robot )
    {
        this.mRobot = robot;
    }

    /**
     * Start playing the macro at the Sphero robot
     */
    public void playMacro()
    {
//        if ( this.mode == MacroObjectMode.Normal )
//        {
        if ( this.mRobot == null )
            return;

        this.mRobot.sendCommand( new SaveTemporaryMacroCommand( (byte)1, generateMacroData() ) );
        this.mRobot.sendCommand( new RunMacroCommand( (byte)-1 ) );

//            SaveTemporaryMacroCommand.sendCommand( this.mRobot, 1, generateMacroData() );
//            RunMacroCommand.sendCommand( this.mRobot, -1 );
//        }
//        else
//            if ( this.mode == MacroObjectMode.CachedStreaming )
//            {
//                if ( this.commands.size() == 0 )
//                {
//                    this.commands.addAll( this.lastCommands );
//                }
//                else
//                {
//                    this.lastCommands.clear();
//                    this.lastCommands.addAll( this.commands );
//                }
//
//                emptyCommandQueue();
//                if ( !this.running )
//                {
//                    this.running = true;
//                    if ( !this.registeredObserver )
//                    {
//                        this.registeredObserver = true;
//                        if ( this.mRobot == null )
//                            return;
//                        DeviceMessenger.AsyncDataListener listener = new DeviceMessenger.AsyncDataListener()
//                        {
//                            public void onDataReceived( DeviceAsyncData data )
//                            {
//                                if ( data.getClass() == MacroEmitMarker.class )
//                                {
//                                    MacroEmitMarker marker = ( MacroEmitMarker ) data;
//                                    if ( marker.getMarkerId() == 1 )
//                                    {
//                                        if ( MacroObject.this.ballMemory.size() > 0 )
//                                            MacroObject.this.ballMemory.remove( 0 );
//                                        MacroObject.this.emptyCommandQueue();
//                                    }
//                                }
//                            }
//                        };
//                        DeviceMessenger.getInstance().addAsyncDataListener( this.mRobot, listener );
//                    }
//                    if ( this.mRobot != null )
//                        RunMacroCommand.sendCommand( this.mRobot, -2 );
//                }
//            }
    }


    public void stopMacro()
    {
        if ( this.mRobot == null )
            return;

        this.mRobot.sendCommand( new AbortMacroCommand() );
        //AbortMacroCommand.sendCommand( this.mRobot );
        this.commands.clear();
        this.ballMemory.clear();
//        this.running = false;
    }


    private Integer freeBallMemory()
    {
        Integer bytesInUse = Integer.valueOf( 0 );
        for ( Iterator i = this.ballMemory.iterator(); i.hasNext(); )
        {
            bytesInUse = Integer.valueOf( bytesInUse.intValue() + (( Integer ) i.next()).intValue() );
        }
        Integer freeBytes = Integer.valueOf( 1000 - bytesInUse.intValue() );
        return freeBytes;
    }


    private void emptyCommandQueue()
    {
        if ( this.commands.isEmpty() )
            return;

        int freeBytes = freeBallMemory().intValue();
        if ( freeBytes < 128 )
            return;
        if ( freeBytes > 250 )
            freeBytes = 250;

        int maxIndex = 0;
        int chunkSize = 0;
        ArrayList chunk = new ArrayList();
        for ( Iterator i = this.commands.iterator(); i.hasNext(); )
        {
            MacroCommand command = ( MacroCommand ) i.next();
            if ( chunkSize + command.getLength() >= freeBytes )
                break;
            chunkSize += command.getLength();
            maxIndex++;
            chunk.add( command );
        }

        chunk.add( new Emit( Integer.valueOf( 1 ) ) );
        chunkSize += 2;

        ByteArrayBuffer data = new ByteArrayBuffer( chunkSize );
        for ( Iterator i = chunk.iterator(); i.hasNext(); )
        {
            MacroCommand command = ( MacroCommand ) i.next();
            data.append( command.getByteRepresentation(), 0, command.getLength() );
        }

        this.ballMemory.add( new Integer( chunkSize ) );

        if ( this.mRobot != null )
            this.mRobot.sendCommand( new SaveMacroCommand( ( byte ) 1, ( byte ) -2, data.toByteArray() ) );
        //SaveMacroCommand.sendCommand( this.mRobot, 1, -2, data.toByteArray() );

        this.commands.removeAll( chunk );

        if ( (freeBallMemory().intValue() > 128) && (this.commands.size() > 0) )
            emptyCommandQueue();
    }

    /**
     * Generate the data for all the commands,
     * the maximum size of the macro is 256 and a Roll stop command will be appended
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
            // Check if we still got space left
            if ( currentLength > 248 )
            {
                // TODO: Will this really work??? The command will not be added if the byte array is full
                Roll stop = new Roll( Double.valueOf( 0.0D ), Integer.valueOf( 0 ), Integer.valueOf( 0 ) );
                data.append( stop.getByteRepresentation(), 0, stop.getLength() );
                break;
            }

            // Fetch the next command
            MacroCommand command = ( MacroCommand ) i.next();

            // Append the command to our buffer
            data.append( command.getByteRepresentation(), 0, command.getLength() );

            // Update the current length
            currentLength += command.getLength();
        }

        // Append a end command for the macro
        data.append( MacroCommand.MACRO_COMMAND.MAC_END.getValue() );

        // Return the created macro data

        return data.toByteArray();
    }
//
//


//    public boolean isRunning()
//    {
//        return this.running;
//    }
//
//    public static enum MacroObjectMode
//    {
//        Normal,
//        CachedStreaming;
//    }
}
