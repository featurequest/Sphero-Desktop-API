package site.nicklas.sphero;

import site.nicklas.sphero.command.*;
import site.nicklas.sphero.util.Value;

/**
 * Handles the basic controls of a robot. See methods for more information
 *
 * @deprecated Not in use any more, use methods in the Robot class instead
 * @author Nicklas Gavelin
 */
public class RobotController
{
    private Robot r;
    // Current values
    private DriveAlgorithm driveAlgorithm;


    /**
     * Create a controller for a given robot
     *
     * @param r The robot to control
     */
    protected RobotController( Robot r )
    {
        this.r = r;
        this.driveAlgorithm = new RCDriveAlgorithm();
    }


    /**
     * Set to another drive algorithm
     *
     * @param algorithm The new drive algorithm
     */
    public void setDriveAlgorithm( DriveAlgorithm algorithm )
    {
        this.driveAlgorithm = algorithm;
    }


    /**
     * Stop both of the robots motors
     */
    public void stopMotors()
    {
        // Update new values
        this.r.sendCommand( new RollCommand( r.getRobotMovement().getHeading(), 0.0F, true ) );
    }


    /**
     * Returns true if the motors are stopped,
     * false otherwise.
     *
     * @return True or false depending on motor status
     */
    public boolean isStopped()
    {
        return !this.r.getRobotMovement().getStop();
    }


    /**
     * Roll the robot with a given heading and speed
     *
     * @param heading The new heading
     * @param speed   The new speed
     */
    public void roll( float heading, float speed )
    {
        // Send the command
        this.r.sendCommand( new RollCommand( heading, speed, false ) );
    }


    /**
     * Drive in a direction
     *
     * @param x X direction
     * @param y Y direction
     * @param z Z direction
     */
    public void drive( double x, double y, double z )
    {
        // Convert the values to the correct ones depending on the given algorithm
        this.driveAlgorithm.convert( x, y, z );
        this.driveAlgorithm.adjustHeading();

        // Cap the value
        this.driveAlgorithm.adjustedHeading = Value.clamp( this.driveAlgorithm.adjustedHeading, 0.0D, 359.0D );

        // Send the command
        this.roll( ( float ) this.driveAlgorithm.adjustedHeading, ( float ) this.driveAlgorithm.speed );
    }


    /**
     * Rotate the robot
     *
     * @param heading The new heading, 0-360
     */
    public void rotate( float heading )
    {
        this.roll( heading, 0.0F );
    }


    /**
     * Send a versioning message to the robot
     */
    public void versioning()
    {
        this.r.sendCommand( new VersioningCommand() );
    }


    public void boost()
    {
        // TODO: Implement macro & boost
    }


    /**
     * Jump the robot to the bootloader part
     */
    public void jumpToBootloader()
    {
        this.r.sendCommand( new JumpToBootloaderCommand() );
    }


    /**
     * Send a sleep command to the robot.
     * The sleep time is given in seconds.
     *
     * @param time Number of seconds to sleep. The connection WILL be LOST to
     *             the robot and have to be re-initialized.
     */
    public void sleep( int time )
    {
        this.r.sendCommand( new SleepCommand( time ) );
    }


    /**
     * Update the robot rotation rate
     *
     * @param rotationRate The new rotation rate, value 0-1
     */
    public void setRotationRate( float rotationRate )
    {
        this.r.sendCommand( new RotationRateCommand( rotationRate ) );
    }


    /**
     * Set a new RGB color for the robot RGB led
     *
     * @param red   The new red value
     * @param green The new green value
     * @param blue  The new blue value
     */
    public void setRGBLEDColor( int red, int green, int blue )
    {
        this.r.sendCommand( new RGBLEDCommand( red, green, blue ) );
    }


    /**
     * Resets the robots heading
     */
    public void resetHeading()
    {
        this.r.sendCommand( new CalibrateCommand( 0.0F ) );
    }


    /**
     * Update heading offset
     *
     * @param offset The heading offset
     */
    public void setHeadingOffset( double offset )
    {
        this.driveAlgorithm.headingOffset = offset;
    }


    /**
     * Set brightness of the front led. 0-1
     *
     * @param brightness The brightness value, 0-1
     */
    public void setFrontLEDBrightness( float brightness )
    {
        this.r.sendCommand( new FrontLEDCommand( brightness ) );
    }


    /**
     * Set the name of the robot
     *
     * @param name The new name
     */
    public void setRobotName( String name )
    {
        this.r.sendCommand( new SetRobotNameCommand( name ) );
    }


    /**
     * Turn on/off stabilization
     *
     * @param on True for on, false for off
     */
    public void stabilization( boolean on )
    {
        this.r.sendCommand( new StabilizationCommand( on ) );
    }
}
