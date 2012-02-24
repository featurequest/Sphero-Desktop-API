/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package experimental.test;

import java.util.Collection;
import se.nicklasgavelin.bluetooth.Bluetooth;
import se.nicklasgavelin.bluetooth.Bluetooth.EVENT;
import se.nicklasgavelin.bluetooth.BluetoothDevice;
import se.nicklasgavelin.bluetooth.BluetoothDiscoveryListener;
import se.nicklasgavelin.sphero.Robot;
import se.nicklasgavelin.sphero.command.RawMotorCommand;
import se.nicklasgavelin.sphero.exception.InvalidRobotAddressException;
import se.nicklasgavelin.sphero.exception.RobotBluetoothException;
import se.nicklasgavelin.sphero.macro.Delay;
import se.nicklasgavelin.sphero.macro.MacroObject;
import se.nicklasgavelin.sphero.macro.RawMotor;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class Experimental_Main implements BluetoothDiscoveryListener
{
    public static void main( String[] args ) throws InvalidRobotAddressException, RobotBluetoothException
    {
        Experimental_Main experimental_Main = new Experimental_Main();
    }


    private Experimental_Main() throws InvalidRobotAddressException, RobotBluetoothException
    {
        String id = "00066644390F";// ( - | x | - ) // "000666440DB8"; // ( x | - | - )
        Robot r = new Robot(
                new BluetoothDevice(
                new Bluetooth( this, Bluetooth.SERIAL_COM ),
                "btspp://" + id + ":1;authenticate=true;encrypt=false;master=false" ) );

        if ( r.connect() )
        {
            System.out.println( "Connected" );
            r.stopMacro();
            MacroObject mo = this.createSwingMotionMacro( 255, 1, 50 );
            mo.setMode( MacroObject.MacroObjectMode.CachedStreaming );
            r.sendCommand( mo );
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
}
