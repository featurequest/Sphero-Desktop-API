package site.nicklas.sphero.util;

/**
 * @author Orbotix
 */
public class Value
{
    public static double clamp( double value, double min, double max )
    {
        if ( value > max )
            return max;
        if ( value < min )
        {
            return min;
        }
        return value;
    }


    public static int clamp( int value, int min, int max )
    {
        if ( value > max )
            return max;
        if ( value < min )
        {
            return min;
        }
        return value;
    }


    public static double window( double value, double windowValue, double delta )
    {
        if ( (Math.abs( value ) > Math.abs( windowValue ) - delta) && (Math.abs( value ) < Math.abs( windowValue ) + delta) )
        {
            return windowValue;
        }
        return value;
    }
}