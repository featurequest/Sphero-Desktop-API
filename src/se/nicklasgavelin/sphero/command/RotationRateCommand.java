package se.nicklasgavelin.sphero.command;

import se.nicklasgavelin.util.Value;

/**
 * Command for setting the Sphero rotation rate. I've seen no visible result
 * after sending this command.
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RotationRateCommand extends CommandMessage
{
	private float rate;

	/**
	 * Create a rotation rate command.
	 * 
	 * @param rate Rotation rate between 0-1
	 */
	public RotationRateCommand( float rate )
	{
		super( COMMAND_MESSAGE_TYPE.ROTATION_RATE );
		this.rate = (float) Value.clamp( rate, 0.0D, 1.0D );
	}

	/**
	 * Returns the set rotation rate
	 * 
	 * @return The set rotation rate
	 */
	public float getRate()
	{
		return this.rate;
	}

	@Override
	protected byte[] getPacketData()
	{
		byte[] data = new byte[ 1 ];
		data[0] = (byte) (int) ( this.rate * 255.0D );

		return data;
	}
}
