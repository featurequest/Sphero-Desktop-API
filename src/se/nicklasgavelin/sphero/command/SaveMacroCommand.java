package se.nicklasgavelin.sphero.command;

import se.nicklasgavelin.util.ByteArrayBuffer;

/**
 * Send a command to save a given macro command on the Sphero device
 * 
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class SaveMacroCommand extends CommandMessage
{
	public static final byte MacroFlagNone = 0;
	public static final byte MacroFlagMotorControl = 1;
	public static final byte MacroFlagExclusiveDrive = 2;
	public static final byte MacroFlagUseVersion3 = 4;
	public static final byte MacroFlagInhibitIfConnected = 8;
	public static final byte MacroFlagEndMarker = 16;
	public static final byte MacroFlagStealth = 32;
	public static final byte MacroFlagUnkillable = 64;
	public static final byte MacroFlagExtendedFlags = -128;
	public static final byte MACRO_STREAMING_DESTINATION = -2;
	private final byte[] macroData;
	private final byte macroFlags;
	private final byte destination;

	/**
	 * Create a command with a given destination, flags and macro command
	 * 
	 * @param flags The flags
	 * @param destination The destination on the Sphero
	 * @param macro The macro command as a byte array
	 */
	public SaveMacroCommand( byte flags, byte destination, byte[] macro )
	{
		super( COMMAND_MESSAGE_TYPE.SAVE_MACRO );

		this.macroFlags = flags;
		this.destination = destination;
		this.macroData = macro;
	}

	/**
	 * Create a command with a given destination, flags and macro command
	 * 
	 * @param flags The flags
	 * @param destination The destination on the Sphero
	 * @param macro The macro command as a byte array
	 */
	public SaveMacroCommand( int flags, int destination, byte[] macro )
	{
		this( (byte) flags, (byte) destination, macro );
	}

	@Override
	protected byte[] getPacketData()
	{
		ByteArrayBuffer bab = new ByteArrayBuffer( this.macroData.length + 2 );
		;
		bab.append( this.destination );
		bab.append( this.macroFlags );
		bab.append( this.macroData );

		return bab.toByteArray();
	}
}
