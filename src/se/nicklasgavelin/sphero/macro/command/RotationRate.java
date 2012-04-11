package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * A macro command to set the rotation rate of the Sphero device
 * 
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class RotationRate extends MacroCommand
{
	private float mRate;
	public static final float MIN_RATE = 0, MAX_RATE = 1;

	// public RotationRate(byte[] data) {
	// super(data);
	// }

	/**
	 * Create a rotation rate macro command with a given rotation rate
	 * 
	 * @param rate The rotation rate
	 */
	public RotationRate( float rate )
	{
		super( MACRO_COMMAND.MAC_ROTATION_RATE );
		this.mRate = rate;
	}

	/**
	 * Returns the internal rotation rate value
	 * 
	 * @return The rotation rate value
	 */
	public float getRotationRate()
	{
		return this.mRate;
	}

	/**
	 * Update the internal rate value
	 * 
	 * @param rate The new rotation rate (MIN_RATE - MAX_RATE)
	 */
	public void setRotationRate( float rate )
	{
		this.mRate = Value.clamp( rate, MIN_RATE, MAX_RATE );
	}

	@Override
	public byte[] getByteRepresentation()
	{
		// byte[] data = new byte[ getLength() ];
		// data[0] = getCommandID();
		// data[1] = ( byte ) ( int ) (this.mRate * 255.0D);
		ByteArrayBuffer bab = new ByteArrayBuffer( getLength() );
		bab.append( getCommandID() );
		bab.append( (int) ( this.mRate * 255.0D ) );

		return bab.toByteArray();
	}
}