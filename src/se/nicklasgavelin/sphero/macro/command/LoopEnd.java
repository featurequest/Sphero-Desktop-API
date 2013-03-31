package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;

/**
 * 
 * 
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 * @author Sebastian Garn, sgarn@cs.tu-berlin.de, Technical University of Berlin
 */
public class LoopEnd extends MacroCommand {

	public LoopEnd() {
		super( MACRO_COMMAND.MAC_LOOP_END );
	}

	@Override
	public byte[] getByteRepresentation()
	{
		ByteArrayBuffer bytes = new ByteArrayBuffer( getLength() );
		bytes.append(getCommandID());

		return bytes.toByteArray();
	}
}