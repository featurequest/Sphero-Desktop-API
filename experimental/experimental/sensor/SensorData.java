/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package experimental.sensor;

import java.util.Date;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class SensorData
{
    private Date timestamp;


    public SensorData()
    {
        this.timestamp = new Date();
    }


    public Date getTimestamp()
    {
        return this.timestamp;
    }


    /* ************
     * INNER CLASSES
     */
    public class Axis3Sensor
    {
        protected int x, y, z;


        public Axis3Sensor( int x, int y, int z )
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }


        public int getX()
        {
            return this.x;
        }


        public int getY()
        {
            return this.y;
        }


        public int getZ()
        {
            return this.z;
        }


        public double[] normalized()
        {
            double R = Math.sqrt( (Math.pow( x, 2 ) + Math.pow( y, 2 ) + Math.pow( z, 2 )) );
            return new double[]
                    {
                        (( double ) x) / R, (( double ) y) / R, (( double ) z) / R
                    };
        }
    }

    public class Axis3SensorState
    {
        protected boolean xValid, yValid, zValid;
    }
}
