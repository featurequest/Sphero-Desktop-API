package se.nicklasgavelin.sphero.command;

/**
 * Command to configure collision detection.
 * 
 * @author John Sichi, jsichi@gmail.com
 */
public class ConfigureCollisionDetectionCommand extends CommandMessage
{
    private final int meth, xThreshold, xSpeed, yThreshold, ySpeed, deadTime;

    /**
     * Returns the method set in the command
     *
     * @return the set method
     */
    public int getMethod()
    {
        return this.meth;
    }
    
    /**
     * Returns the X threshold set in the command
     *
     * @return the set X threshold
     */
    public int getXThreshold()
    {
        return this.xThreshold;
    }
    
    /**
     * Returns the X speed set in the command
     *
     * @return the set X speed
     */
    public int getXSpeed()
    {
        return this.xSpeed;
    }
    
    /**
     * Returns the Y threshold set in the command
     *
     * @return the set Y threshold
     */
    public int getYThreshold()
    {
        return this.yThreshold;
    }
    
    /**
     * Returns the Y speed set in the command
     *
     * @return the set Y speed
     */
    public int getYSpeed()
    {
        return this.ySpeed;
    }
    
    /**
     * Returns the dead time set in the command
     *
     * @return the set dead time
     */
    public int getDeadTime()
    {
        return this.deadTime;
    }
    
    /**
     * Create a collision detection command
     *
     * @param meth Detection method type (0 to disable, 1 for default detection
     * mtehod)
     * @param xThreshold An 8-bit settable threshold for the X (left/right)
     * axis of Sphero. A value of 0 disables the contribution of that axis.
     * @param xSpeed An 8-bit settable speed value for the X axis. This setting
     * is ranged by the speed, then added to xThreshold to generate the final
     * threshold value.
     * @param yThreshold An 8-bit settable threshold for the Y (front/back)
     * axis of Sphero. A value of 0 disables the contribution of that axis.
     * @param ySpeed An 8-bit settable speed value for the Y axis. This setting
     * is ranged by the speed, then added to yThreshold to generate the final
     * threshold value.
     * @param deadTime An 8-bit post-collision dead time to prevent
     * retriggering; specified in 10ms increments.
     */
    public ConfigureCollisionDetectionCommand( int meth, int xThreshold, int xSpeed, int yThreshold, int ySpeed, int deadTime )
    {
        super( COMMAND_MESSAGE_TYPE.CONFIGURE_COLLISION_DETECTION );
        this.meth = meth;
        this.xThreshold = xThreshold;
        this.xSpeed = xSpeed;
        this.yThreshold = yThreshold;
        this.ySpeed = ySpeed;
        this.deadTime = deadTime;
    }

    @Override
    protected byte[] getPacketData()
    {
        byte[] data = 
            {
                ( byte ) this.meth,
                ( byte ) this.xThreshold,
                ( byte ) this.xSpeed,
                ( byte ) this.yThreshold,
                ( byte ) this.ySpeed,
                ( byte ) this.deadTime
            };

        return data;
    }
}
