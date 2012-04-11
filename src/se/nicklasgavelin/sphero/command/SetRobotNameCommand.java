package se.nicklasgavelin.sphero.command;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Command to change the name of the Sphero.
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SetRobotNameCommand extends CommandMessage
{
	private String name;

	/**
	 * Create a set robot name command with a given robot name
	 * 
	 * @param name The new robot name
	 */
	public SetRobotNameCommand( String name )
	{
		super( COMMAND_MESSAGE_TYPE.SET_BLUETOOTH_NAME );
		this.name = name;
	}

	/**
	 * Returns the name set
	 * 
	 * @return The name set
	 */
	public String getName()
	{
		return this.name;
	}

	@Override
	protected byte[] getPacketData()
	{
		// Create an empty array
		byte[] data = new byte[ 48 ];
		Arrays.fill( data, (byte) 0 );

		byte[] encoded_name;
		try
		{
			// Try UTF encoding
			encoded_name = this.name.getBytes( "UTF-8" );
		}
		catch( UnsupportedEncodingException e )
		{
			return null;
		}

		// Copy everything to the data array
		for( int i = 0; ( i < 48 ) && ( i < encoded_name.length ); i++ )
			data[i] = encoded_name[i];

		return data;
	}
}
