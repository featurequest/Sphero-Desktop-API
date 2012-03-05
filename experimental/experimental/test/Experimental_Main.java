/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package experimental.test;

import experimental.sensor.AccelerometerSensorData;
import experimental.sensor.TouchSensor;
import java.awt.Color;
import java.util.Collection;
import java.util.logging.Level;
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
import se.nicklasgavelin.sphero.macro.command.Delay;
import se.nicklasgavelin.sphero.macro.MacroObject;
import se.nicklasgavelin.sphero.macro.command.RawMotor;
import se.nicklasgavelin.sphero.macro.command.*;
import se.nicklasgavelin.sphero.response.ResponseMessage;
import se.nicklasgavelin.sphero.response.information.DataResponse;
import se.nicklasgavelin.sphero.response.InformationResponseMessage;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class Experimental_Main implements BluetoothDiscoveryListener, RobotListener, TouchSensor.TouchListener
{
    /**
     * Main method for experimental stuff
     *
     * @param args All arguments are ignored
     *
     * @throws InvalidRobotAddressException If the address for the robot is
     * invalid
     * @throws RobotBluetoothException      If there occurs a Bluetooth
     * exception during connecting
     */
    public static void main( String[] args ) throws InvalidRobotAddressException, RobotBluetoothException
    {
//        String data = "2 82 9 -107 -2 1 7 -1 114 0 0 11 0 56 7 -1 116 0 0 11 0 57 7 -1 118 0 0 11 0 58 7 -1 120 0 0 11 0 59 7 -1 122 0 0 11 0 60 7 -1 124 0 0 11 0 61 7 -1 126 0 0 11 0 62 7 -1 -127 0 0 11 0 63 7 -1 -125 0 0 11 0 64 7 -1 -123 0 0 11 0 65 7 -1 -121 0 0 11 0 66 7 -1 -119 0 0 11 0 67 7 -1 -117 0 0 11 0 68 7 -1 -115 0 0 11 0 69 7 -1 -113 0 0 11 0 70 7 -1 -111 0 0 11 0 71 7 -1 -109 0 0 11 0 72 7 -1 -107 0 0 11 0 73 21 1";// -4";
//        System.out.println( calculateChecksum( data ) );
        Experimental_Main experimental_Main = new Experimental_Main();
    }
    private TouchSensor s;


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
    private int delay = 25;
    private int steps = 100;


    private Experimental_Main() throws InvalidRobotAddressException, RobotBluetoothException
    {
        Logging.debug( "test" );
        String id = "00066644390F" /* x - - */;
//        String id = "000666440DB8" /* - - x */;

        Robot r = new Robot(
                new BluetoothDevice(
                new Bluetooth( this, Bluetooth.SERIAL_COM ),
                "btspp://" + id + ":1;authenticate=true;encrypt=false;master=false" ) );

        Logger.getLogger( Experimental_Main.class.getName() ).log( Level.INFO, "Trying to connect to robot" );
        if ( r.connect() )
        {
            r.addListener( this );
            Logger.getLogger( Experimental_Main.class.getName() ).log( Level.INFO, "Connected to robot" );

            s = new TouchSensor( r );
            s.addTouchListener( this );
            startStream( r );

//            MacroObject mo = new MacroObject();
//            mo.setMode( MacroObject.MacroObjectMode.CachedStreaming );
//            mo.addCommand( new RGB( Color.WHITE, 0 ) );
//            mo.addCommand( new Fade( 255, 0, 0, 500 ) );
//            mo.addCommand( new Delay( 1000 ) );
//
//            mo.addCommand( new Fade( 0, 255, 0, 500 ) );
//            mo.addCommand( new Delay( 1000 ) );
//
//            mo.addCommand( new Fade( 0, 0, 255, 500 ) );
//            mo.addCommand( new Delay( 1000 ) );
//
//            r.sendCommand( mo );

//            for( int i = 0; i < 1000; i++ )
//            {
//                Color from = se.nicklasgavelin.util.Color.fromHex( "33FF00" );
//             &   Color to = se.nicklasgavelin.util.Color.fromHex( "FF8900" );
//                r.rgbTransition( from, to, steps, delay );
//                r.rgbTransition( to, from, steps, delay );
//
//                try
//                {
//                    Thread.sleep(1000);
//                }
//                catch ( InterruptedException ex )
//                {
//                    Logger.getLogger( Experimental_Main.class.getName() ).log( Level.SEVERE, null, ex );
//                }
//            }

//            r.sendCommand( new RawMotorCommand( RawMotorCommand.MOTOR_MODE.FORWARD, 0, RawMotorCommand.MOTOR_MODE.FORWARD, 0 ) );
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
    private int p = 0;


    @Override
    public void event( Robot r, EVENT_CODE code )
    {
        System.out.println( "Macro done" );
//
//        switch( code )
//        {
//            case MACRO_DONE:
//                if( p == 0 )
//                {
//                    r.rgbTransition( Color.RED, Color.BLUE, steps, delay );
////                    r.sendCommandAfterMacro( new RGBLEDCommand( Color.BLUE ) );
//                }
//                else if( p == 1 )
//                {
//                    r.rgbTransition( Color.BLUE, Color.RED, steps, delay );
////                    r.sendCommandAfterMacro( new RGBLEDCommand( Color.RED ) );
//                }
//                else if( p == 2 )
//                {
//                    r.rgbTransition( Color.RED, Color.BLACK, steps, delay );
////                    r.sendCommandAfterMacro( new RGBLEDCommand( Color.BLACK ) );
//                }
//
//                p = (++p % 3);
//            break;
//        }
    }


    private void startStream( Robot r )
    {
        SetDataStreamingCommand sds = new SetDataStreamingCommand( 4, 1, SetDataStreamingCommand.DATA_STREAMING_MASKS.GYRO.ALL.RAW, 100 ); //new SetDataStreamingCommand( 1, 1, SetDataStreamingCommand.DATA_STREAMING_MASKS.ACCELEROMETER.ALL.RAW, 65534 );
        r.sendCommand( sds );
    }
    private int co = 0;


    @Override
    public void informationResponseReceived( Robot r, InformationResponseMessage response )
    {
        if ( response instanceof DataResponse )
        {
            co++;

            if ( co > 90 )
            {
                startStream( r );
                co = 0;
            }

            DataResponse dr = ( DataResponse ) response;
            byte[] data = dr.getSensorData();

            int x = (data[1] | (data[0] << 8));
            int y = (data[3] | (data[2] << 8));
            int z = (data[5] | (data[4] << 8));

//            System.out.println( "X=" + x + ", Y=" + y + ", Z=" + z );
            s.addData( new AccelerometerSensorData( x, y, z ) );
        }
    }


    @Override
    public void touchEvent( Robot r )
    {
        int d = 500, k = 250;
        MacroObject mo = new MacroObject();
        mo.setMode( MacroObject.MacroObjectMode.CachedStreaming );
        mo.addCommand( new RGB( Color.WHITE, 0 ) );
        mo.addCommand( new Fade( 255, 0, 0, k ) );
        mo.addCommand( new Delay( d ) );

        mo.addCommand( new Fade( 0, 255, 0, k ) );
        mo.addCommand( new Delay( d ) );

        mo.addCommand( new Fade( 0, 0, 255, k ) );
        mo.addCommand( new Delay( d ) );

        r.sendCommand( mo );
    }
}
