/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * Calibration command as a macro command
 * Used for setting the 0 heading
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class Calibrate extends MacroCommand
{
	private int heading;
	public static int MIN_HEADING = 0, MAX_HEADING = 359;

	/**
	 * Create a calibrate macro command with a given heading as the calibration
	 * heading
	 * 
	 * @param _heading The new calibration heading (MIN_HEADING - MAX_HEADING)
	 */
	public Calibrate( int _heading )
	{
		super( MACRO_COMMAND.MAC_CALIBRATE );
		this.setHeading( _heading );
	}

	/**
	 * Update the internal heading value
	 * 
	 * @param _heading The internal heading value
	 */
	public void setHeading( int _heading )
	{
		this.heading = Value.clamp( _heading, MIN_HEADING, MAX_HEADING );
	}

	@Override
	public byte[] getByteRepresentation()
	{
		ByteArrayBuffer bab = new ByteArrayBuffer( getLength() );
		bab.append( getCommandID() );
		bab.append( this.heading >> 8 );
		bab.append( this.heading & 0xFF );

		return bab.toByteArray();
	}
}
