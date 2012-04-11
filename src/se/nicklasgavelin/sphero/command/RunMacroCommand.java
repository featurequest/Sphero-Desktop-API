package se.nicklasgavelin.sphero.command;

import se.nicklasgavelin.util.ByteArrayBuffer;

public class RunMacroCommand extends CommandMessage
{
	private byte identifier;

	public RunMacroCommand( byte macroId )
	{
		super( COMMAND_MESSAGE_TYPE.RUN_MACRO );
		this.identifier = macroId;
	}

	public RunMacroCommand( int macroId )
	{
		this( (byte) macroId );
	}

	@Override
	protected byte[] getPacketData()
	{
		ByteArrayBuffer bab = new ByteArrayBuffer( 1 );
		bab.append( this.identifier );

		return bab.toByteArray();
		// byte[] data = new byte[ 1 ];
		// data[0] = this.identifier;
		//
		// return data;
	}
}
