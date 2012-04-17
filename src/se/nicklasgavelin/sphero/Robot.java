package se.nicklasgavelin.sphero;

import java.awt.Color;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.nicklasgavelin.bluetooth.BluetoothConnection;
import se.nicklasgavelin.bluetooth.BluetoothDevice;
import se.nicklasgavelin.configuration.ProjectProperties;
import se.nicklasgavelin.log.Logging;
import se.nicklasgavelin.sphero.RobotListener.EVENT_CODE;
import se.nicklasgavelin.sphero.command.RawMotorCommand.MOTOR_MODE;
import se.nicklasgavelin.sphero.command.*;
import se.nicklasgavelin.sphero.exception.InvalidRobotAddressException;
import se.nicklasgavelin.sphero.exception.RobotBluetoothException;
import se.nicklasgavelin.sphero.exception.RobotInitializeConnectionFailed;
import se.nicklasgavelin.sphero.macro.MacroCommand;
import se.nicklasgavelin.sphero.macro.MacroObject;
import se.nicklasgavelin.sphero.macro.command.Delay;
import se.nicklasgavelin.sphero.macro.command.Emit;
import se.nicklasgavelin.sphero.macro.command.RGB;
import se.nicklasgavelin.sphero.response.InformationResponseMessage;
import se.nicklasgavelin.sphero.response.ResponseMessage;
import se.nicklasgavelin.sphero.response.regular.GetBluetoothInfoResponse;
import se.nicklasgavelin.util.ByteArrayBuffer;
import se.nicklasgavelin.util.Pair;
import se.nicklasgavelin.util.Value;

/**
 * Robot class. Mirrors the direct connection between the application
 * and the Sphero robot. Contains some macro commands to perform simple
 * direct commands.
 * 
 * It's also possible to create commands and send them directly using
 * the Robot.sendCommand method.
 * 
 * Commands may be sent with time delays or periodicity.
 * 
 * Example usage:
 * Robot r = new Robot( <BluetoothDevice> );
 * r.connect();
 * r.sendCommand( new FrontLEDCommand( 0.5F ) );
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 * @version 1.2
 * 
 *          TODO: Set temporary internal values on sending commands so that we don't
 *          update them too late if we send multiple commands
 */
public class Robot
{
	private RobotSetting rs;

	// Bluetooth
	private final BluetoothDevice bt;
	private BluetoothConnection btc;
	private boolean connected = false;
	// Listener/writer
	private Robot.RobotStreamListener listeningThread;
	private Robot.RobotSendingQueue sendingTimer;
	private List<RobotListener> listeners;
	// Other
	private String name = null;
	// Robot macro
	private Robot.MACRO_SETTINGS macroSettings;
	// Robot position and led color
	private Robot.RobotMovement movement;
	private Robot.RobotRawMovement rawMovement;
	private Robot.RobotLED led;
	// Pinger
	private float PING_INTERVAL; // Time in milliseconds
	// Address
	/**
	 * The start of the Bluetooth address that is describing if the address
	 * belongs to a Sphero device.
	 */
	public static final String ROBOT_ADDRESS_PREFIX = "00066";
	// Robot controller
	// private RobotController controller;
	private static int error_num = 0;
	private static final String[] invalidAddressResponses = new String[] { "The bluetooth address is invalid, the Sphero device bluetooth address must start with " + ROBOT_ADDRESS_PREFIX, "The address is still invalid, the Sphero bluetooth address must start with " + ROBOT_ADDRESS_PREFIX, "Check your frigging bluetooth address, it still need to start with " + ROBOT_ADDRESS_PREFIX, "I give up... You are not taking me seriously. Why would I give a hoot about bluetooth addresses anyway (Still need to start with " + ROBOT_ADDRESS_PREFIX + ")" };

	/**
	 * Create a robot from a Bluetooth device. You need to call Robot.connect
	 * after creating a robot to connect to it via the Bluetooth connection
	 * given.
	 * 
	 * @param bt The Bluetooth device that represents the robot
	 * 
	 * @throws InvalidRobotAddressException
	 *             throws
	 *             RobotBluetoothException
	 */
	public Robot( BluetoothDevice bt ) throws InvalidRobotAddressException, RobotBluetoothException
	{
		this( bt, null );
	}

	/**
	 * Create a robot from a Bluetooth device. You need to call Robot.connect
	 * after creating a robot to connect to it via the Bluetooth connection
	 * given.
	 * 
	 * @param bt The Bluetooth device that represents the robot
	 * 
	 * @throws InvalidRobotAddressException
	 *             throws
	 *             RobotBluetoothException
	 */
	public Robot( BluetoothDevice bt, RobotSetting rs ) throws InvalidRobotAddressException, RobotBluetoothException
	{
		this.bt = bt;

		// Create a unique logger for this class instance
		// this.logger = Logging.createLogger( Robot.class, Robot.logLevel,
		// this.bt.getAddress() );

		// Control that we got a valid robot
		if( !this.bt.getAddress().startsWith( ROBOT_ADDRESS_PREFIX ) )
		{
			String msg = invalidAddressResponses[Value.clamp( error_num++, 0, invalidAddressResponses.length - 1 )];
			Logging.error( msg );
			throw new InvalidRobotAddressException( msg );
		}

		if( rs == null )
			this.rs = ProjectProperties.getInstance().getRobotSetting();
		else
			this.rs = rs;

		// Set ping interval
		PING_INTERVAL = this.rs.socketPingInterval;

		// Initialize the position and LEDs
		this.movement = new Robot.RobotMovement();
		this.rawMovement = new Robot.RobotRawMovement();
		this.led = new Robot.RobotLED();
		this.macroSettings = new Robot.MACRO_SETTINGS();

		// Discover the connection services that we can use
		bt.discover();

		// Create an empty array of listeners
		this.listeners = new ArrayList<RobotListener>();

		Logging.debug( "Robot created successfully" );

		// Add system hook
		Runtime.getRuntime().addShutdownHook( new Thread( new Runnable() {
			@Override
			public void run()
			{
				// Force disconnect asap!

				try
				{
					// Wait for disconnect event
					Robot.this.disconnect( false );
					
					if( Robot.this.sendingTimer != null && Robot.this.sendingTimer.w != null )
						Robot.this.sendingTimer.w.join();
				}
				catch( InterruptedException ex )
				{
					Logger.getLogger( Robot.class.getName() ).log( Level.SEVERE, null, ex );
				}
			}
		} ) );
	}

	/*
	 * *****************************************************
	 * LISTENERS
	 * ****************************************************
	 */

	/**
	 * Add a robot listener to the current class instance
	 * 
	 * @param l The listener to add
	 */
	public void addListener( RobotListener l )
	{
		Logging.debug( "Adding listener of type " + l.getClass().getCanonicalName() );

		synchronized( this.listeners )
		{
			if( !this.listeners.contains( l ) )
				this.listeners.add( l );
		}
	}

	/**
	 * Remove a listener that is listening from the current class
	 * instance.
	 * 
	 * @param l The listener to remove
	 */
	public void removeListener( RobotListener l )
	{
		synchronized( this.listeners )
		{
			// Check so that we can remove it
			if( this.listeners.contains( l ) )
				this.listeners.remove( l );
		}
	}

	/**
	 * Notify all listeners about a device response
	 * 
	 * @param dr The device response that was received
	 * @param dc The device command belonging to the device response
	 */
	private void notifyListenersDeviceResponse( ResponseMessage dr, CommandMessage dc )
	{
		Logging.debug( "Notifying listeners about device respose " + dr + " for device command " + dc );

		synchronized( this.listeners )
		{
			// Go through all listeners and notify them
			for( RobotListener r : this.listeners )
				r.responseReceived( this, dr, dc );
		}
	}

	private void notifyListenersInformationResponse( InformationResponseMessage dir )
	{
		Logging.debug( "Nofifying listeners about information response " + dir );
		
		synchronized( this.listeners )
		{
			for( RobotListener r : this.listeners )
				r.informationResponseReceived( this, dir );
		}
	}

	/**
	 * Notify listeners of occurring events
	 * 
	 * @param event The event to notify about
	 */
	private void notifyListenerEvent( EVENT_CODE event )
	{
		Logging.debug( "Notifying listeners about event " + event );

		// Notify all listeners
		synchronized( this.listeners )
		{
			for( RobotListener r : this.listeners )
				r.event( this, event );
		}
	}

	/**
	 * Called to close down the complete connection and notify listeners
	 * about an unexpected closing.
	 */
	private void connectionClosedUnexpected()
	{
		// Cancel the sending of new messages
		this.sendingTimer.cancel();

		// Cancel the listening of incomming messages
		this.listeningThread.stopThread();

		// Close the bluetooth connection
		this.btc.stop();

		// Notify about disconnect
		if( this.connected )
		{
			this.connected = false;
			Logging.error( "Connection closed unexpectedly for some reason, all threads have been closed down for the robot" );
			this.notifyListenerEvent( EVENT_CODE.CONNECTION_CLOSED_UNEXPECTED );
		}
	}

	/*
	 * *****************************************************
	 * CONNECTION MANAGEMENT
	 * ****************************************************
	 */

	/**
	 * Connect to the robot via the Bluetooth connection given in the
	 * constructor.
	 * Will NOT throw any exceptions if connection fails.
	 * 
	 * @return True if connection succeeded, false otherwise
	 */
	public boolean connect()
	{
		return this.connect( false );
	}

	/**
	 * Connect with a possible chance of getting an exception thrown if the
	 * connection
	 * fails.
	 * 
	 * @param throwException True to throw exception, false otherwise
	 * 
	 * @throws RobotInitializeConnectionFailed Thrown if throwException is set
	 *             to true and initialization failed
	 * @return True if connected, will throw exception if not connected
	 */
	public boolean connect( boolean throwException )
	{
		try
		{
			return this.internalConnect();
		}
		catch( RobotBluetoothException e )
		{
			if( throwException )
				throw new RobotInitializeConnectionFailed( e.getMessage() );
		}
		catch( RobotInitializeConnectionFailed e )
		{
			if( throwException )
				throw e;
		}

		return false;
	}

	/**
	 * Connects to the robot via Bluetooth. Will return true if the connection
	 * was successful, throws and exception otherwise.
	 * 
	 * @throws RobotInitializeConnectionFailed If connection failed
	 * @return True if connection succeeded
	 */
	private boolean internalConnect() throws RobotInitializeConnectionFailed, RobotBluetoothException
	{
		Logging.debug( "Trying to connect to " + this.getName() + ":" + this.getAddress() );
		this.btc = bt.connect();

		// Check if we could connect to the bluetooth device
		if( this.btc == null )
		{
			Logging.error( "Failed to connect to the robot bluetooth connection" );
			throw new RobotInitializeConnectionFailed( "Failed to connect due to bluetooth error" );
		}

		// We are now connected, continue with
		// the initialization of everything else regarding the connection
		this.connected = true;

		// Create a listening thread and close any old ones down
		if( this.listeningThread != null )
			this.listeningThread.stopThread();
		this.listeningThread = new Robot.RobotStreamListener( btc );
		this.listeningThread.start();

		// Create our sending timer
		if( this.sendingTimer != null )
			this.sendingTimer.cancel();
		this.sendingTimer = new Robot.RobotSendingQueue( btc );

		// Reset the robot
		this.sendSystemCommand( new AbortMacroCommand() );
		this.sendSystemCommand( new RollCommand( this.movement.getHeading(), this.movement.getVelocity(), this.movement.getStop() ) );
		this.sendSystemCommand( new CalibrateCommand( this.movement.getHeading() ) );
		this.sendSystemCommand( new FrontLEDCommand( this.led.getFrontLEDBrightness() ) );
		this.sendSystemCommand( new RGBLEDCommand( this.getLed().getRGBColor() ) );

		// Create our pinger
		this.sendSystemCommand( new PingCommand( this ), PING_INTERVAL, PING_INTERVAL );

		// Notify listeners
		this.notifyListenerEvent( ( this.connected ? EVENT_CODE.CONNECTION_ESTABLISHED : EVENT_CODE.CONNECTION_FAILED ) );

		// Return connection status
		return this.connected;
	}

	/**
	 * Disconnect from the robot (closes all streams and Bluetooth connections,
	 * also closes down all internal threads).
	 */
	public void disconnect()
	{
		this.disconnect( true );
	}

	private boolean disconnecting = false;

	/**
	 * Method to notify listeners about a disconnect and set the connect flag
	 * 
	 * @param notifyListeners True to notify listeners, false otherwise
	 */
	private void disconnect( boolean notifyListeners )
	{
		Logging.debug( "Disconnecting from the current robot" );

		if( this.connected )
		{
			// Set notify status, a bit ugly but hey.. Quick hack!
			notifyListenersDisconnect = notifyListeners;

			// Close all connection
			this.closeConnections();
		}
		else
		{
			// Check if we want to notify listeners that there exists no active connection
			if( notifyListeners )
				this.notifyListenerEvent( EVENT_CODE.NO_CONNECTION_EXISTS );
		}

		// Set our connection flag to false
		// this.connected = false;
	}

	/**
	 * Closes down all listening and sending sockets
	 */
	private void closeConnections()
	{
		// Check if we have something to disconnect from
		if( this.connected )
		{
			this.connected = false;
			this.disconnecting = true;

			// Stop our transmission of commands
			this.sendingTimer.cancel();

			// Send a direct command to motorStop any movement (eludes the .cancel
			// command)
			this.sendingTimer.forceCommand( new AbortMacroCommand() );
			this.sendingTimer.forceCommand( new RollCommand( 0, 0, true ) );
			this.sendingTimer.forceCommand( new FrontLEDCommand( 0 ) );
			this.sendingTimer.forceCommand( new RGBLEDCommand( Color.BLACK ) );
		}
	}

	/*
	 * *****************************************************
	 * COMMANDS
	 * ****************************************************
	 */

	/**
	 * Send a command to the active robot
	 * 
	 * @param command The command to send
	 */
	public void sendCommand( CommandMessage command )
	{
		this.sendingTimer.enqueue( command, false );
	}

	/**
	 * Enqueue a command to be sent after a macro has finished execution
	 * 
	 * @param command The command to run after macro command execution
	 */
	public void sendCommandAfterMacro( CommandMessage command )
	{
		this.macroSettings.sendCommandAfterMacro( command );
	}

	/**
	 * Stops the commands entered to be sent after the macro is finished
	 * running.
	 * To send new commands they have to be re-entered into the
	 * sendCommandAfterMacro method.
	 */
	public void cancelSendCommandAfterMacro()
	{
		this.macroSettings.clearSendingQueue();
	}

	/**
	 * Send a command with a given delay
	 * 
	 * @param command The command to send
	 * @param delay The delay before the command is sent
	 */
	public void sendCommand( CommandMessage command, float delay )
	{
		this.sendingTimer.enqueue( command, delay );
	}

	/**
	 * Send a command infinite times with a certain initial delay and a certain
	 * given period length between next-coming messages.
	 * 
	 * @param command The command to send
	 * @param initialDelay The initial delay before the first message is sent
	 *            (in milliseconds)
	 * @param periodLength The length between the transmissions
	 */
	public void sendPeriodicCommand( CommandMessage command, float initialDelay, float periodLength )
	{
		this.sendingTimer.enqueue( command, false, initialDelay, periodLength );
	}

	/**
	 * Send a system command
	 * 
	 * @param command The command to send
	 */
	private void sendSystemCommand( CommandMessage command )
	{
		this.sendingTimer.enqueue( command, true );
	}

	/**
	 * Send a system command after a given delay
	 * 
	 * @param command The command to send
	 * @param delay The delay before sending the message
	 */
	private void sendSystemCommand( CommandMessage command, float delay )
	{
		this.sendingTimer.enqueue( command, delay, true );
	}

	/**
	 * Send a system command infinitely with a certain initial delay before the
	 * first message and a period length between nextcomming messages.
	 * 
	 * @param command The command to send
	 * @param initialDelay The initial delay before the first message is sent
	 *            (in milliseconds)
	 * @param periodLength The length between the transmissions
	 */
	private void sendSystemCommand( CommandMessage command, float initialDelay, float periodLength )
	{
		this.sendingTimer.enqueue( command, true, initialDelay, periodLength );
	}

	private boolean receivedFirstDisconnect = false,
			notifyListenersDisconnect = true;

	/**
	 * Updates position, led colors/brightness etc depending on the command that
	 * is sent.
	 * 
	 * @param command The command that is suppose to be sent
	 */
	private void updateInternalValues( CommandMessage command )
	{
		// Disconnect event, we will disconnect if we are not connected and
		// we have sent both a roll motorStop command and a front led turn off command
		if( !this.connected && ( command instanceof FrontLEDCommand || command instanceof RollCommand ) )
		{
			if( receivedFirstDisconnect )
			{
				// Stop any active listening thread
				if( this.listeningThread != null )
					this.listeningThread.stopThread();

				// Stop any active sending timer thread
				if( this.sendingTimer != null )
					this.sendingTimer.stopAll();

				// Stop the bluetooth connection
				if( this.btc != null )
					this.btc.stop();

				// Check if we want to notify anyone
				if( notifyListenersDisconnect )
				{
					// Send disconnect event
					this.notifyListenerEvent( EVENT_CODE.DISCONNECTED );
				}
			}
			else
				receivedFirstDisconnect = true;
		}

		// Logging.debug( "Updating internal values for " + command );

		// Update stuff event
		switch ( command.getCommand() )
		{
		/*
		 * Roll command, update internal values
		 */
			case ROLL:
				RollCommand rc = (RollCommand) command;

				// Set new values
				this.movement.heading = rc.getHeading();
				this.movement.velocity = rc.getVelocity();
				this.movement.stop = rc.getStopped();

				break;

			case SPIN_LEFT:
				// TODO: Movements are stopped other than for some special commands
				break;

			case SPIN_RIGHT:
				// TODO: Movements are stopped other than for some special commands
				break;

			case RAW_MOTOR:
				RawMotorCommand raw = (RawMotorCommand) command;
				this.rawMovement.leftMotorMode = raw.getLeftMode();
				this.rawMovement.rightMotorMode = raw.getRightMode();
				this.rawMovement.leftMotorSpeed = raw.getLeftSpeed();
				this.rawMovement.rightMotorSpeed = raw.getRightSpeed();

				break;

			/*
			 * Rotation rate.
			 * TODO: Does it have some effect on the robot? Havn't seen any
			 * definite
			 * effects when performed
			 */
			case ROTATION_RATE:
				RotationRateCommand rrc = (RotationRateCommand) command;
				this.movement.rotationRate = rrc.getRate();

				break;

			case JUMP_TO_BOOTLOADER:
			case GO_TO_SLEEP:
				// Graceful disconnect as we will loose the connection when
				// jumping to the bootloader
				this.disconnect();

				break;

			case RGB_LED_OUTPUT:
				RGBLEDCommand rgb = (RGBLEDCommand) command;

				// Update led values
				this.led.red = rgb.getRed();
				this.led.green = rgb.getGreen();
				this.led.blue = rgb.getBlue();

				break;

			case FRONT_LED_OUTPUT:
				FrontLEDCommand flc = (FrontLEDCommand) command;
				this.led.brightness = flc.getBrightness();

				break;

			/*
			 * Havn't seen any effect of this command
			 */
			case SET_BLUETOOTH_NAME:
				// Update the name
				this.bt.updateName();

				break;
		}
	}

	/*
	 * *****************************************************
	 * MACRO COMMANDS
	 * ****************************************************
	 */

	/**
	 * Roll the robot with a given motorHeading and speed
	 * 
	 * @param heading The motorHeading (0-360)
	 * @param speed The speed (0-1)
	 */
	public void roll( float heading, float speed )
	{
		this.sendCommand( new RollCommand( heading, speed, false ) );
	}

	/**
	 * Calibrate the motorHeading to a specific motorHeading (Will move the
	 * robot to this
	 * motorHeading)
	 * 
	 * @param heading The motorHeading to calibrate to (0-359)
	 */
	public void calibrate( float heading )
	{
		this.sendCommand( new RollCommand( heading, 0F, false ) );
		this.sendCommand( new CalibrateCommand( heading ) );

		// Blink the main led a few times to show calibration
		this.sendSystemCommand( new FrontLEDCommand( this.getLed().getFrontLEDBrightness() ), 11000 );
		this.sendSystemCommand( new FrontLEDCommand( 0 ) );
		this.sendSystemCommand( new FrontLEDCommand( 0 ), 100, 10 );
		this.sendSystemCommand( new FrontLEDCommand( 1F ), 200, 10 );
	}

	/**
	 * Creates a transition between two different colors with a number of
	 * changes between the colors (the transition itself). The delay between
	 * each step is set to 25 ms.
	 * 
	 * @param from The color to go from
	 * @param to The color to end up with
	 * @param steps The number of steps to take between the change between the
	 *            two colors
	 */
	public void rgbTransition( Color from, Color to, int steps )
	{
		this.rgbTransition( from, to, steps, 25 );
	}
	
	/**
	 * Creates a transition between two different colors with a number of
	 * changes between the colors (the transition itself). The delay between
	 * each color shift is set to 25 ms.
	 * 
	 * @param fRed The initial red color value
	 * @param fGreen The initial green color value
	 * @param fBlue The initial blue color value
	 * @param tRed The final red color value
	 * @param tGreen The final green color value
	 * @param tBlue The final blue color value
	 * @param steps Number of steps to take (The number of times to change
	 *            color until reaching the final color)
	 */
	public void rgbTransition( int fRed, int fGreen, int fBlue, int tRed, int tGreen, int tBlue, int steps )
	{
		this.rgbTransition( fRed, fGreen, fBlue, tRed, tGreen, tBlue, steps, 25 );
	}

	/**
	 * Creates a transition between two different colors with a number of
	 * changes between the colors (the transition itself).
	 * 
	 * @param from The color to go from
	 * @param to The color to end up with
	 * @param steps The number of steps to take between the change between the
	 *            two colors
	 * @param dDelay Delay between the color shifts
	 */
	public void rgbTransition( Color from, Color to, int steps, int dDelay )
	{
		this.rgbTransition( from.getRed(), from.getGreen(), from.getBlue(), to.getRed(), to.getGreen(), to.getBlue(), steps, dDelay );
	}

	/**
	 * Creates a transition between two different colors with a number of
	 * changes between the colors (the transition itself).
	 * 
	 * @param fRed The initial red color value
	 * @param fGreen The initial green color value
	 * @param fBlue The initial blue color value
	 * @param tRed The final red color value
	 * @param tGreen The final green color value
	 * @param tBlue The final blue color value
	 * @param steps Number of steps to take (The number of times to change
	 *            color until reaching the final color)
	 * @param dDelay Delay between the color shifts
	 */
	public void rgbTransition( int fRed, int fGreen, int fBlue, int tRed, int tGreen, int tBlue, int steps, int dDelay )
	{
		int tdelay = dDelay;

		// Hue, saturation, intensity
		final float[] fHSB = Color.RGBtoHSB( fRed, fGreen, fBlue, null );
		final float[] tHSB = Color.RGBtoHSB( tRed, tGreen, tBlue, null );

		float hDif = Math.abs( fHSB[0] - tHSB[0] );
		float sDif = Math.abs( fHSB[1] - tHSB[1] );
		float iDif = Math.abs( fHSB[2] - tHSB[2] );

		boolean iHue = ( fHSB[0] < tHSB[0] );
		boolean iSat = ( fHSB[1] < tHSB[1] );
		boolean iInt = ( fHSB[2] < tHSB[2] );

		float incHue = ( hDif / steps );
		float incSat = ( sDif / steps );
		float incInt = ( iDif / steps );

		Color c;
		float[] n = new float[ 3 ];

		// Create macro
		MacroObject mo = new MacroObject();

		// Go through all steps
		for( int i = 0; i < steps; i++ )
		{
			// Calculate hue saturation and intensity
			n[0] = ( iHue ? fHSB[0] + ( i * incHue ) : fHSB[0] - ( i * incHue ) );
			n[1] = ( iSat ? fHSB[1] + ( i * incSat ) : fHSB[1] - ( i * incSat ) );
			n[2] = ( iInt ? fHSB[2] + ( i * incInt ) : fHSB[2] - ( i * incInt ) );

			// Get new color
			int ik = Color.HSBtoRGB( Value.clamp( n[0], 0, 1 ), Value.clamp( n[1], 0, 1 ), Value.clamp( n[2], 0, 1 ) );
			c = new Color( ik );

			// Add new RGB commands
			mo.addCommand( new RGB( c, 0 ) );
			mo.addCommand( new Delay( tdelay ) );
		}

		// Set streaming as we don't know if we can fit all macro commands in one message
		mo.setMode( MacroObject.MacroObjectMode.CachedStreaming );

		// Send macro to the Sphero device
		this.sendCommand( mo );
	}
	
	private void createFromToColorMacroObject( MacroObject mo, Color from, Color to, int steps, int dDelay )
	{
		int tdelay = dDelay;

		// Hue, saturation, intensity
		final float[] fHSB = Color.RGBtoHSB( from.getRed(), from.getGreen(), from.getBlue(), null );
		final float[] tHSB = Color.RGBtoHSB( to.getRed(), to.getGreen(), to.getBlue(), null );

		float hDif = Math.abs( fHSB[0] - tHSB[0] );
		float sDif = Math.abs( fHSB[1] - tHSB[1] );
		float iDif = Math.abs( fHSB[2] - tHSB[2] );

		boolean iHue = ( fHSB[0] < tHSB[0] );
		boolean iSat = ( fHSB[1] < tHSB[1] );
		boolean iInt = ( fHSB[2] < tHSB[2] );

		float incHue = ( hDif / steps );
		float incSat = ( sDif / steps );
		float incInt = ( iDif / steps );

		Color c;
		float[] n = new float[ 3 ];

		// Go through all steps
		for( int i = 0; i < steps; i++ )
		{
			// Calculate hue saturation and intensity
			n[0] = ( iHue ? fHSB[0] + ( i * incHue ) : fHSB[0] - ( i * incHue ) );
			n[1] = ( iSat ? fHSB[1] + ( i * incSat ) : fHSB[1] - ( i * incSat ) );
			n[2] = ( iInt ? fHSB[2] + ( i * incInt ) : fHSB[2] - ( i * incInt ) );

			// Get new color
			int ik = Color.HSBtoRGB( Value.clamp( n[0], 0, 1 ), Value.clamp( n[1], 0, 1 ), Value.clamp( n[2], 0, 1 ) );
			c = new Color( ik );

			// Add new RGB commands
			mo.addCommand( new RGB( c, 0 ) );
			mo.addCommand( new Delay( tdelay ) );
		}
	}

	public void rgbBreath( Color from, Color to, int steps, int dDelay )
	{
		MacroObject mo = new MacroObject();
		this.createFromToColorMacroObject( mo, from, to, steps/2, dDelay/2 );
		this.createFromToColorMacroObject( mo, to, from, steps/2, dDelay/2 );

		// Set streaming as we don't know if we can fit all macro commands in one message
		mo.setMode( MacroObject.MacroObjectMode.CachedStreaming );

		// Send macro to the Sphero device
		this.sendCommand( mo );
	}
	
	/**
	 * Rotate the robot
	 * 
	 * @param heading The new motorHeading, 0-360
	 */
	public void rotate( float heading )
	{
		this.roll( heading, 0.0F );
	}

	/**
	 * Jump the robot to the bootloader part.
	 * 
	 * NOTICE: THE DEVICE CONNETION WILL DISCONNECT WHEN THE ROBOT
	 * JUMPS TO THE BOOTLOADER!
	 */
	public void jumpToBootloader()
	{
		this.sendCommand( new JumpToBootloaderCommand() );
	}

	/**
	 * Send a sleep command to the robot.
	 * The sleep time is given in seconds.
	 * 
	 * @param time Number of seconds to sleep. The connection WILL be LOST to
	 *            the robot and have to be re-initialized.
	 */
	public void sleep( int time )
	{
		this.sendCommand( new SleepCommand( time ) );
	}

	/**
	 * Update the robot rotation rate
	 * 
	 * @param rotationRate The new rotation rate, value 0-1
	 */
	public void setRotationRate( float rotationRate )
	{
		this.sendCommand( new RotationRateCommand( rotationRate ) );
	}

	/**
	 * Set a new RGB color for the robot RGB LED
	 * 
	 * @param red The new red value
	 * @param green The new green value
	 * @param blue The new blue value
	 */
	public void setRGBLEDColor( int red, int green, int blue )
	{
		this.sendCommand( new RGBLEDCommand( red, green, blue ) );
	}

	/**
	 * Set a new color for the robot RGB LED
	 * 
	 * @param c The new color
	 */
	public void setRGBLedColor( Color c )
	{
		this.sendCommand( new RGBLEDCommand( c ) );
	}

	/**
	 * Resets the robots motorHeading.
	 * 
	 * Sends a roll command with current velocity and motorStop value and also a
	 * calibrate
	 * command to reset the motorHeading.
	 */
	public void resetHeading()
	{
		this.sendCommand( new RollCommand( 0.0F, this.movement.getVelocity(), this.movement.getStop() ) );
		this.sendCommand( new CalibrateCommand( 0.0F ) );
	}

	/**
	 * Update motorHeading offset
	 * 
	 * @param offset The motorHeading offset
	 */
	public void setHeadingOffset( double offset )
	{
		this.movement.algorithm.headingOffset = offset;
	}

	/**
	 * Set ledBrightness of the front led. 0-1
	 * 
	 * @param brightness The ledBrightness value, 0-1
	 */
	public void setFrontLEDBrightness( float brightness )
	{
		this.sendCommand( new FrontLEDCommand( brightness ) );
	}

	/**
	 * Set the name of the robot.
	 * 
	 * Note: Doesn't seem to update anything, maybe not implemented on the
	 * Sphero yet?
	 * 
	 * @param name The new name
	 */
	public void setRobotName( String name )
	{
		this.sendCommand( new SetRobotNameCommand( name ) );
	}

	/**
	 * Turn on/off stabilization
	 * 
	 * @param on True for on, false for off
	 */
	public void stabilization( boolean on )
	{
		this.sendCommand( new StabilizationCommand( on ) );
	}

	/**
	 * Drive in a direction
	 * 
	 * @param x X direction
	 * @param y Y direction
	 * @param z Z direction
	 */
	public void drive( double x, double y, double z )
	{
		// Convert the values to the correct ones depending on the given algorithm
		this.movement.algorithm.convert( x, y, z );
		this.movement.algorithm.adjustHeading();

		// Cap the value
		this.movement.algorithm.adjustedHeading = Value.clamp( this.movement.algorithm.adjustedHeading, 0.0D, 359.0D );

		// Send the command
		this.roll( (float) this.movement.algorithm.adjustedHeading, (float) this.movement.algorithm.speed );
	}

	/**
	 * Boost the robot (Speed increase to maximum)
	 * 
	 * @param timeInterval Time interval for the boost command (in ms)
	 */
	public void boost( float timeInterval )
	{
		// Create commands to send
		RollCommand boost = new RollCommand( this.movement.heading, 1F, false );
		RollCommand resetBoost = new RollCommand( this.movement.heading, this.movement.velocity, this.movement.stop );

		// Send commands
		this.sendSystemCommand( boost );
		this.sendSystemCommand( resetBoost, timeInterval );
	}

	/**
	 * Send a command to motorStop the robot motors
	 */
	public void stopMotors()
	{
		this.sendCommand( new RollCommand( this.movement.heading, 0.0F, true ) );
	}

	/**
	 * Returns true if motors are stopped. False otherwise. Will not return true
	 * if the speed is 0!
	 * 
	 * @return True if motors are stopped, false otherwise
	 */
	public boolean isStopped()
	{
		return !this.movement.stop;
	}

	/**
	 * Set the current drive algorithm. Only affects the Robot.drive method.
	 * 
	 * @param algorithm The new drive algorithm
	 */
	public void setDriveAlgorithm( DriveAlgorithm algorithm )
	{
		this.movement.algorithm = algorithm;
	}

	/**
	 * Returns the current drive algorithm that affects the Robot.drive
	 * method.
	 * 
	 * @return The current drive algorithm
	 */
	public DriveAlgorithm getDriveAlgorithm()
	{
		return this.movement.algorithm;
	}

	/*
	 * *****************************************************
	 * MACRO
	 * ****************************************************
	 */

	/**
	 * Stop any current macros from running
	 */
	public void stopMacro()
	{
		this.macroSettings.stopMacro();
	}

	/**
	 * Send a macro to the Sphero device. If the macro mode is set to Normal
	 * either
	 * a RunMacroCommand has to be sent or you have to run .playMacro on the
	 * Robot instance
	 * 
	 * @param macro The macro to send to the Sphero
	 */
	public void sendCommand( MacroObject macro )
	{
		this.macroSettings.playMacro( macro );
	}

	/*
	 * *****************************************************
	 * GETTERS
	 * ****************************************************
	 */
	// /**
	// * Returns the controller for the robot. The controller helps the user
	// * to perform some basic commands. For more advanced solutions use the
	// * sendCommand method instead and create own commands
	// *
	// * @author Nicklas Gavelin
	// * @return The robot controller
	// */
	// public RobotController getController()
	// {
	// return this.controller;
	// }
	/**
	 * Checks if a given Bluetooth address is a valid Sphero address or not.
	 * 
	 * @param address The Bluetooth address
	 * 
	 * @return True if valid, false otherwise
	 */
	public static boolean isValidAddress( String address )
	{
		return( address.startsWith( ROBOT_ADDRESS_PREFIX ) );
	}

	/**
	 * Returns true if the robot is connected
	 * 
	 * @return True if connected to the robot, false otherwise
	 */
	public boolean isConnected()
	{
		return this.connected;
	}

	/**
	 * Returns the Bluetooth connection address or null if no
	 * address could be returned
	 * 
	 * @return The Bluetooth connection URL
	 */
	public String getConnectionURL()
	{
		return this.bt.getConnectionURL();
	}

	/**
	 * Checks if a given Bluetooth device is a valid Sphero Bluetooth device or
	 * not.
	 * 
	 * @param device The Bluetooth device
	 * 
	 * @return True if valid, false otherwise
	 */
	public static boolean isValidDevice( BluetoothDevice device )
	{
		return( device.getAddress().startsWith( ROBOT_ADDRESS_PREFIX ) );
	}

	/**
	 * Returns the robot unique id (identical to the Bluetooth address of the
	 * device)
	 * 
	 * @return The unique Bluetooth id
	 */
	public String getId()
	{
		return this.bt.getAddress();// this.bt.getRemoteDevice().getBluetoothAddress();
	}

	/**
	 * Returns the Bluetooth address of the robot.
	 * Same as getId()
	 * 
	 * @return The Bluetooth address of the robot
	 */
	public String getAddress()
	{
		return this.bt.getAddress();// this.bt.getRemoteDevice().getBluetoothAddress();
	}

	/**
	 * Returns the name of the robot
	 * 
	 * @return The name of the robot
	 */
	public String getName()
	{
		String n = this.bt.getName();
		if( n == null )
			return this.name;
		return n;
	}

//	/**
//	 * Returns this, used in threads to access the robot as you
//	 * can't use "this"
//	 * 
//	 * @deprecated Use Robot.this instead
//	 * @return The robot
//	 */
//	private Robot getRobot()
//	{
//		return this;
//	}

	/**
	 * Returns the robot led
	 * 
	 * @return The robot led
	 */
	public Robot.RobotLED getLed()
	{
		return this.led;
	}

	/**
	 * Returns the robot movement
	 * 
	 * @return The robot movement
	 */
	public Robot.RobotMovement getRobotMovement()
	{
		return this.movement;
	}

	/**
	 * Returns the raw movements of the Sphero robot
	 * 
	 * @return The raw movements of the robot
	 */
	public Robot.RobotRawMovement getRobotRawMovement()
	{
		return this.rawMovement;
	}

	/*
	 * *****************************************************
	 * STREAM LISTENER/WRITER
	 * ****************************************************
	 */
	/**
	 * Handles the listening for the connected robot
	 * 
	 * @author Nicklas Gavelin
	 */
	private class RobotStreamListener extends Thread
	{
		// Thread motorStop/continue
		private boolean stop = false;
		// Bluetooth connection to use
		private BluetoothConnection btc;
		// Queue for commands that are waiting for responses
		private LinkedList<Pair<CommandMessage, Boolean>> waitingForResponse;

		/**
		 * Create a listener from the Bluetooth connection
		 * 
		 * @param btc The Bluetooth connection
		 */
		public RobotStreamListener( BluetoothConnection btc )
		{
			this.btc = btc;
			this.waitingForResponse = new LinkedList<Pair<CommandMessage, Boolean>>();
		}

		/**
		 * Enqueue a command that are waiting for a response from the device
		 * 
		 * @param cmd The pair of the command and the flag that tells if it's a
		 *            system command or not
		 */
		protected void enqueue( Pair<CommandMessage, Boolean> cmd )
		{
			this.waitingForResponse.add( cmd );
		}

		/**
		 * Stop the actively running thread
		 */
		public void stopThread()
		{
			this.stop = true;
		}

		private byte[] linkedToArray( List<Byte> list )
		{
			byte[] d = new byte[ list.size() ];
			for( int i = 0; i < list.size(); i++ )
				d[i] = list.get( i );

			// for ( int i = 0; list.size() > 0; i++ )
			// d[ i] = list.remove( 0 );

			return d;
		}

		/**
		 * Runs the listening of the socket
		 */
		@Override
		public void run()
		{
			// ByteArrayBuffer buf = new ByteArrayBuffer( BUFFER_SIZE );

			// Create a data array that contains all our read
			// data.
			byte[] data = new byte[ ProjectProperties.getInstance().getBufferSize() ];
			LinkedList<Byte> buffer = new LinkedList<Byte>();

			// Run until we manually motorStop the thread
			while( !this.stop )
			{
				try
				{
					int read = this.btc.read( data );
					if( read == -1 )
						throw new IOException( "Reached end of stream" );

					// Append all newly read values to our buffer
					// These values may only be the header or may as well be
					// multiple messages depending on how much we could read this time
					for( int k = 0; k < read; k++ )
						buffer.add( data[k] );

					// Now we will continue to read until we got the whole message
					// if we already have the whole message, skip this part
					for( int dataLength = 0; buffer.size() < ( ResponseMessage.RESPONSE_HEADER_LENGTH + dataLength ); )
					{
						// We need to read more of the data input to get a complete
						// message
						// Now read once again until we reach the end of the message
						read = this.btc.read( data ); // Store it in the data array as
														// earlier

						// Append the read data to our data list
						for( int k = 0; k < read; k++ )
							buffer.add( data[k] );

						// Now we have read a number of bytes and may have the complete
						// message
						// But we will check so that we have read equal to the complete
						// message
						// length or more (more messages than one is fine as long as we
						// have at least one
						// complete message)
						if( buffer.size() > ResponseMessage.PAYLOAD_LENGTH_INDEX )
						{
							// We check the length of the packet by reading the length
							// index
							// These indexes are the same both for information and regular
							// packets
							// so it's fine selecting whatever index that we need
							dataLength = linkedToArray( buffer )[ResponseMessage.PAYLOAD_LENGTH_INDEX];
						}
					}

					// Now we have at least one fine packet
					// convert the linked list to an array that we can read from (easier
					// reading)
					byte[] nData = linkedToArray( buffer );

					// Now we have our read data, the next step is to start reading
					// messages until
					// we have read all completed messages in the array, after we have
					// read all messages
					// we will dump all remaining data in the buffer once again and then
					// we will continue
					// reading from the top again
					int read2 = 0; // Read to point in array
					for( int pointer = 0; pointer < buffer.size() && ( nData.length - pointer >= ResponseMessage.RESPONSE_HEADER_LENGTH ) && ( nData.length - pointer >= ( ResponseMessage.RESPONSE_HEADER_LENGTH + nData[pointer + ResponseMessage.PAYLOAD_LENGTH_INDEX] ) ); )
					{
						// Now the above restrictions make these things come true
						// 1. Our current position in the buffer array (nData) is not more
						// than our buffer size
						// 2. Our current message length is above that of the header
						// length of a message (We got a header to read)
						// 3. Our current message length is above that of the header
						// length + the packet length (We got a complete packet to read)
						// These restriction makes us able to read a COMPLETED message and
						// not only the header part of the message

						// Now we will start by creating an object for our header
						// The header will select our specific message values such as
						// response code and type
						// and also the length of the contained data
						ResponseMessage.ResponseHeader drh = new ResponseMessage.ResponseHeader( nData, pointer );

						// Check the type of the response,
						// Regular response is messages received after sending a command
						// to the device
						// Information response is messages received as an effect of
						// sending a specific command that
						// sets the Sphero to keep sending information for some given
						// reason
						switch ( drh.getResponseType() )
						{
						/* Regular response message */
							case REGULAR:
								// We have received the message as an action that depends
								// on a message
								// we sent earlier, now check which message that this
								// response corresponds to
								Pair<CommandMessage, Boolean> cmd = waitingForResponse.remove();

								// Fetch the type of command that we sent, this is used
								// for debugging purposes
								CommandMessage.COMMAND_MESSAGE_TYPE cmdType = cmd.getFirst().getCommand();

								// The command that we sent will act as the decider for
								// which type of response that
								// we received. The response we create is in fact the
								// response which corresponds to the
								// command that we sent, although it's an super type that
								// we extend for increased functionality
								ResponseMessage response = ResponseMessage.valueOf( cmd.getFirst(), drh );

								// Print some debug information that will help us if we
								// end up with trouble later on
								Logging.debug( "Received response packet: " + response + ( cmd.getSecond() ? " as a SYSTEM RESPONSE" : "" ) );

								// Update internal values if we got an OK response code
								// from the robot
								// on the command that we sent. We use a switch case
								// instead of an if/elseif for nicer looking code ;-)
								switch ( drh.getResponseCode() )
								{
								/*
								 * Code OK, nothing went wrong with the command that we
								 * sent
								 */
									case CODE_OK:
										// Update the internal settings for the robot with
										// the response stuff that we have received
										updateInternalValues( cmd.getFirst() );
										break;
									default:
										Logging.error( "Received response code " + drh.getResponseCode() + " for " + cmdType );
										break;
								}

								// Check if we sent the command as a system command
								// (command sent by the inner classes or robot class for
								// setting
								// up the device itself and not by the user)
								if( cmd.getSecond() ) // System command
								{
									// The sent command is a system command
									// Check which type of command to see if we need to
									// update something internal
									switch ( cmdType )
									{
									/*
									 * A bluetooth information message that returns
									 * information about the bluetooth
									 * connection
									 */
										case GET_BLUETOOTH_INFO:
											// Check that the response is OK so that we
											// can do something with our data
											if( drh.getResponseCode().equals( ResponseMessage.RESPONSE_CODE.CODE_OK ) )
											{
												// Update Sphero name
												GetBluetoothInfoResponse gb = (GetBluetoothInfoResponse) response;
												if( !gb.isCorrupt() )
													name = gb.getName();
												break;
											}
											break;
										case RGB_LED_OUTPUT:
											if( Robot.this.disconnecting )
											{
												if( cmd.getFirst().getCommand().equals( CommandMessage.COMMAND_MESSAGE_TYPE.RGB_LED_OUTPUT ) )
												{
													// Notify
													// We are disconnecting
													Robot.this.disconnecting = false;
													this.stopThread();
												}
											}
											break;
									}
								}
								else
								// Notify user
								{
									// The sent command is a user sent command that we
									// need to notify the user about
									Robot.this.notifyListenersDeviceResponse( response, cmd.getFirst() );
								}
								break;

							/* Information response message */
							case INFORMATION:
								// Check if we got a OK response code so that we can read
								// the message that we received
								// Otherwise we need to throw away the message
								switch ( drh.getResponseCode() )
								{
								/* OK response code, message is fine */
									case CODE_OK:
										// Now create our message from the data that we
										// have received
										InformationResponseMessage dir = InformationResponseMessage.valueOf( drh );

										if( !dir.isCorrupt() )
										{
											// Message content is OK and we can send the
											// data onwards for handling
											switch ( dir.getInformationResponseType() )
											{
											/* Data message, contains sensor data */
											/* Emit macro message */
												case EMIT:
													if( Robot.this.macroSettings.macroRunning )
													{
														// We have a macro running and
														// received an emit message
														// now we want to continue sending
														// any data that is left
														// for transmission regarding a
														// macro
														if( Robot.this.macroSettings.ballMemory.size() > 0 )
														{
															// Remove the size of the last
															// macro that we have
															// allocated for the macro
															// data
															// as the robot has a limited
															// amount of memory for macro
															// storage
															Robot.this.macroSettings.ballMemory.remove( (Integer) macroSettings.ballMemory.toArray()[0] );
														}

														// Transmit any remaining macro
														// data now that we got more
														// memory on the device
														Robot.this.macroSettings.emptyMacroCommandQueue();
														Robot.this.macroSettings.stopIfFinished();
													}
													break;

												/*
												 * Data message and any other type of
												 * message
												 */
												case DATA:
													// Notify listeners about a received
													// data message
													Robot.this.notifyListenersInformationResponse( dir );
													break;

												/*
												 * Not implemented type of information
												 * message received, ignore it and log
												 * this
												 * occurrence
												 */
												default:
													// Logging.error(
													// "Unkown type of information message was received "
													// );
													break;
											}
										}
										else
											// Received a corrupt message code for some
											// reason, log the instance
											Logging.error( "Received corrupt information response message " + dir );
										break;
								}
								break;

							/* Unknown response code received */
							default:
								// Logging.error( "Unkown response type received: " +
								// drh.getResponseType() );
								break;
						}

						// Now we need to move our pointer forward so that
						// we may continue to read any other messages that we have read in
						// our
						// buffer array, but first check which type of header that we need
						// to use for calculating the complete packet length
						int headerLength = ( drh.getResponseType().equals( ResponseMessage.ResponseHeader.RESPONSE_TYPE.INFORMATION ) ? ResponseMessage.INFORMATION_RESPONSE_HEADER_LENGTH : ResponseMessage.RESPONSE_HEADER_LENGTH );

						// Add the current packet length to the data pointer
						read2 = ( pointer += drh.getPayloadLength() + headerLength );
					}

					// Now we need to clear our data array and add any data that
					// we couldn't read cause it was incomplete to our buffer
					// for handling when we have read more information
					buffer.clear();

					// Add the remaining data to the buffer by reading
					// from our abandoned position
					for( ; read2 < nData.length; read2++ )
						buffer.add( nData[read2] );
				}
				catch( NullPointerException e )
				{
					Logging.error( "NullPointerException", e );
				}
				catch( NoSuchElementException e )
				{
					Logging.error( "NoSuchElementException", e );
				}
				catch( Exception e )
				{
					if( connected )
						Logging.fatal( "Listening thread closed down unexpectedly", e );
					connectionClosedUnexpected();
				}
			}
		}
	}

//	/**
//	 * Performs updates depending on which messages that are sent
//	 * 
//	 * @param sent The sent messages
//	 */
//	private void update( Collection<Pair<CommandMessage, Boolean>> sent )
//	{
//		for( Pair<CommandMessage, Boolean> p : sent )
//		{
//			switch ( p.getFirst().getCommand() )
//			{
//				case SAVE_MACRO:
//					if( this.macroSettings.macroRunning )
//					{
//						// Macro has been saved, now get the fuck out!
//						if( this.macroSettings.ballMemory.size() > 0 )
//							this.macroSettings.ballMemory.remove( 0 );
//						this.macroSettings.emptyMacroCommandQueue();
//					}
//					break;
//			}
//		}
//	}

	/**
	 * Handles the sending of commands to the active robot.
	 * Manages multiple queues (one timer and one sending queue). The
	 * sending queue is for sending direct messages and the timer queue
	 * is used to schedule commands to be sent after a certain delay
	 * or with periodic transmissions.
	 * 
	 * @author Nicklas Gavelin
	 */
	private class RobotSendingQueue extends Timer
	{
		// Internal storage
		private boolean stop = false, stopAccepting = false;
		private final BluetoothConnection btc;
		// Writer & queue that the writer uses
		private Robot.RobotSendingQueue.Writer w;
		private final BlockingQueue<Pair<CommandMessage, Boolean>> sendingQueue;

		/**
		 * Create a robot stream writer for a specific Bluetooth connection
		 * 
		 * @param btc The Bluetooth connection to send to
		 */
		protected RobotSendingQueue( BluetoothConnection btc )
		{
			this.btc = btc;
			this.sendingQueue = new LinkedBlockingQueue<Pair<CommandMessage, Boolean>>();
			this.w = new Robot.RobotSendingQueue.Writer();

			this.startWriter();
		}

		/**
		 * Start the writer thread.
		 * The writer will motorStop at the same time as the RobotSendinQueue is
		 * stopped.
		 */
		private void startWriter()
		{
			this.w.start();
		}

		/**
		 * Forces a command to be sent even if the stopAccepting flag
		 * is set to true. The command sent will be a system command
		 * 
		 * @param command The command to enqueue
		 */
		public void forceCommand( CommandMessage command )
		{
			this.sendingQueue.add( new Pair<CommandMessage, Boolean>( command, true ) );
		}

		/**
		 * Enqueue a single command to be sent as soon as possible without using
		 * the timer objects that are often used to enqueue commands to be sent
		 * after a certain delay.
		 * 
		 * @param command The command to send
		 * @param systemCommand True if the command is a system command, false
		 *            otherwise
		 */
		public void enqueue( CommandMessage command, boolean systemCommand )
		{
			synchronized( sendingQueue )
			{
				try
				{
					if( !this.stop && !this.stopAccepting )
						this.sendingQueue.put( new Pair<CommandMessage, Boolean>( command, systemCommand ) );
				}
				catch( InterruptedException e )
				{
				}
			}
		}

		/**
		 * Enqueue a single command to be sent as soon as possible without using
		 * the timer objects that are often used to enqueue commands to be sent
		 * after a certain delay. The command will be sent as a SYSTEM command
		 * and will not notify any RobotListeners after a response is received!
		 * 
		 * @param command The command to send
		 * @param delay The delay to send the command after (in ms)
		 */
		public void enqueue( CommandMessage command, float delay )
		{
			this.enqueue( command, delay, false );
		}

//		/**
//		 * Enqueue a command with a certain repeat period and initial delay
//		 * before sending the
//		 * first message. <b>The message will be repeated as long as the writer
//		 * allows it</b>.
//		 * 
//		 * @param command The command to transmit
//		 * @param initialDelay The initial delay before sending the first one
//		 * @param periodLength The period length between the transmissions
//		 */
//		public void enqueue( CommandMessage command, float initialDelay, float periodLength )
//		{
//			this.enqueue( command, false, initialDelay, periodLength );
//		}

		/**
		 * Enqueue a command with a certain repeat period and initial delay
		 * before sending the
		 * first message. <b>The message will be repeated as long as the writer
		 * allows it</b>.
		 * 
		 * @param command The command to send
		 * @param systemCommand True for a system command, false otherwise
		 * @param initialDelay The initial delay for sending
		 * @param periodLength The period length between transmissions
		 */
		public void enqueue( CommandMessage command, boolean systemCommand, float initialDelay, float periodLength )
		{
			if( !this.stop && !this.stopAccepting )
				this.schedule( new Robot.RobotSendingQueue.CommandTask( new Pair<CommandMessage, Boolean>( command, systemCommand ) ), (long) initialDelay, (long) periodLength );
		}

		/**
		 * Enqueue an already existing command task to run at a certain initial
		 * delay and
		 * a certain period length
		 * 
		 * @param task The task to run after the timer runs
		 * @param delay The delay before running the task in milliseconds
		 */
		private void enqueue( Robot.RobotSendingQueue.CommandTask task, float delay )
		{
			if( !this.stop && !this.stopAccepting )
				this.schedule( task, (long) delay );
		}

		/**
		 * Enqueue a single command to be sent after a specific delay
		 * 
		 * @param command The command to send
		 * @param delay The delay to send after (in ms)
		 * @param systemCommand True if the command is a system command, false
		 *            otherwise
		 */
		public void enqueue( CommandMessage command, float delay, boolean systemCommand )
		{
			if( !this.stop && !this.stopAccepting )
				this.schedule( new Robot.RobotSendingQueue.CommandTask( new Pair<CommandMessage, Boolean>( command, systemCommand ) ), (long) delay );
		}

		/**
		 * Stops the current timer. Will not be possible to restart it once
		 * this method is run!
		 */
		@Override
		public void cancel()
		{
			this.stopAccepting = true;
			super.cancel();
		}

		/**
		 * Stop everything
		 */
		public void stopAll()
		{
			this.stop = true;
		}

		/**
		 * Handles the transmission of a single command
		 * 
		 * @author Nicklas Gavelin
		 */
		private class CommandTask extends TimerTask
		{
			// Storage of the command to send
			private Pair<CommandMessage, Boolean> execute;
			private int repeat = 0;
			private float delay;
			private boolean repeating = false;

			/**
			 * Create a command task to send a command
			 * 
			 * @param execute The command together with a boolean value
			 *            describing if it's a system message or not
			 */
			private CommandTask( Pair<CommandMessage, Boolean> execute )
			{
				this.execute = execute;
			}

			/**
			 * Create a command task with a repeated number of runs
			 * 
			 * @param execute The command to execute
			 * @param delay The delay between the commands
			 * @param repeat The number of repeats to perform (-1 for infinite
			 *            repeats)
			 */
			private CommandTask( Pair<CommandMessage, Boolean> execute, float delay, int repeat )
			{
				this( execute );
				this.repeat = repeat;
				this.delay = delay;

				if( repeat != -1 ) // Infinite command, will be sent until the end of
									// time!
					this.repeating = true;
			}

			@Override
			public void run()
			{
				// Enqueue the command directly to the writer
				enqueue( execute.getFirst(), execute.getSecond() );

				// Check if we want to repeat the command
				if( repeating )
				{
					if( repeat == -1 || --repeat > 0 )
						enqueue( this, delay );

				}
			}
		}

		/**
		 * Handles all transmissions to the Sphero device.
		 * 
		 * @author Nicklas Gavelin
		 */
		private class Writer extends Thread
		{
			@Override
			public void run()
			{
				ByteArrayBuffer sendingBuffer = new ByteArrayBuffer( 256 );

				// Run until we manually motorStop the thread or
				// a connection error occurs.
				while( !stop )
				{
					try
					{
						// Fetch a message from the sending queue and append the data of that packet to our
						// sending buffer. We will then try to add more data to our sending buffer.
						Pair<CommandMessage, Boolean> p = sendingQueue.take();

						// Append message to sending buffer
						sendingBuffer.append( p.getFirst().getPacket(), 0, p.getFirst().getPacketLength() );

						// Add command to listening queue
						listeningThread.enqueue( p );
						// sent.add( p );

						Logging.debug( "Queueing " + p.getFirst() );

						// Lock until we have sent our messages in-case someone
						// else tries to do access our sendingQueue at the same time (enqueue)
						synchronized( sendingQueue )
						{
							try
							{
								// We will try to send as much as we can
								if( !sendingQueue.isEmpty() )
								{
									// Go through all the messages that we can
									for( int i = 0; i < sendingQueue.size(); i++ )
									{
										Pair<CommandMessage, Boolean> c = sendingQueue.peek();

										// Peek at the the rest of the messages
										int length = c.getFirst().getPacketLength();

										// Check that we have enough space to add the next
										// message to, if not
										// send what we got and continue later on
										if( sendingBuffer.length() - length < 0 )
											break;

										// Enqueue the next command
										sendingBuffer.append( c.getFirst().getPacket(), 0, c.getFirst().getPacketLength() );
										listeningThread.enqueue( c );
										// sent.add( c );
										sendingQueue.remove();

										Logging.debug( "Queueing " + c.getFirst() );
									}
								}

								// Write to socket
								Logging.debug( "Sending " + sendingBuffer );
								btc.write( sendingBuffer.toByteArray() );
								btc.flush();

								// update( sent );
							}
							catch( IOException e )
							{
								// Remove last until we have removed all messages that we
								// tried to send
								// for ( int i = 0; i < added; i++ ) // TODO: Remove this
								// listeningThread.removeLast();

								// Close unexpectedly
								if( connected )
									Logging.fatal( "Writing thread closed down unexpectedly", e );
								connectionClosedUnexpected();
							}
							finally
							{
								sendingBuffer.clear();
							}
						}
					}
					catch( InterruptedException e )
					{
					} // Nothing important, just continue on
				}
			}
		}
	}

	/**
	 * Manages transmission of Macro commands and allows for 
	 * streaming possibilities (dividing macro into pieces and sending them off
	 * one by one until the whole macro has been transmitted and played)
	 * 
	 * @author Orbotix
	 * @author Nicklas Gavelin
	 */
	private class MACRO_SETTINGS
	{
		private final Collection<MacroCommand> commands;
		private final Collection<CommandMessage> sendingQueue;
		private final Collection<Integer> ballMemory;
		private boolean macroRunning, macroStreamingEnabled;
		private int emits = 0;

		/**
		 * Create a macro settings object
		 */
		private MACRO_SETTINGS()
		{
			commands = new ArrayList<MacroCommand>();
			sendingQueue = new ArrayList<CommandMessage>();
			ballMemory = new ArrayList<Integer>();
			macroRunning = false;
			macroStreamingEnabled = true;
		}

		/**
		 * Stop any current macros from running
		 */
		private void stopMacro()
		{
			// Abort the current macro
			sendCommand( new AbortMacroCommand() );

			// Clear the memory
			this.commands.clear();
			this.ballMemory.clear();

			// Set motorStop flag
			this.macroRunning = false;
		}

		/**
		 * Stop macro from executing (finished)
		 */
		protected void stopIfFinished()
		{
			emits = ( emits > 0 ? emits - 1 : 0 );
			if( this.commands.isEmpty() && this.macroRunning && emits == 0 )
			{
//				for( CommandMessage cmd : this.sendingQueue )
//					sendCommand( cmd );
//				this.sendingQueue.clear();
				this.stopMacro();

				// Notify listeners about macro done event
				Robot.this.notifyListenerEvent( EVENT_CODE.MACRO_DONE );
			}
		}

		/**
		 * Play a given macro object
		 * 
		 * @param macro The given macro object
		 */
		private void playMacro( MacroObject macro )
		{
			if( macro.getMode().equals( MacroObject.MacroObjectMode.Normal ) )
			{
				// Normal macro mode
				Robot.this.sendSystemCommand( new SaveTemporaryMacroCommand( 1, macro.generateMacroData() ) );
				Robot.this.sendSystemCommand( new RunMacroCommand( -1 ) );
			}
			else
			{
				if( !macroStreamingEnabled )
					return;

				if( macro.getMode().equals( MacroObject.MacroObjectMode.CachedStreaming ) )
				{
					// Cached streaming mode
					if( !macro.getCommands().isEmpty() )
					{
						// Get all macro commands localy instead
						// this.commands.clear();
						this.commands.addAll( macro.getCommands() );

						this.macroRunning = true;

						// Now empty our queue
						this.emptyMacroCommandQueue();

						// if ( !this.macroRunning )
						// {
						// this.macroRunning = true;
						// // this.sendSystemCommand( new RunMacroCommand( -2 ) );
						// }
					}
				}
			}
		}

		/**
		 * Send a command after a CachedStreaming macro has run
		 * 
		 * @param command The command to send after the macro is finished
		 *            running
		 */
		public void sendCommandAfterMacro( CommandMessage command )
		{
			this.sendingQueue.add( command );
		}

		/**
		 * Clears the queue used for storing commands to send after the macro
		 * has finished running
		 */
		public void clearSendingQueue()
		{
			this.sendingQueue.clear();
		}

		/**
		 * Continue emptying the macro command queue by creating new commands
		 * and sending them to the Sphero device
		 */
		private synchronized void emptyMacroCommandQueue()
		{
			// Calculate number of free bytes that we have
			int ballSpace = freeBallMemory(), freeBytes = ( ballSpace > rs.macroMaxSize ? rs.macroMaxSize : ballSpace ), chunkSize = 0;

			// Check if we need or can create more commands
			if( this.commands.isEmpty() || ballSpace <= rs.macroMinSpaceSize )
				return;

			// Create our sending collection (stuff that we want to send)
			Collection<MacroCommand> send = new ArrayList<MacroCommand>();

			// Emit marker (we will receive a message from the Sphero when this emit
			// marker is reached)
			Emit em = new Emit( 1 );
			int emitLength = em.getLength();

			// Go through new commands that we want to send
			for( MacroCommand cmd : this.commands )
			{
				// Check if we allow for the new command to be added (that we still got
				// enough space left to add it)
				if( freeBytes - ( chunkSize + cmd.getLength() + emitLength ) <= 0 || ( chunkSize + cmd.getLength() + emitLength ) > rs.macroMaxSize )
					break;

				// Add the command to the send queue and increase the space we've used
				send.add( cmd );
				chunkSize += cmd.getLength();
			}

			// Remove the commands that we can send from the waiting command queue
			this.commands.removeAll( send );

			// Add emitter
			send.add( em );
			chunkSize += em.getLength();

			// Create our sending buffer to add commands to
			ByteArrayBuffer sendBuffer = new ByteArrayBuffer( chunkSize );

			// Add all commands to the buffer
			for( MacroCommand cmd : send )
				sendBuffer.append( cmd.getByteRepresentation() );

			if( commands.isEmpty() )
				sendBuffer.append( MacroCommand.MACRO_COMMAND.MAC_END.getValue() );

			this.ballMemory.add( chunkSize );

			// Send a save macro command to the Sphero with the new data
			SaveMacroCommand svc = new SaveMacroCommand( SaveMacroCommand.MacroFlagMotorControl, SaveMacroCommand.MACRO_STREAMING_DESTINATION, sendBuffer.toByteArray() );

			emits++;
			Robot.this.sendSystemCommand( svc );

			// Check if we can continue creating more messages to send
			if( !this.commands.isEmpty() && freeBallMemory() > rs.macroMinSpaceSize )
				this.emptyMacroCommandQueue();
		}

		/**
		 * Returns the number of free bytes for the ball
		 * 
		 * @return The number of free bytes for the Sphero device
		 */
		private int freeBallMemory()
		{
			int bytesInUse = 0;
			for( Iterator<Integer> i = this.ballMemory.iterator(); i.hasNext(); )
				bytesInUse = bytesInUse + i.next().intValue();

			return( rs.macroRobotStorageSize - bytesInUse );
		}
	}

	/**
	 * Holds the robot position, rotation rate and the drive algorithm used.
	 * All the internal values may be accessed with the get methods that are
	 * available.
	 * 
	 * @author Nicklas Gavelin
	 */
	public class RobotMovement
	{
		// The current values
		private float heading, velocity, rotationRate;
		private boolean stop = true;
		// The current drive algorithm that is used for calculating velocity
		// and motorHeading when running .drive no Robot
		private DriveAlgorithm algorithm;

		/**
		 * Create a new robot movement object
		 */
		private RobotMovement()
		{
			this.reset();
		}

		/**
		 * Returns the current motorHeading of the robot
		 * 
		 * @return The current motorHeading of the robot (0-360)
		 */
		public float getHeading()
		{
			return this.heading;
		}

		/**
		 * Returns the current velocity of the robot
		 * 
		 * @return The current velocity (0-1)
		 */
		public float getVelocity()
		{
			return this.velocity;
		}

		/**
		 * Returns the current rotation rate of the robot.
		 * 
		 * @return The current rotation rate (0-1)
		 */
		public float getRotationRate()
		{
			return this.rotationRate;
		}

		/**
		 * Returns the current motorStop value of the robot.
		 * True means the robot is stopped, false means it's
		 * moving with a certain velocity
		 * 
		 * @return True if moving, false otherwise
		 */
		public boolean getStop()
		{
			return this.stop;
		}

		/**
		 * Returns the current drive algorithm that is used to
		 * calculate the velocity and motorHeading when running .drive on Robot
		 * 
		 * @return The current drive algorithm
		 */
		public DriveAlgorithm getDriveAlgorithm()
		{
			return this.algorithm;
		}

		/**
		 * Resets all values of the class instance.
		 * Will NOT send any commands to the robot, this has to be
		 * done manually! BE SURE TO DO IT!
		 */
		private void reset()
		{
			this.heading 		= rs.motorHeading;
			this.velocity 		= rs.motorStartSpeed;
			this.rotationRate 	= rs.motorRotationRate;
			this.stop 			= rs.motorStop;
			this.algorithm 		= new RCDriveAlgorithm();
		}
	}

	/**
	 * Raw movement values for the robot. These have no
	 * connection to the ordinary robot movement RobotMovement as
	 * these use direct commends to the engines instead of pre-defined
	 * commands.
	 * 
	 * @author Nicklas Gavelin
	 */
	public class RobotRawMovement
	{
		// Holds motor speed and mode (Forward, Reverse)
		private int leftMotorSpeed, rightMotorSpeed;
		private MOTOR_MODE leftMotorMode, rightMotorMode;

		/**
		 * Create a new raw robot movement
		 */
		private RobotRawMovement()
		{
			this.reset();
		}

		/**
		 * Returns the left motor speed
		 * 
		 * @return The left motor speed
		 */
		public int getLeftMotorSpeed()
		{
			return this.leftMotorSpeed;
		}

		/**
		 * Returns the right motor speed
		 * 
		 * @return The right motor speed
		 */
		public int getRightMotorSpeed()
		{
			return this.rightMotorSpeed;
		}

		/**
		 * Returns the left motor mode
		 * 
		 * @return The left motor mode (Forward/Reverse)
		 */
		public MOTOR_MODE getLeftMotorMode()
		{
			return this.leftMotorMode;
		}

		/**
		 * Returns the right motor mode
		 * 
		 * @return The right motor mode (Forward/Reverse)
		 */
		public MOTOR_MODE getRightMotorMode()
		{
			return this.rightMotorMode;
		}

		/**
		 * Resets the internal values.
		 * WARNING: WILL NOT SEND _ANY_ COMMANDS TO THE SPHERO DEVICE, THIS
		 * HAS TO BE DONE MANUALLY!
		 */
		private void reset()
		{
			this.leftMotorSpeed = this.rightMotorSpeed = rs.motorStartSpeed;
			this.leftMotorMode 	= this.rightMotorMode = rs.motorMode;
		}
	}

	/**
	 * Manages the RGB and LED brightness information to prevent 
	 * storing this directly in objects in the Robot instance.
	 * 
	 * @author Nicklas Gavelin
	 */
	public class RobotLED
	{
		// Internal values
		private int red, green, blue;
		private float brightness;

		/**
		 * Create a new robot led object
		 */
		private RobotLED()
		{
			this.reset();
		}

		/**
		 * Returns the red color value (0-255)
		 * 
		 * @return The red color value
		 */
		public int getRGBRed()
		{
			return this.red;
		}

		/**
		 * Returns the green color value (0-255)
		 * 
		 * @return The green color value
		 */
		public int getRGBGreen()
		{
			return this.green;
		}

		/**
		 * Returns the blue color value (0-255)
		 * 
		 * @return The blue color value
		 */
		public int getRGBBlue()
		{
			return this.blue;
		}

		/**
		 * Returns the RGB Color value for the internal RGB LED
		 * 
		 * @return The color for the RGB LED
		 */
		public Color getRGBColor()
		{
			return( new Color( this.red, this.green, this.blue ) );
		}

		/**
		 * Returns the ledBrightness of the front led (0-1)
		 * 
		 * @return The ledBrightness level of the front led
		 */
		public float getFrontLEDBrightness()
		{
			return this.brightness;
		}

		/**
		 * Resets the internal values to default values
		 */
		private void reset()
		{
			// Set white color (default for connected devices)
			this.red 	= rs.ledRGB.getRed();
			this.green 	= rs.ledRGB.getGreen();
			this.blue 	= rs.ledRGB.getBlue();

			// Reset the ledBrightness to 0 (off)
			this.brightness = rs.ledBrightness;
		}
	}
}
