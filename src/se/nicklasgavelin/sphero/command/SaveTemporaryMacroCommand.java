package se.nicklasgavelin.sphero.command;

public class SaveTemporaryMacroCommand extends CommandMessage
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
	private byte flags;
	private byte[] macroData;

	public SaveTemporaryMacroCommand( int flags, byte[] macro )
	{
		this( (byte) flags, macro );
	}

	public SaveTemporaryMacroCommand( byte flags, byte[] macro )
	{
		super( COMMAND_MESSAGE_TYPE.MACRO );
		this.macroData = macro;
		this.flags = flags;
	}

	@Override
	protected byte[] getPacketData()
	{
		byte[] data = new byte[ this.macroData.length + 2 ];

		data[0] = -1;
		data[1] = this.flags;

		for( int i = 0; i < this.macroData.length; i++ )
			data[( i + 2 )] = this.macroData[i];

		return data;
	}
}
