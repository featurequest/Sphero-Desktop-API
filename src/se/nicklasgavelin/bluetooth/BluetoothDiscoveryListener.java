package se.nicklasgavelin.bluetooth;

import java.util.Collection;
import se.nicklasgavelin.bluetooth.Bluetooth.EVENT;

/**
 * Used with the "Bluetooth" class to listen for updates regarding the device
 * search that may be performed. Will return events if the device search fails
 * or if the device search is completed. If the device search is completed
 * the devices detected will also be returned.
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 * 
 */
public interface BluetoothDiscoveryListener
{
	/**
	 * Called when the search is completed
	 * 
	 * @param devices The devices that have been discovered
	 */
	public void deviceSearchCompleted( Collection<BluetoothDevice> devices );

	/**
	 * Called when a search results in a discovered device.
	 * Each device discovered will result in a call to this method
	 * 
	 * @param device The device discovered
	 */
	public void deviceDiscovered( BluetoothDevice device );

	/**
	 * Called when something went wrong during a discovery search
	 * 
	 * @param error The error object that describes the error that occurred
	 */
	public void deviceSearchFailed( EVENT error );

	/**
	 * Called when the device search has been started
	 */
	public void deviceSearchStarted();
}
