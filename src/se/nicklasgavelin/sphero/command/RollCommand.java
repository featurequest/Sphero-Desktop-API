package se.nicklasgavelin.sphero.command;

import se.nicklasgavelin.util.Value;

/**
 * Command to roll the robot in a given heading with a given speed.
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RollCommand extends CommandMessage
{
	private final float heading, velocity;
	private final boolean stop;

	/**
	 * Create a roll command with a given heading, velocity and stop flag
	 * 
	 * @param heading The new heading
	 * @param velocity The new velocity (0-1)
	 * @param stop The new stop flag (false = don't stop, true = stop)
	 */
	public RollCommand( float heading, float velocity, boolean stop )
	{
		super( COMMAND_MESSAGE_TYPE.ROLL );

		this.heading = ( (int) heading % 360 );
		this.velocity = (float) Value.clamp( velocity, 0.0D, 1.0D );
		this.stop = stop;
	}

	/**
	 * Returns the heading set in the command
	 * 
	 * @return The set heading
	 */
	public float getHeading()
	{
		return this.heading;
	}

	/**
	 * Returns the set velocity in the command
	 * 
	 * @return The set velocity
	 */
	public float getVelocity()
	{
		return this.velocity;
	}

	/**
	 * Returns the set stop value for the command
	 * 
	 * @return True if set to stop the Sphero, false otherwise
	 */
	public boolean getStopped()
	{
		return this.stop;
	}

	@Override
	protected byte[] getPacketData()
	{
		byte[] data = new byte[ 4 ];

		data[0] = (byte) (int) ( this.velocity * 255.0D );
		data[1] = (byte) ( (int) this.heading >> 8 );
		data[2] = (byte) (int) this.heading;
		data[3] = (byte) ( this.stop ? 0 : 1 );

		return data;
	}
}
