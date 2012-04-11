/*
 * Please read the LICENSE file that is included with the source
 * code.
 */

package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;

/**
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class EndStream extends MacroCommand
{
	public EndStream()
	{
		super( MACRO_COMMAND.MAC_STREAM_END );
	}

	@Override
	public byte[] getByteRepresentation()
	{
		ByteArrayBuffer bab = new ByteArrayBuffer( getLength() );
		bab.append( getCommandID() );

		return bab.toByteArray();
	}

}
