package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * 
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 * @author Sebastian Garn, sgarn@cs.tu-berlin.de, Technical University of Berlin
 */
public class LoopStart extends MacroCommand {
    public static final int MIN_LOOP_VALUE = 1, MAX_LOOP_VALUE = 65535;
    private int mCount;

	public LoopStart(int count) {
		super( MACRO_COMMAND.MAC_LOOP_START );
		mCount = Value.clamp(count, MIN_LOOP_VALUE, MAX_LOOP_VALUE );
	}

	public void setCount(int count) {
	    mCount = Value.clamp(count, MIN_LOOP_VALUE, MAX_LOOP_VALUE );;
	}
	
	public int getCount() {
	    return mCount;
	}
	  
	@Override
	public byte[] getByteRepresentation()
	{
		ByteArrayBuffer bytes = new ByteArrayBuffer( getLength() );
		bytes.append(getCommandID());
		bytes.append(getCount());

		return bytes.toByteArray();
	}
}