package se.nicklasgavelin.bluetooth;

import java.io.IOException;
import javax.bluetooth.*;
import se.nicklasgavelin.log.Logging;
import se.nicklasgavelin.sphero.exception.RobotBluetoothException;

/**
 * Manages a single bluetooth device
 * Masks the "RemoteDevice" class as extra methods are implemented
 * to make it easier to communicate with the given device
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 * 
 */
public class BluetoothDevice implements DiscoveryListener
{
	// Internal storage
	private RemoteDevice device;
	private final Bluetooth bt;
	private BluetoothService service;
	private int activeSearch;
	private String name = null;
	private String address = null;
	private String connectionUrl = null;
	private static final String UNKNOWN_NAME = "(UNKNOWN)";

	/**
	 * Create a bluetooth device using the "Bluetooth bt" to connect to it
	 * 
	 * @param bt The bluetooth connection
	 * @param device The remote device
	 */
	protected BluetoothDevice( Bluetooth bt, RemoteDevice device )
	{
		this.bt = bt;
		this.device = device;
		this.activeSearch = -1;
		this.address = device.getBluetoothAddress();

		try
		{
			// Try to fetch the name for the device
			this.setName( this.device.getFriendlyName( false ) );
		}
		catch( IOException e )
		{
			this.setName( UNKNOWN_NAME );
		}
	}

	/**
	 * Create a bluetooth device from a bluetooth class instance and
	 * a connection url to a specific device.
	 * WARNING: When using this constructor some of the methods
	 * that this class posess will return erroneous data or throw exceptions.
	 * 
	 * @param bt The bluetooth instance
	 * @param connectionUrl The device connection url
	 */
	public BluetoothDevice( Bluetooth bt, String connectionUrl )
	{
		this.bt = bt;
		this.connectionUrl = connectionUrl;
		this.address = this.connectionUrl.split( "://" )[1].split( ":" )[0];
	}

	/**
	 * Returns the connection url for the specific bluetooth device
	 * and service.
	 * 
	 * @return The bluetooth connection url
	 */
	public String getConnectionURL()
	{
		if( this.connectionUrl != null )
			return this.connectionUrl;
		try
		{
			return this.service.getConnectionURL();
		}
		catch( NullPointerException e )
		{
			return null;
		}
	}

	/**
	 * Set the name of the device
	 * 
	 * @param name The name of the device
	 */
	private void setName( String name )
	{
		this.name = name;
	}

	/**
	 * Returns the Bluetooth address of the device
	 * 
	 * @return The Bluetooth address
	 */
	public String getAddress()
	{
		return this.address;
	}

	/**
	 * Returns the name of the device or the value of UNKNOWN_NAME
	 * if no name could be found.
	 * 
	 * @return The name of the device or the value of
	 *         BluetoothDevice.UNKNOWN_NAME
	 */
	public String getName()
	{
		if( this.name == null && this.device != null )
		{
			try
			{
				// Try and updat the name
				this.name = this.device.getFriendlyName( false );
			}
			catch( IOException e )
			{
				this.name = UNKNOWN_NAME;
			}
		}

		return this.name;
	}

	/**
	 * Returns the remote device,
	 * this remote device shouldn't be used except
	 * when absolutely necessary as the BluetoothDevice
	 * class should handle all communications with the RemoteDevice class
	 * instance.
	 * 
	 * @return The remote device (RemoteDevice)
	 */
	public RemoteDevice getRemoteDevice()
	{
		return this.device;
	}

	/**
	 * Ask the device for the bluetooth name and update
	 * it internally when a response is received
	 */
	public void updateName()
	{
		// Request name
		try
		{
			if( this.device != null )
				this.setName( this.device.getFriendlyName( false ) );
		}
		catch( IOException e )
		{
			error( "Failed to update bluetooth device name: " + e.getMessage() );
		}
	}

	/**
	 * Connect to the Bluetooth device and return the created connection
	 * or null if no connection could be made.
	 * 
	 * @throws RobotBluetoothException If failure to connect
	 * @return The created Bluetooth connection or null if no connection could be created
	 */
	public BluetoothConnection connect() throws RobotBluetoothException
	{
		// Check if we have any active services
		if( this.connectionUrl != null )
		{
			// Fetch a new service
			this.service = new BluetoothService( this.connectionUrl );

			if( this.service == null )
				return null;
		}
		else if( this.service == null )
		{
			// Force discovery
			this.discover();

			// Check if we found anything
			if( this.service == null )
				return null;
		}

		// Connect to the available service
		try
		{
			// Connect to the service
			return this.service.connect();
		}
		catch( IOException e )
		{
			// Failure to connect for some reason
			return null;
		}
	}

	/**
	 * Start with discovering services available for this Bluetooth device.
	 * 
	 * @throws RobotBluetoothException If failure to perform device discovert
	 */
	public void discover() throws RobotBluetoothException
	{
		// Check if we have tried with discovery earlier
		if( this.activeSearch < 0 )
		{
			try
			{
				// See to it that we are the only one performing stuff on the bt
				// instance.
				synchronized( this.bt )
				{
					// Serh for available services for this device
					this.activeSearch = this.bt.getDiscoveryAgent().searchServices( new int[] { BluetoothService.ATTR_SERVICENAME, BluetoothService.ATTR_SERVICEDESC, BluetoothService.ATTR_PROVIDERNAME }, new javax.bluetooth.UUID[] { this.bt.getUUID() }, this.device, this );

					// Lock until we are done
					this.bt.wait();
				}
			}
			catch( BluetoothStateException e )
			{
				throw new RobotBluetoothException( e.getMessage() );
			}
			catch( InterruptedException e )
			{
				// Failure to discover
				throw new RobotBluetoothException( e.getMessage() );
			}
		}
	}

	/**
	 * Cancel an active service discovery
	 */
	public void cancelDiscovery()
	{
		// Cancel any active discovery searches
		if( this.activeSearch >= 0 )
		{
			this.bt.getDiscoveryAgent().cancelServiceSearch( this.activeSearch );
			activeSearch = -1;
		}
	}

	/**
	 * Called when the service search is completed
	 * 
	 * @param transId -
	 * @param respCode -
	 */
	@Override
	public void serviceSearchCompleted( int transId, int respCode )
	{
		// Notify observers
		synchronized( this.bt )
		{
			if( this.activeSearch == transId )
				this.bt.notifyAll();
		}

		// Check the response code
		switch ( respCode )
		{
			case DiscoveryListener.SERVICE_SEARCH_COMPLETED:
				log( "The service search completed normally" );
				break;
			case DiscoveryListener.SERVICE_SEARCH_TERMINATED:
				log( "The service search request was cancelled by a call to DiscoveryAgent.cancelServiceSearch(int)" );
				break;
			case DiscoveryListener.SERVICE_SEARCH_ERROR:
				log( "An error occurred while processing the request" );
				break;
			case DiscoveryListener.SERVICE_SEARCH_NO_RECORDS:
				log( "No records were found during the service search" );
				break;
			case DiscoveryListener.SERVICE_SEARCH_DEVICE_NOT_REACHABLE:
				log( "The device specified in the search request could not be reached or the local device could not establish a connection to the remote device" );
				break;
			default:
				log( "Unknown Response Code - " + respCode );
				break;
		}
	}

	/**
	 * Called when a new service is discovered
	 * NOTICE: Only takes the first available service
	 * 
	 * TODO: add functionality for more services
	 * 
	 * @param transId -
	 * @param records Discovered service records
	 */
	@Override
	public void servicesDiscovered( int transId, ServiceRecord[] records )
	{
		if( this.activeSearch == transId )
		{
			if( records.length > 0 )
				this.service = new BluetoothService( this, records[0] );//, this.bt );// TODO:
																					// Will
																					// there
																					// ever
																					// be
																					// multiple
																					// services???
		}
	}

	@Override
	public void deviceDiscovered( RemoteDevice arg0, DeviceClass arg1 )
	{
	}

	@Override
	public void inquiryCompleted( int arg0 )
	{
	}

	/**
	 * Log internal stuff
	 * 
	 * @param msg The message to log (debug level)
	 */
	private void log( String msg )
	{
		Logging.debug( msg );
	}

	private void error( String msg )
	{
		Logging.error( msg );
	}
}
