package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;

/**
 * 
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 * @author Sebastian Garn, sgarn@cs.tu-berlin.de, Technical University of Berlin
 */
public class LoopStart extends MacroCommand {

    private int mCount = Integer.valueOf( 1000 );
    public static final int MAX_DELAY = 65534, MIN_DELAY = 0;

	public LoopStart(int count) {
		super( MACRO_COMMAND.MAC_LOOP_START );
		mCount = count;
	}

	public void setCount(int count) {
	    if (count == 0) count = 1;
	    
	    mCount = count;
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