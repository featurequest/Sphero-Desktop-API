package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class Emit extends MacroCommand
{
	private static final int DEFAULT_IDENTIFIER = 1, MAX_IDENTIFIER = 255,
			MIN_IDENTIFIER = 0;
	private Integer identifier = Integer.valueOf( DEFAULT_IDENTIFIER );

	// public Emit( byte[] data )
	// {
	// }
	public Emit( int _identifier )
	{
		super( MACRO_COMMAND.MAC_EMIT );
		this.setIdentifier( _identifier );
	}

	/**
	 * Returns the internal identifier
	 * 
	 * @return The internal identifier
	 */
	public Integer getIdentifier()
	{
		return this.identifier;
	}

	/**
	 * Set the internal identifier value
	 * 
	 * @param _identifier The new identifier
	 */
	public void setIdentifier( Integer _identifier )
	{
		this.identifier = Value.clamp( _identifier, MIN_IDENTIFIER, MAX_IDENTIFIER );
		// if ( (_identifier.intValue() >= IDENTIFIER_MIN_VALUE) &&
		// (_identifier.intValue() <= IDENTIFIER_MAX_VALUE) )
		// this.identifier = _identifier;
	}

	@Override
	public byte[] getByteRepresentation()
	{
		ByteArrayBuffer bytes = new ByteArrayBuffer( getLength() );
		bytes.append( getCommandID() );
		bytes.append( this.identifier.intValue() );

		return bytes.toByteArray();
	}
}