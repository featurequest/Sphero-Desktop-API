/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package experimental.sensor;

/**
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class AccelerometerSensorData extends SensorData
{
    private AccelerometerAcceleration acceleration;
    private SensorData.Axis3Sensor accelerationRaw;
    private SensorData.Axis3SensorState state;


    public AccelerometerSensorData( int x, int y, int z )
    {
        super();
        this.accelerationRaw = new Axis3Sensor( x, y, z );

//        int x = (data[1] | (data[0] << 8));
//        int y = (data[3] | (data[2] << 8));
//        int z = (data[5] | (data[4] << 8));
//
//        AccelerometerSensorData as = new AccelerometerSensorData( x, y, z );
//        SensorData.Axis3Sensor raw = as.getAxis3Sensor();
//        System.err.println( "x=" + raw.getX() + ", y=" + raw.getY() + ", z=" + raw.getZ() );

        // TODO: Calculate acceleration
    }


    public Axis3Sensor getAxis3Sensor()
    {
        return this.accelerationRaw;//new Vector3D( this.accelerationRaw.x, this.accelerationRaw.y, this.accelerationRaw.z );
    }


    public AccelerometerAcceleration getAcceleration()
    {
        return this.acceleration;
    }

    public class AccelerometerAcceleration
    {
        float x, y, z;


        private AccelerometerAcceleration( float x, float y, float z )
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
