/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package experimental.test;

import experimental.sphero.macro.Delay;
import experimental.sphero.macro.MacroObject;
import experimental.sphero.macro.RGBSD2;
import java.util.Collection;
import se.nicklasgavelin.bluetooth.Bluetooth;
import se.nicklasgavelin.bluetooth.Bluetooth.EVENT;
import se.nicklasgavelin.bluetooth.BluetoothDevice;
import se.nicklasgavelin.bluetooth.BluetoothDiscoveryListener;
import se.nicklasgavelin.sphero.Robot;
import se.nicklasgavelin.sphero.exception.InvalidRobotAddressException;
import se.nicklasgavelin.sphero.exception.RobotBluetoothException;

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
        String id = "000666440DB8";
        Robot r = new Robot(
                new BluetoothDevice(
                new Bluetooth( this, Bluetooth.SERIAL_COM ),
                "btspp://" + id + ":1;authenticate=true;encrypt=false;master=false" ) );

        if ( r.connect() )
        {
            System.out.println( "Connected" );

            MacroObject mo = new MacroObject();
            mo.setRobot( r );
            mo.addCommand( new RGBSD2( 255, 0, 0 ) );
            mo.addCommand( new Delay( 4000 ) );
            mo.addCommand( new RGBSD2( 0, 255, 0 ) );
            mo.addCommand( new Delay( 2000 ) );
//            mo.addCommand( new Roll( 1.0D, 0, 0) );
//            mo.addCommand( new Delay(2000) );
//            mo.addCommand( new Roll( 0D, 0, 0) );
            mo.playMacro();
        }
        else
            System.err.println( "Failed to connect" );
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
