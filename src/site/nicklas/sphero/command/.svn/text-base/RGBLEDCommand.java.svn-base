package site.nicklas.sphero.command;

import java.awt.Color;
import site.nicklas.sphero.util.Value;

/**
 * Command to adjust the color displayed by the RGB LED on the Sphero.
 *
 * @author Nicklas Gavelin
 */
public class RGBLEDCommand extends DeviceCommand
{
    // Colors
    private byte red, green, blue;


    /**
     * Create a RGBLEDCommand that sets the color values to the
     * ones given.
     *
     * @param red   The new red value
     * @param green The new green value
     * @param blue  The new blue value
     */
    public RGBLEDCommand( int red, int green, int blue )
    {
        super( DEVICE_COMMAND.RGB_LED_OUTPUT );
        this.red = ( byte ) Value.clamp( red, 0, 255 );
        this.green = ( byte ) Value.clamp( green, 0, 255 );;
        this.blue = ( byte ) Value.clamp( blue, 0, 255 );;
    }


    /**
     * Create a RGBLEDCommand from a awt.color instance
     *
     * @param c The color
     */
    public RGBLEDCommand( Color c )
    {
        this( c.getRed(), c.getGreen(), c.getBlue() );
    }


    /**
     * Returns the red value
     *
     * @return The red value
     */
    public int getRed()
    {
        return this.red;
    }


    /**
     * Returns the green value
     *
     * @return The green value
     */
    public int getGreen()
    {
        return this.green;
    }


    /**
     * Returns the blue value
     *
     * @return The blue value
     */
    public int getBlue()
    {
        return this.blue;
    }


    @Override
    protected byte[] getPacketData()
    {
        byte[] data = new byte[ 3 ];
        data[0] = this.red;
        data[1] = this.green;
        data[2] = this.blue;

        return data;
    }
}
