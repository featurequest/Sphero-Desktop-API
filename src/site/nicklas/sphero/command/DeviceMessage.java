package site.nicklas.sphero.command;

import java.util.Date;

/**
 * An abstract class for device commands and responses
 * 
 * @author Orbotix
 */
public abstract class DeviceMessage
{
    private final Date timestamp;

    /**
     * Create a device message
     */
    public DeviceMessage()
    {
        this.timestamp = new Date();
    }
    
    /**
     * Returns the timestamp for when the message
     * was created
     * 
     * @return The timestamp as a date object
     */
    public Date getTimestamp()
    {
        return this.timestamp;
    }
}