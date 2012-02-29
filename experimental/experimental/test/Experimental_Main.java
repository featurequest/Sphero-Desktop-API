/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package experimental.test;

import java.util.Collection;
import java.util.logging.Logger;
import se.nicklasgavelin.bluetooth.Bluetooth;
import se.nicklasgavelin.bluetooth.Bluetooth.EVENT;
import se.nicklasgavelin.bluetooth.BluetoothDevice;
import se.nicklasgavelin.bluetooth.BluetoothDiscoveryListener;
import se.nicklasgavelin.log.Logging;
import se.nicklasgavelin.sphero.Robot;
import se.nicklasgavelin.sphero.RobotListener;
import se.nicklasgavelin.sphero.command.CommandMessage;
import se.nicklasgavelin.sphero.command.RawMotorCommand;
import se.nicklasgavelin.sphero.command.SetDataStreamingCommand;
import se.nicklasgavelin.sphero.exception.InvalidRobotAddressException;
import se.nicklasgavelin.sphero.exception.RobotBluetoothException;
import se.nicklasgavelin.sphero.macro.Delay;
import se.nicklasgavelin.sphero.macro.MacroObject;
import se.nicklasgavelin.sphero.macro.RawMotor;
import se.nicklasgavelin.sphero.response.ResponseMessage;
import se.nicklasgavelin.sphero.response.information.DataResponse;
import se.nicklasgavelin.sphero.response.information.DeviceInformationResponse;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class Experimental_Main implements BluetoothDiscoveryListener, RobotListener
{
    /**
     * Main method for experimental stuff
     *
     * @param args All arguments are ignored
     *
     * @throws InvalidRobotAddressException If the address for the robot is
     *                                      invalid
     * @throws RobotBluetoothException      If there occurs a Bluetooth
     *                                      exception during connecting
     */
    public static void main( String[] args ) throws InvalidRobotAddressException, RobotBluetoothException
    {
//        String data = "2 82 9 -107 -2 1 7 -1 114 0 0 11 0 56 7 -1 116 0 0 11 0 57 7 -1 118 0 0 11 0 58 7 -1 120 0 0 11 0 59 7 -1 122 0 0 11 0 60 7 -1 124 0 0 11 0 61 7 -1 126 0 0 11 0 62 7 -1 -127 0 0 11 0 63 7 -1 -125 0 0 11 0 64 7 -1 -123 0 0 11 0 65 7 -1 -121 0 0 11 0 66 7 -1 -119 0 0 11 0 67 7 -1 -117 0 0 11 0 68 7 -1 -115 0 0 11 0 69 7 -1 -113 0 0 11 0 70 7 -1 -111 0 0 11 0 71 7 -1 -109 0 0 11 0 72 7 -1 -107 0 0 11 0 73 21 1";// -4";
//        System.out.println( calculateChecksum( data ) );
        Experimental_Main experimental_Main = new Experimental_Main();
    }



    private static byte calculateChecksum( String data )
    {
        String[] s = data.split( " " );

        int checksum = 0;
        for ( String b : s )
        {
            byte by = Byte.parseByte( b );
            checksum += by;
        }

        return ( byte ) (checksum ^ 0xFFFFFFFF);
    }


    private Experimental_Main() throws InvalidRobotAddressException, RobotBluetoothException
    {
        Logging.debug( "test" );
        String id = "00066644390F";// ( - | x | - ) // "000666440DB8"; // ( x | - | - )
        Robot r = new Robot(
                new BluetoothDevice(
                new Bluetooth( this, Bluetooth.SERIAL_COM ),
                "btspp://" + id + ":1;authenticate=true;encrypt=false;master=false" ) );

        if ( r.connect() )
        {
            r.addListener( this );
            System.out.println( "Connected" );
            r.sendCommand( new RawMotorCommand( RawMotorCommand.MOTOR_MODE.FORWARD, 0, RawMotorCommand.MOTOR_MODE.FORWARD, 0 ) );
            SetDataStreamingCommand sds = new SetDataStreamingCommand( 100, 1, SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.ALL.RAW, 65534 );
            r.sendCommand( new SetDataStreamingCommand(
                    100,
                    1,
                    65534 ) );




//            r.sendCommand( new SetDataStreamingCommand( 100, 1, SetDataStreamingCommand.DATA_STREAMING_MASK_ACCELEROMETER_X_RAW | SetDataStreamingCommand.DATA_STREAMING_MASK_ACCELEROMETER_Y_RAW | SetDataStreamingCommand.DATA_STREAMING_MASK_ACCELEROMETER_Z_RAW, 65534 ) );
//            r.stopMacro();
//            r.sendCommandAfterMacro( new RGBLEDCommand( Color.BLUE ) );
//            r.rgbTransition( Color.RED, Color.BLUE, 300, 25 );

            //MacroObject mo = this.createSwingMotionMacro( 255, 1, 50 );
            //mo.setMode( MacroObject.MacroObjectMode.CachedStreaming );
            //r.sendCommand( mo );
//            MacroObject mo = new MacroObject();
//            mo.addCommand( new RawMotor( RawMotorCommand.MOTOR_MODE.FORWARD, 100, RawMotorCommand.MOTOR_MODE.FORWARD, 100 ) );
//            mo.addCommand( new Delay( 5000 ) );
//            mo.addCommand( new RawMotor( RawMotorCommand.MOTOR_MODE.FORWARD, 0, RawMotorCommand.MOTOR_MODE.FORWARD, 0 ) );
//            r.sendCommand( mo );
        }
        else
            System.err.println( "Failed to connect" );
    }


    private int calcspeed( double f, float maxSpeed )
    {
        return ( int ) (Math.sin( (f % (Math.PI / 2)) + (Math.PI / 2) ) * maxSpeed);
    }


    public MacroObject createSwingMotionMacro( int _maxSpeed, int dDelay, int nSteps )
    {
        float maxSpeed = ( float ) _maxSpeed;
        int n = nSteps;
        int delay = dDelay;
        double PI = Math.PI;
        double maxValue = 2 * PI + (PI / 2);
        double incVal = (maxValue / n);

        RawMotorCommand.MOTOR_MODE mm = RawMotorCommand.MOTOR_MODE.FORWARD; // : RawMotorCommand.MOTOR_MODE.REVERSE );
        MacroObject mo = new MacroObject();

        for ( int i = 0; i < n; i++ )
        {
            int speed = calcspeed( i * incVal, maxSpeed );
            if ( speed == _maxSpeed )
            {
                switch ( mm )
                {
                    case FORWARD:
                        mm = RawMotorCommand.MOTOR_MODE.REVERSE;
                        break;
                    case REVERSE:
                        mm = RawMotorCommand.MOTOR_MODE.FORWARD;
                        break;
                }
            }

            mo.addCommand( new RawMotor( mm, speed, mm, speed ) );
            mo.addCommand( new Delay( delay ) );
        }

        mo.addCommand( new RawMotor( RawMotorCommand.MOTOR_MODE.FORWARD, 0, RawMotorCommand.MOTOR_MODE.FORWARD, 0 ) );

        return mo;
    }


    @Override
    public void deviceSearchCompleted( Collection<BluetoothDevice> devices )
    {
    }


    @Override
    public void deviceDiscovered( BluetoothDevice device )
    {
    }


    @Override
    public void deviceSearchFailed( EVENT error )
    {
    }


    @Override
    public void deviceSearchStarted()
    {
    }


    @Override
    public void responseReceived( Robot r, ResponseMessage response, CommandMessage dc )
    {
    }


    @Override
    public void event( Robot r, EVENT_CODE code )
    {
    }


    @Override
    public void informationResponseReceived( Robot r, DeviceInformationResponse response )
    {
        if ( response instanceof DataResponse )
        {
            DataResponse dr = ( DataResponse ) response;
            byte[] data = dr.getSensorData();
        }
    }
}
