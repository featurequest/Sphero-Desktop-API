/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.macro.command;

import se.nicklasgavelin.sphero.command.RawMotorCommand;
import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Value;

/**
 * Raw motor macro command
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class RawMotor extends MacroCommand
{
	public static final int MIN_SPEED = 0, MAX_SPEED = 255, MIN_DELAY = 0,
			MAX_DELAY = 255;
	private RawMotorCommand.MOTOR_MODE leftMode, rightMode;
	private int leftSpeed, rightSpeed, delay;

	/**
	 * Create a raw motor macro with given headings for each of the two motors and a given
	 * speed.
	 * The initial delay before execution is also customizable.
	 * 
	 * @param _leftMode The left motor mode
	 * @param _leftSpeed The left motor speed (MIN_SPEED - MAX_SPEEd)
	 * @param _rightMode The right motor mode
	 * @param _rightSpeed The right motor speed (MIN_SPEED - MAX_SPEED)
	 * @param _delay The initial delay before executing the macro (MIN_DELAY - MAX_DELAY)
	 */
	public RawMotor( RawMotorCommand.MOTOR_MODE _leftMode, int _leftSpeed, RawMotorCommand.MOTOR_MODE _rightMode, int _rightSpeed, int _delay )
	{
		super( MACRO_COMMAND.MAC_RAW_MOTOR );
		this.setMotorModes( _leftMode, _rightMode );
		this.setMotorSpeed( _leftSpeed, _rightSpeed );
		this.setDelay( _delay );
	}

	/**
	 * Create a raw motor macro with given headings for each of the two motors and a given
	 * speed.
	 * 
	 * @param _leftMode The left motor mode
	 * @param _leftSpeed The left motor speed (MIN_SPEED - MAX_SPEEd)
	 * @param _rightMode The right motor mode
	 * @param _rightSpeed The right motor speed (MIN_SPEED - MAX_SPEED)
	 */
	public RawMotor( RawMotorCommand.MOTOR_MODE _leftMode, int _leftSpeed, RawMotorCommand.MOTOR_MODE _rightMode, int _rightSpeed )
	{
		super( MACRO_COMMAND.MAC_RAW_MOTOR );
		this.setMotorModes( _leftMode, _rightMode );
		this.setMotorSpeed( _leftSpeed, _rightSpeed );
		this.setDelay( 0 );
	}

	/**
	 * Update the internal delay
	 * 
	 * @param _delay The new internal initial delay before execution
	 */
	public void setDelay( int _delay )
	{
		this.delay = Value.clamp( _delay, MIN_DELAY, MAX_DELAY );
	}

	/**
	 * Update the internal motor modes for the macro command
	 * 
	 * @param _leftMode The new left motor mode
	 * @param _rightMode The new right motor mode
	 */
	public void setMotorModes( RawMotorCommand.MOTOR_MODE _leftMode, RawMotorCommand.MOTOR_MODE _rightMode )
	{
		this.leftMode = _leftMode;
		this.rightMode = _rightMode;
	}

	/**
	 * Update the motor speeds
	 * 
	 * @param _leftSpeed The new left motor speed (MIN_SPEED - MAX_SPEED)
	 * @param _rightSpeed The new right motor speed (MIN_SPEED - MAX_SPEED)
	 */
	public void setMotorSpeed( int _leftSpeed, int _rightSpeed )
	{
		this.leftSpeed = Value.clamp( _leftSpeed, MIN_SPEED, MAX_SPEED );
		this.rightSpeed = Value.clamp( _rightSpeed, MIN_SPEED, MAX_SPEED );
	}

	@Override
	public byte[] getByteRepresentation()
	{
		ByteArrayBuffer bab = new ByteArrayBuffer( getLength() );
		bab.append( getCommandID() );
		bab.append( this.leftMode.getValue() );
		bab.append( this.leftSpeed );
		bab.append( this.rightMode.getValue() );
		bab.append( this.rightSpeed );
		bab.append( this.delay );

		return bab.toByteArray();
	}
}
