package se.nicklasgavelin.bluetooth;

import java.io.IOException;
import java.util.Observable;
import javax.bluetooth.DataElement;
import javax.bluetooth.ServiceRecord;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

/**
 * This originated from the Mobile Processing project -
 * http://mobile.processing.org
 * 
 * Ported to Processing by, http://www.extrapixel.ch/bluetooth/
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 * 
 * @author Francis Li
 * @author extrapixel
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 *         (Modifier)
 */
public class BluetoothService extends Observable implements Runnable
{
	/**
	 * Name that is set if the Bluetooth device couldn't return the name
	 */
	public static final String UNKNOWN = "(Unknown)";

	public static final int ATTR_SERVICENAME = 0x0100;
	public static final int ATTR_SERVICEDESC = 0x0101;
	public static final int ATTR_PROVIDERNAME = 0x0102;

	private BluetoothDevice device;
	private ServiceRecord record;
	// private final Bluetooth bt;
	private String name;
	private String description;
	private String provider;
	private String serviceConnectionURL = null;

	/**
	 * Create a Bluetooth service from an available service record, device and
	 * Bluetooth class instance.
	 * 
	 * @param device The Bluetooth device for this service
	 * @param record The service record
	 */
	protected BluetoothService( BluetoothDevice device, ServiceRecord record )// ,
																				// Bluetooth
																				// bt )
	{
		// Store objects
		this.device = device;
		this.record = record;
		// this.bt = bt;

		DataElement element;

		// Fetch record information
		element = record.getAttributeValue( ATTR_SERVICENAME );
		if( element != null )
			name = (String) element.getValue();
		else
			name = UNKNOWN;

		element = record.getAttributeValue( ATTR_SERVICEDESC );
		if( element != null )
			description = (String) element.getValue();
		else
			description = UNKNOWN;

		element = record.getAttributeValue( ATTR_PROVIDERNAME );
		if( element != null )
			provider = (String) element.getValue();
		else
			provider = UNKNOWN;
	}

	/**
	 * Create a Bluetooth service from a direct connection URL,
	 * WARNING: Using this method will result in methods that calls
	 * the device or record to return erroneous data or case exception.
	 * 
	 * @param serviceConnectionURL The Bluetooth connection URL
	 */
	protected BluetoothService( String serviceConnectionURL )
	{
		// this.bt = null;
		this.serviceConnectionURL = serviceConnectionURL;
		this.name = UNKNOWN;
		this.description = UNKNOWN;
		this.provider = UNKNOWN;
	}

	/**
	 * Returns the name of the service
	 * 
	 * @return The service name
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Returns service description
	 * 
	 * @return Service description
	 */
	public String getDescription()
	{
		return this.description;
	}

	/**
	 * Returns provider name
	 * 
	 * @return Provider name
	 */
	public String getProvider()
	{
		return this.provider;
	}

	/**
	 * Returns the Bluetooth connection URL
	 * 
	 * @return The Bluetooth connection URL or null if no address could be
	 *         fetched
	 */
	public String getConnectionURL()
	{
		// Check if we can return something
		if( this.record != null )
			return this.record.getConnectionURL( ServiceRecord.AUTHENTICATE_NOENCRYPT, false );
		else if( this.serviceConnectionURL != null )
			return this.serviceConnectionURL;

		return null;
	}

	/**
	 * Connect to the Bluetooth service,
	 * will throw an IOExceptio if the connection fails
	 * 
	 * @return The Bluetooth connection
	 * 
	 * @throws IOException If the service is unable to connect
	 */
	public BluetoothConnection connect() throws IOException
	{
		// Create our stream connection object
		StreamConnection con;

		// Check the method of connection
		if( this.serviceConnectionURL != null )
			con = (StreamConnection) Connector.open( this.serviceConnectionURL );
		else
			con = (StreamConnection) Connector.open( record.getConnectionURL( ServiceRecord.AUTHENTICATE_NOENCRYPT, false ) );

		// Create our bluetooth connection and return it
		BluetoothConnection c = new BluetoothConnection( con );

		c.device = device;
		c.open();

		return c;
	}

	/**
	 * This run() method is used to run the server thread, which accepts
	 * client connections and dispatches them to the sketch. The setup
	 * occurs in Bluetooth.start().
	 */
	@Override
	public void run()
	{
		// TODO: Implement
		/*
		 * System.out.println("Ran service thread");
		 * while (true)
		 * {//bt.serverThread == Thread.currentThread()) {
		 * try
		 * {
		 * StreamConnection con = bt.server.acceptAndOpen();
		 * BluetoothClient c = new BluetoothClient( con );
		 * c.device = new BluetoothDevice( bt, RemoteDevice.getRemoteDevice( con
		 * ));
		 * 
		 * try {
		 * c.device.setName( c.device.getRemoteDevice().getFriendlyName(false)
		 * );
		 * } catch (Exception e) {
		 * //c.device.name = null;
		 * }
		 * //if (c.device.name == null) {
		 * // c.device.name = Device.UNKNOWN;
		 * //}
		 * 
		 * c.open();
		 * bt.clientConnectEvent(c);
		 * }
		 * catch (IOException ioe)
		 * {
		 * throw new RuntimeException(ioe.getMessage());
		 * }
		 * }
		 */
		/*
		 * try {
		 * bt.server.close();
		 * } catch (IOException ioe) {
		 * }
		 */
	}
}
