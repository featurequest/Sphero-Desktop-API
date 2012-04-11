package se.nicklasgavelin.sphero.example;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JFrame;
import se.nicklasgavelin.bluetooth.Bluetooth;
import se.nicklasgavelin.bluetooth.Bluetooth.EVENT;
import se.nicklasgavelin.bluetooth.BluetoothDevice;
import se.nicklasgavelin.bluetooth.BluetoothDiscoveryListener;
import se.nicklasgavelin.sphero.Robot;
import se.nicklasgavelin.sphero.RobotListener;
import se.nicklasgavelin.sphero.command.CommandMessage;
import se.nicklasgavelin.sphero.command.FrontLEDCommand;
import se.nicklasgavelin.sphero.exception.InvalidRobotAddressException;
import se.nicklasgavelin.sphero.exception.RobotBluetoothException;
import se.nicklasgavelin.sphero.response.ResponseMessage;
import se.nicklasgavelin.sphero.response.InformationResponseMessage;

/**
 * Simple test class to test the Sphero API
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class Example_Site_API extends JFrame
{
	private static final long serialVersionUID = 6998786554264771793L;
	
	// Internal storage
	private int responses = 0;
	private ConnectThread ct;
	private JButton connectButton, disconnectButton;

	/**
	 * Main method
	 * 
	 * @param args Will be ignored
	 */
	@SuppressWarnings( "unused" )
	public static void main( String[] args )
	{
		Example_Site_API example_Site_API = new Example_Site_API();
		// new Thread( new Example_Site_API() ).start();
	}

	/**
	 * Our example application
	 */
	public Example_Site_API()
	{
		super( "Example API usage" );
		this.setLayout( new GridLayout( 2, 1 ) );

		// Connect button
		connectButton = new JButton( "Connect to available devices" );
		disconnectButton = new JButton( "Disconnect from all devices" );

		// Bind action to our connect button
		connectButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e )
			{
				// Check if we have something previous to stop
				if( ct != null )
					ct.stopThread();

				// Create a new thread
				ct = new ConnectThread();
				ct.start();

				// Toggle our button
				connectButton.setEnabled( false );
				disconnectButton.setEnabled( true );
			}
		} );

		// Bind action to the disconnect button
		disconnectButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed( ActionEvent e )
			{
				// Check if we have something to stop
				if( ct != null )
					ct.stopThread();

				// Toggle our buttons
				connectButton.setEnabled( true );
				disconnectButton.setEnabled( false );
			}
		} );

		// Add buttons to our GUI
		this.add( connectButton );
		this.add( disconnectButton );

		// Set some default stuff
		this.pack();
		this.setVisible( true );
		this.setDefaultCloseOperation( EXIT_ON_CLOSE );
	}

	/**
	 * Set connect button state (also affects the disconnect button)
	 * 
	 * @param enabled True to enable, false otherwise
	 */
	private void setConnectEnabled( boolean enabled )
	{
		this.connectButton.setEnabled( enabled );
		this.disconnectButton.setEnabled( !enabled );
	}

	/**
	 * Handles the detection of new devices and listens on our robots for
	 * responses and events
	 */
	private class ConnectThread extends Thread implements BluetoothDiscoveryListener, Runnable, RobotListener
	{
		// Internal storage
		private Bluetooth bt;
		private boolean stop = false;
		private Collection<Robot> robots;

		/**
		 * Create a connect thread
		 */
		public ConnectThread()
		{
			this.robots = new ArrayList<Robot>();
		}

		/**
		 * Stop everything regarding the connection and robots
		 */
		private void stopThread()
		{
			if( bt != null )
				bt.cancelDiscovery();
			this.stop = true;

			// Disconnect from all robots and clear the connected list
			for( Robot r : robots )
				r.disconnect();
			robots.clear();
		}

		@Override
		public void run()
		{
			try
			{
				// Will perform a bluetooth discovery before connecting to
				// any devices
				bt = new Bluetooth( this, Bluetooth.SERIAL_COM );
				bt.discover(); // # COMMENT THIS IF UNCOMMENTING THE BELOW AREA #

				// Uncomment the code below and comment out the bt.discover() line above
				// to
				// connect directly to a given Sphero

				// // ## START UNCOMMENT ##
				// final String bluetoothAddress = "0006664438B8";
				// BluetoothDevice btd = new BluetoothDevice( bt, "btspp://" +
				// bluetoothAddress + ":1;authenticate=true;encrypt=false;master=false" );
				//
				// // Create the robot from the bluetooth device
				// Robot r = new Robot( btd );
				//
				// // Try to connect to the robot
				// if ( r.connect() )
				// {
				// // Add ourselves as listeners
				// r.addListener( this );
				//
				// // Send a rgb transition command macro
				// r.rgbTransition( 255, 0, 0, 0, 255, 255, 50 );
				//
				// // Send a direct command
				// r.sendCommand( new FrontLEDCommand( 1F ) );
				// }
				// // ## END UNCOMMENT ##

				// Run forever, euheuheuh!
				while( !stop )
				{
					try
					{
						Thread.sleep( 5000 );
					}
					catch( InterruptedException e )
					{
					}
				}
			}
			catch( Exception e )
			{
				// Failure in searching for devices for some reason.
				e.printStackTrace();
			}
		}

		/*
		 * *************************************
		 * BLUETOOTH DISCOVERY STUFF
		 */

		/**
		 * Called when the device search is completed with detected devices
		 * 
		 * @param devices The devices detected
		 */
		@Override
		public void deviceSearchCompleted( Collection<BluetoothDevice> devices )
		{
			// Device search is completed
			System.out.println( "Completed device discovery" );

			// Try and see if we can find any Spheros in the found devices
			for( BluetoothDevice d : devices )
			{
				// Check if the Bluetooth device is a Sphero device or not
				if( Robot.isValidDevice( d ) )
				{
					System.out.println( "Found robot " + d.getAddress() );

					// We got a valid device (Sphero device), connect to it and
					// have some fun with colors.
					try
					{
						// Create our robot from the Bluetooth device that we got
						Robot r = new Robot( d );

						// Add ourselves as listeners for the responses
						r.addListener( this );

						// Check if we can connect
						if( r.connect() )
						{
							// Add robots to our connected robots list
							robots.add( r );

							System.out.println( "Connected to " + d.getName() + " : " + d.getAddress() );
							r.rgbTransition( 255, 0, 0, 0, 255, 255, 50 );

							// Send direct command
							r.sendCommand( new FrontLEDCommand( 1 ) );
						}
						else
							System.err.println( "Failed to connect to robot" );
					}
					catch( InvalidRobotAddressException ex )
					{
						ex.printStackTrace();
					}
					catch( RobotBluetoothException ex )
					{
						ex.printStackTrace();
					}
				}
			}

			// Disable the thread and set connected button state
			if( robots.isEmpty() )
			{
				this.stopThread();
				setConnectEnabled( true );
			}
		}

		/**
		 * Called when the search is started
		 */
		@Override
		public void deviceSearchStarted()
		{
			System.out.println( "Started device search" );
		}

		/**
		 * Called if something went wrong with the device search
		 * 
		 * @param error The error that occurred
		 */
		@Override
		public void deviceSearchFailed( EVENT error )
		{
			System.err.println( "Failed with device search: " + error );
		}

		/**
		 * Called when a Bluetooth device is discovered
		 * 
		 * @param device The device discovered
		 */
		@Override
		public void deviceDiscovered( BluetoothDevice device )
		{
			System.out.println( "Discovered device " + device.getName() + " : " + device.getAddress() );
		}

		/*
		 * ********************************************
		 * ROBOT STUFF
		 */

		/**
		 * Called when a response is received from a robot
		 * 
		 * @param r The robot the event concerns
		 * @param response The response received
		 * @param dc The command the response is concerning
		 */
		@Override
		public void responseReceived( Robot r, ResponseMessage response, CommandMessage dc )
		{
			System.out.println( "(" + ( ++responses ) + ") Received response: " + response.getResponseCode() + " to message " + dc.getCommand() );
		}

		/**
		 * Event that may occur for a robot
		 * 
		 * @param r The robot the event concerns
		 * @param code The event code for the event
		 */
		@Override
		public void event( Robot r, EVENT_CODE code )
		{
			System.out.println( "Received event: " + code );
		}

		@Override
		public void informationResponseReceived( Robot r, InformationResponseMessage response )
		{
			// Information response (Ex. Sensor data)
		}
	}
}
