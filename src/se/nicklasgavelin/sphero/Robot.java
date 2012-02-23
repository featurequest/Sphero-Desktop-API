package se.nicklasgavelin.sphero;

import java.awt.Color;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import se.nicklasgavelin.bluetooth.BluetoothConnection;
import se.nicklasgavelin.bluetooth.BluetoothDevice;
import se.nicklasgavelin.log.Logging;
import se.nicklasgavelin.sphero.RobotListener.EVENT_CODE;
import se.nicklasgavelin.sphero.command.RawMotorCommand.MOTOR_MODE;
import se.nicklasgavelin.sphero.command.*;
import se.nicklasgavelin.sphero.exception.InvalidRobotAddressException;
import se.nicklasgavelin.sphero.exception.RobotBluetoothException;
import se.nicklasgavelin.sphero.exception.RobotInitializeConnectionFailed;
import se.nicklasgavelin.sphero.response.DeviceResponse;
import se.nicklasgavelin.sphero.response.DeviceResponse.RESPONSE_CODE;
import se.nicklasgavelin.sphero.response.DeviceResponseHeader;
import se.nicklasgavelin.sphero.response.GetBluetoothInfoResponse;
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
 * Technology
 * @version 1.1
 *
 * TODO: Set temporary internal values on sending commands so that we don't
 * update them too late if we send multiple commands
 */
public class Robot
{
    // Logger for the robot class
//    private Logger logger = null;
//    private static final Level logLevel = Level.WARN;
    // Bluetooth
    private BluetoothDevice bt;
    private BluetoothConnection btc;
    private boolean connected = false;
    // Listener/writer
    private RobotStreamListener listeningThread;
    private RobotSendingQueue sendingTimer;
    private List<RobotListener> listeners;
    // Other
    private String name = null;

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
        private float heading,
                velocity,
                rotationRate;
        private boolean stop = true;
        // The current drive algorithm that is used for calculating velocity
        // and heading when running .drive no Robot
        private DriveAlgorithm algorithm;


        /**
         * Create a new robot movement object
         */
        private RobotMovement()
        {
            this.reset();
        }


        /**
         * Returns the current heading of the robot
         *
         * @return The current heading of the robot (0-360)
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
         * Returns the current stop value of the robot.
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
         * calculate the velocity and heading when running .drive on Robot
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
            this.heading = this.velocity = this.rotationRate = 0F;
            this.stop = true;
            this.algorithm = new RCDriveAlgorithm();
        }
    }

    /**
     * Raw movement values for the robot. These have no
     * connection to the ordinary robot movement RobotMovement as
     * these use direct commends to the engines instead of pre-defined
     * commands.
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
            this.leftMotorSpeed = this.rightMotorSpeed = 0;
            this.leftMotorMode = this.rightMotorMode = MOTOR_MODE.FORWARD;
        }
    }

    /**
     * Holds information about the current LED color
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
            return (new Color( this.red, this.green, this.blue ));
        }


        /**
         * Returns the brightness of the front led (0-1)
         *
         * @return The brightness level of the front led
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
            this.red = this.green = this.blue = 255;

            // Reset the brightness to 0 (off)
            this.brightness = 0.0F;
        }
    }
    // Robot position and led color
    private RobotMovement movement;
    private RobotRawMovement rawMovement;
    private RobotLED led;
    // Pinger
    private float PING_INTERVAL = 60000; // Time in milliseconds
    // Address
    public static final String ROBOT_ADDRESS_PREFIX = "00066";
    // Robot controller
//    private RobotController controller;
    private static int error_num = 0;
    private static final String[] invalidAddressResponses = new String[]
    {
        "The bluetooth address is invalid, the Sphero bluetooth address must start with " + ROBOT_ADDRESS_PREFIX,
        "Are you serious? The address is still invalid, the Sphero bluetooth address must start with " + ROBOT_ADDRESS_PREFIX,
        "Are you plain fucking lazy or just stupid? Check your frigging bluetooth address, it still need to start with " + ROBOT_ADDRESS_PREFIX,
        "I give up... You are not taking me seriously and I'm just getting pissed off. Why would I give a shit about bluetooth addresses anyway"
    };


    /**
     * Create a robot from a Bluetooth device. You need to call Robot.connect
     * after creating a robot to connect to it via the Bluetooth connection
     * given.
     *
     * @param bt The Bluetooth device that represents the robot
     */
    public Robot( BluetoothDevice bt ) throws InvalidRobotAddressException, RobotBluetoothException
    {
        this.bt = bt;

        // Create a unique logger for this class instance
        //this.logger = Logging.createLogger( Robot.class, Robot.logLevel, this.bt.getAddress() );

        // Control that we got a valid robot
        if ( !this.bt.getAddress().startsWith( ROBOT_ADDRESS_PREFIX ) )
        {
            String msg = invalidAddressResponses[ Value.clamp( error_num++, 0, invalidAddressResponses.length - 1 )];
            Logging.warn( msg );
            throw new InvalidRobotAddressException( msg );
        }

        // Initialize the position and LEDs
        this.movement = new RobotMovement();
        this.rawMovement = new RobotRawMovement();
        this.led = new RobotLED();

        // Discover the connection services that we can use
        bt.discover();

        // Create an empty array of listeners
        this.listeners = new ArrayList<RobotListener>();

        Logging.debug( "Robot created successfully" );
    }

    /*
     * *****************************************************
     * LISTENERS
     *****************************************************
     */

    /**
     * Add a robot listener to the current class instance
     *
     * @param l The listener to add
     */
    public void addListener( RobotListener l )
    {
        Logging.debug( "Adding listener of type " + l.getClass().getCanonicalName() );

        if ( !this.listeners.contains( l ) )
            this.listeners.add( l );
    }


    /**
     * Remove a listener that is listening from the current class
     * instance.
     *
     * @param l The listener to remove
     */
    public void removeListener( RobotListener l )
    {
        // Check so that we can remove it
        if ( this.listeners.contains( l ) )
            this.listeners.remove( l );
    }


    /**
     * Notify all listeners about a device response
     *
     * @param dr The device response that was received
     * @param dc The device command belonging to the device response
     */
    private void notifyListenersDeviceResponse( DeviceResponse dr, DeviceCommand dc )
    {
        Logging.debug( "Notifying listeners about device respose " + dr + " for device command " + dc );

        // Go through all listeners and notify them
        for ( RobotListener r : this.listeners )
            r.responseReceived( this, dr, dc );
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
        for ( RobotListener r : this.listeners )
            r.event( this, event );
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
        if ( this.connected )
        {
            this.connected = false;
            Logging.error( "Connection closed unexpectedly for some reason, all threads have been closed down for the robot" );
            this.notifyListenerEvent( EVENT_CODE.CONNECTION_CLOSED_UNEXPECTED );
        }
    }

    /*
     * *****************************************************
     * CONNECTION MANAGEMENT
     *****************************************************
     */

    /**
     * Connect to the robot via the bluetooth connection given in the
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
     * to true and intialization failed
     * @return True if connected, will throw exception if not connected
     */
    public boolean connect( boolean throwException )
    {
        try
        {
            return this.internalConnect();
        }
        catch ( RobotBluetoothException e )
        {
            if ( throwException )
                throw new RobotInitializeConnectionFailed( e.getMessage() );
        }
        catch ( RobotInitializeConnectionFailed e )
        {
            if ( throwException )
                throw e;
        }

        return false;
    }


    /**
     * Connects to the robot via bluetooth. Will return true if the connection
     * was successfull, throws and exception otherwise.
     *
     * @throws RobotInitializeConnectionFailed If connection failed
     * @return True if connection succeeded
     */
    private boolean internalConnect() throws RobotInitializeConnectionFailed, RobotBluetoothException
    {
        Logging.debug( "Trying to connect to " + this.getName() + ":" + this.getAddress() );
        this.btc = bt.connect();

        // Check if we could connect to the bluetooth device
        if ( this.btc == null )
        {
            Logging.error( "Failed to connect to the robot bluetooth connection" );
            throw new RobotInitializeConnectionFailed( "Failed to connect due to bluetooth error" );
        }

        // We are now connected, continue with
        // the initialization of everything else regarding the connection
        this.connected = true;

        // Create a controller for our robot
//        this.controller = new RobotController( this );

        // Create a listening thread and close any old ones down
        if ( this.listeningThread != null )
            this.listeningThread.stopThread();
        this.listeningThread = new RobotStreamListener( btc );
        this.listeningThread.start();

        // Create our sending timer
        if ( this.sendingTimer != null )
            this.sendingTimer.cancel();
        this.sendingTimer = new RobotSendingQueue( btc );

        // Reset the robot
        this.sendSystemCommand( new RollCommand( this.movement.heading, this.movement.velocity, this.movement.stop ) );
        this.sendSystemCommand( new CalibrateCommand( this.movement.heading ) );
        this.sendSystemCommand( new FrontLEDCommand( this.led.brightness ) );

        // Create our pinger
        this.sendSystemCommand( new PingCommand( this ), 4000, PING_INTERVAL );

        // Notify listeners
        this.notifyListenerEvent( (this.connected ? EVENT_CODE.CONNECTION_ESTABLISHED : EVENT_CODE.CONNECTION_FAILED) );

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


    /**
     * Method to notify listeners about a disconnect and set the connect flag
     *
     * @param notifyListeners True to notify listeners, false otherwise
     */
    private void disconnect( boolean notifyListeners )
    {
        Logging.debug( "Disconnecting from the current robot" );

        if ( this.connected )
        {
            // Set notify status, a bit ugly but hey.. Quick hack!
            notifyListenersDisconnect = notifyListeners;

            // Close all connection
            this.closeConnections();
        }
        else
        {
            // Check if we want to notify listeners that there exists no active connection
            if ( notifyListeners )
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
        if ( this.connected )
        {
            this.connected = false;

            // Stop our transmission of commands
            this.sendingTimer.cancel();

            // Send a direct command to stop any movement (eludes the .cancel command)
            this.sendingTimer.forceCommand( new RollCommand( 0, 0, true ) );
            this.sendingTimer.forceCommand( new FrontLEDCommand( 0 ) );
        }
    }

    /*
     * *****************************************************
     * COMMANDS
     *****************************************************
     */

    /**
     * Send a command to the active robot
     *
     * @param command The command to send
     */
    public void sendCommand( DeviceCommand command )
    {
        this.sendingTimer.enqueue( command, false );
    }


    /**
     * Send a command with a given delay
     *
     * @param command The command to send
     * @param delay   The delay before the command is sent
     */
    public void sendCommand( DeviceCommand command, float delay )
    {
        this.sendingTimer.enqueue( command, delay );
    }


    /**
     * Send a command infinite times with a certain initial delay and a certain
     * given period length between next-coming messages.
     *
     * @param command      The command to send
     * @param initialDelay The initial delay before the first message is sent
     * (in milliseconds)
     * @param periodLength The length between the transmissions
     */
    public void sendPeriodicCommand( DeviceCommand command, float initialDelay, float periodLength )
    {
        this.sendingTimer.enqueue( command, false, initialDelay, periodLength );
    }


    /**
     * Send a system command
     *
     * @param command The command to send
     */
    private void sendSystemCommand( DeviceCommand command )
    {
        this.sendingTimer.enqueue( command, true );
    }


    /**
     * Send a system command after a given delay
     *
     * @param command The command to send
     * @param delay   The delay before sending the message
     */
    private void sendSystemCommand( DeviceCommand command, float delay )
    {
        this.sendingTimer.enqueue( command, delay, true );
    }


    /**
     * Send a system command infinitely with a certain initial delay before the
     * first message and a period length between nextcomming messages.
     *
     * @param command      The command to send
     * @param initialDelay The initial delay before the first message is sent
     * (in milliseconds)
     * @param periodLength The length between the transmissions
     */
    private void sendSystemCommand( DeviceCommand command, float initialDelay, float periodLength )
    {
        this.sendingTimer.enqueue( command, true, initialDelay, periodLength );
    }
    private boolean receivedFirstDisconnect = false, notifyListenersDisconnect = true;


    /**
     * Updates position, led colors/brightness etc depending on the command that
     * is sent.
     *
     * @param command The command that is suppose to be sent
     */
    private void updateInternalValues( DeviceCommand command )
    {
        // Disconnect event, we will disconnect if we are not connected and
        // we have sent both a roll stop command and a front led turn off command
        if ( !this.connected && (command instanceof FrontLEDCommand || command instanceof RollCommand) )
        {
            if ( receivedFirstDisconnect )
            {
                // Stop any active listening thread
                if ( this.listeningThread != null )
                    this.listeningThread.stopThread();

                // Stop any active sending timer thread
                if ( this.sendingTimer != null )
                    this.sendingTimer.stopAll();

                // Stop the bluetooth connection
                if ( this.btc != null )
                    this.btc.stop();

                // Check if we want to notify anyone
                if ( notifyListenersDisconnect )
                {
                    // Send disconnect event
                    this.notifyListenerEvent( EVENT_CODE.DISCONNECTED );
                }
            }
            else
                receivedFirstDisconnect = true;
        }

        Logging.debug( "Updating internal values for " + command );

        // Update stuff event
        switch ( command.getCommand() )
        {
            /*
             * Roll command, update internal values
             */
            case ROLL:
                RollCommand rc = ( RollCommand ) command;

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
                RawMotorCommand raw = ( RawMotorCommand ) command;
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
                RotationRateCommand rrc = ( RotationRateCommand ) command;
                this.movement.rotationRate = rrc.getRate();

                break;

            case JUMP_TO_BOOTLOADER:
            case GO_TO_SLEEP:
                // Graceful disconnect as we will loose the connection when
                // jumping to the bootloader
                this.disconnect();

                break;

            case RGB_LED_OUTPUT:
                RGBLEDCommand rgb = ( RGBLEDCommand ) command;

                // Update led values
                this.led.red = rgb.getRed();
                this.led.green = rgb.getGreen();
                this.led.blue = rgb.getBlue();

                break;

            case FRONT_LED_OUTPUT:
                FrontLEDCommand flc = ( FrontLEDCommand ) command;
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
     *****************************************************
     */

    /**
     * Roll the robot with a given heading and speed
     *
     * @param heading The heading (0-360)
     * @param speed   The speed (0-1)
     */
    public void roll( float heading, float speed )
    {
        this.sendCommand( new RollCommand( heading, speed, false ) );
    }


    /**
     * Calibrate the heading to a specific heading (Will move the robot to this
     * heading)
     *
     * @param heading The heading to calibrate to (0-359)
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
     * changes between the colors (the transition itself).
     *
     * @param fRed   The initial red color value
     * @param fGreen The initial green color value
     * @param fBlue  The initial blue color value
     * @param tRed   The final red color value
     * @param tGreen The final green color value
     * @param tBlue  The final blue color value
     * @param steps  Number of steps to take (The number of times to change
     * color until reaching the final color)
     */
    public void rgbTransition( int fRed, int fGreen, int fBlue, int tRed, int tGreen, int tBlue, float steps )
    {
        int tdelay = 55;

        // Hue, saturation, intensity
        float[] fHSB = Color.RGBtoHSB( fRed, fGreen, fBlue, null );
        float[] tHSB = Color.RGBtoHSB( tRed, tGreen, tBlue, null );

        float hDif = Math.abs( fHSB[0] - tHSB[0] );
        float sDif = Math.abs( fHSB[1] - tHSB[1] );
        float iDif = Math.abs( fHSB[2] - tHSB[2] );

        boolean iHue = (fHSB[0] < tHSB[0]);
        boolean iSat = (fHSB[1] < tHSB[1]);
        boolean iInt = (fHSB[2] < tHSB[2]);

        float incHue = (hDif / steps);
        float incSat = (sDif / steps);
        float incInt = (iDif / steps);

        Color c;
        float[] n = new float[ 3 ];

        // Go through all steps
        for ( int i = 0; i < steps; i++ )
        {
            // Calculate hue saturation and intensity
            n[0] = (iHue ? fHSB[0] + (i * incHue) : fHSB[0] - (i * incHue));
            n[1] = (iSat ? fHSB[1] + (i * incSat) : fHSB[1] - (i * incSat));
            n[2] = (iInt ? fHSB[2] + (i * incInt) : fHSB[2] - (i * incInt));

            // Get new color
            int ik = Color.HSBtoRGB( n[0], n[1], n[2] );
            c = new Color( ik );

            // Send new values
            this.sendingTimer.enqueue( new RGBLEDCommand( c.getRed(), c.getGreen(), c.getBlue() ), tdelay * i );
        }
    }


    /**
     * Rotate the robot
     *
     * @param heading The new heading, 0-360
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
     * the robot and have to be re-initialized.
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
     * @param red   The new red value
     * @param green The new green value
     * @param blue  The new blue value
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
     * Resets the robots heading.
     *
     * Sends a roll command with current velocity and stop value and also a
     * calibrate
     * command to reset the heading.
     */
    public void resetHeading()
    {
        this.sendCommand( new RollCommand( 0.0F, this.movement.getVelocity(), this.movement.getStop() ) );
        this.sendCommand( new CalibrateCommand( 0.0F ) );
    }


    /**
     * Update heading offset
     *
     * @param offset The heading offset
     */
    public void setHeadingOffset( double offset )
    {
        this.movement.algorithm.headingOffset = offset;
    }


    /**
     * Set brightness of the front led. 0-1
     *
     * @param brightness The brightness value, 0-1
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
        this.roll( ( float ) this.movement.algorithm.adjustedHeading, ( float ) this.movement.algorithm.speed );
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
     * Send a command to stop the robot motors
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
     * GETTERS
     *****************************************************
     */

//    /**
//     * Returns the controller for the robot. The controller helps the user
//     * to perform some basic commands. For more advanced solutions use the
//     * sendCommand method instead and create own commands
//     *
//     * @author Nicklas Gavelin
//     * @return The robot controller
//     */
//    public RobotController getController()
//    {
//        return this.controller;
//    }
    /**
     * Checks if a given Bluetooth address is a valid Sphero address or not.
     *
     * @param address The Bluetooth address
     *
     * @return True if valid, false otherwise
     */
    public static boolean isValidAddress( String address )
    {
        return (address.startsWith( ROBOT_ADDRESS_PREFIX ));
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
        return (device.getAddress().startsWith( ROBOT_ADDRESS_PREFIX ));
    }


    /**
     * Returns the robot unique id (identical to the Bluetooth address of the
     * device)
     *
     * @return The unique Bluetooth id
     */
    public String getId()
    {
        return this.bt.getAddress();//this.bt.getRemoteDevice().getBluetoothAddress();
    }


    /**
     * Returns the Bluetooth address of the robot.
     * Same as getId()
     *
     * @return The Bluetooth address of the robot
     */
    public String getAddress()
    {
        return this.bt.getAddress();//this.bt.getRemoteDevice().getBluetoothAddress();
    }


    /**
     * Returns the name of the robot
     *
     * @return The name of the robot
     */
    public String getName()
    {
        String n = this.bt.getName();
        if ( n == null )
            return this.name;
        return n;
    }


    /**
     * Returns this, used in threads to access the robot as you
     * can't use "this"
     *
     * @return The robot
     */
    private Robot getRobot()
    {
        return this;
    }


    /**
     * Returns the robot led
     *
     * @return The robot led
     */
    public RobotLED getLed()
    {
        return this.led;
    }


    /**
     * Returns the robot movement
     *
     * @return The robot movement
     */
    public RobotMovement getRobotMovement()
    {
        return this.movement;
    }


    /**
     * Returns the raw movements of the Sphero robot
     *
     * @return The raw movements of the robot
     */
    public RobotRawMovement getRobotRawMovement()
    {
        return this.rawMovement;
    }

    /*
     * *****************************************************
     * STREAM LISTENER/WRITER
     *****************************************************
     */
    /**
     * Handles the listening for the connected robot
     *
     * @author Nicklas Gavelin
     */
    private class RobotStreamListener extends Thread
    {
        // Thread stop/continue
        private boolean stop = false;
        // Bluetooth connection to use
        private BluetoothConnection btc;
        // Queue for commands that are waiting for responses
        private LinkedList<Pair<DeviceCommand, Boolean>> waitingForResponse;
        // Buffers
        private static final int BUFFER_SIZE = 256;


        /**
         * Create a listener from the Bluetooth connection
         *
         * @param btc The Bluetooth connection
         */
        public RobotStreamListener( BluetoothConnection btc )
        {
            this.btc = btc;
            this.waitingForResponse = new LinkedList<Pair<DeviceCommand, Boolean>>();
        }


        /**
         * Enqueue a command that are waiting for a response from the device
         *
         * @param cmd The pair of the command and the flag that tells if it's a
         * system command or not
         */
        protected void enqueue( Pair<DeviceCommand, Boolean> cmd )
        {
            this.waitingForResponse.add( cmd );
        }


        /**
         * Remove the last entry of the waiting for response queue
         *
         * @author Nicklas Gavelin
         */
        protected void removeLast()
        {
            this.waitingForResponse.removeLast();
        }


        /**
         * Stop the actively running thread
         */
        public void stopThread()
        {
            this.stop = true;
        }


        /**
         * Runs the listening of the socket
         */
        @Override
        public void run()
        {
            ByteArrayBuffer buf = new ByteArrayBuffer( BUFFER_SIZE );

            // Create a data array that contains all our read
            // data.
            byte[] data = new byte[ BUFFER_SIZE ];

            // Run until we manually stop the thread
            while ( !this.stop )
            {
                try
                {
                    int read = this.btc.read( data );
                    if ( read == -1 )
                        throw new IOException( "Reached end of stream" );
                    buf.append( data, 0, read );

//                    System.out.println("Read " + read);

                    // Read until we get the header
                    int dataLength = 0;
                    while ( buf.length() < DeviceResponse.RESPONSE_HEADER_LENGTH + dataLength ) // Header length + checksum length + data length
                    {
                        read = this.btc.read( data );
                        buf.append( data, 0, read );

                        if ( buf.length() > DeviceResponse.PACKET_LENGTH_INDEX ) // Set data length
                            dataLength = buf.toByteArray()[ DeviceResponse.PACKET_LENGTH_INDEX];
                    }

                    // Fetch all our data that we got in our buffer
                    byte[] newData = buf.toByteArray();

                    // Start reading messages from the buffer that we got
                    int read2 = 0;
                    for ( int pointer = 0;
                            pointer < buf.length()
                            && (newData.length - pointer >= DeviceResponse.RESPONSE_HEADER_LENGTH)
                            && (newData.length - pointer >= DeviceResponse.RESPONSE_HEADER_LENGTH + newData[pointer + DeviceResponse.PACKET_LENGTH_INDEX]); )
                    {
                        // Copy data
                        DeviceResponseHeader drh = new DeviceResponseHeader( newData, pointer );
                        Pair<DeviceCommand, Boolean> cmd = waitingForResponse.removeFirst();

                        // Update internal values if we got an OK from the robot
                        if ( drh.getResponseCode().equals( RESPONSE_CODE.CODE_OK ) )
                            updateInternalValues( cmd.getFirst() );

                        // Build our Device response
                        byte[] packetData = Arrays.copyOfRange( newData, pointer, pointer + drh.getPacketLength() + DeviceResponse.RESPONSE_HEADER_LENGTH );
                        DeviceResponse response = DeviceResponse.valueOf( cmd.getFirst(), packetData ); //, cmd.getFirst() );

                        Logging.debug( "Received " + response + (cmd.getSecond() ? " as a SYSTEM RESPONSE" : "") );

                        // Check if we got a system command or not
                        if ( cmd.getSecond() )
                        {
                            // System command
                            switch ( response.getCommand() )
                            {
                                case GET_BLUETOOTH_INFO:
                                    // Update Sphero name
                                    GetBluetoothInfoResponse gb = ( GetBluetoothInfoResponse ) response;
                                    if ( !gb.isDataCorrupt() )
                                        name = gb.getName();
                                    break;
                            }
                        }
                        else
                        {
                            // Ordinary command, notify listeners
                            notifyListenersDeviceResponse( response, cmd.getFirst() );
                        }

                        // Move our pointer forward
                        pointer += drh.getPacketLength() + DeviceResponse.RESPONSE_HEADER_LENGTH;
                        read2 = pointer;
                    }

                    // Remove all old data from the buffer
                    buf.clear();

                    // Check if we could handle all the messages that we had,
                    // if not add them to the next batch
                    if ( newData.length - read2 > 0 )
                        buf.append( newData, read2, newData.length - read2 );
                }
                catch ( NullPointerException e )
                {
                }
                catch ( NoSuchElementException e )
                {
                }
                catch ( Exception e )
                {
                    if ( connected )
                        Logging.fatal( "Listening thread closed down unexpectedly", e );
                    connectionClosedUnexpected();
                }
            }
        }
    }

    /**
     * Handles the sending of commands to the active robot.
     * Manages multiple queues (one timer and one sendingqueue). The
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
        private Writer w;
        private BlockingQueue<Pair<DeviceCommand, Boolean>> sendingQueue;


        /**
         * Create a robot stream writer for a specific bluetooth connection
         *
         * @param btc The bluetooth connection to send to
         */
        protected RobotSendingQueue( BluetoothConnection btc )
        {
            this.btc = btc;
            this.sendingQueue = new LinkedBlockingQueue<Pair<DeviceCommand, Boolean>>();
            this.w = new Writer();

            this.startWriter();
        }


        /**
         * Start the writer thread.
         * The writer will stop at the same time as the RobotSendinQueue is
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
        public void forceCommand( DeviceCommand command )
        {
            this.sendingQueue.add( new Pair<DeviceCommand, Boolean>( command, true ) );
        }


        /**
         * Enqueue a single command to be sent as soon as possible without using
         * the timer objects that are often used to enqueue commands to be sent
         * after a certain delay.
         *
         * @param command       The command to send
         * @param systemCommand True if the command is a system command, false
         * otherwise
         */
        public void enqueue( DeviceCommand command, boolean systemCommand )
        {
            try
            {
                if ( !this.stop && !this.stopAccepting )
                    this.sendingQueue.put( new Pair<DeviceCommand, Boolean>( command, systemCommand ) );
            }
            catch ( InterruptedException e )
            {
            }
        }


        /**
         * Enqueue a single command to be sent as soon as possible without using
         * the timer objects that are often used to enqueue commands to be sent
         * after a certain delay. The command will be sent as a SYSTEM command
         * and will not notify any RobotListeners after a response is received!
         *
         * @param command The command to send
         * @param delay   The delay to send the command after (in ms)
         */
        public void enqueue( DeviceCommand command, float delay )
        {
            this.enqueue( command, delay, false );
        }


        /**
         * Enqueue a command with a certain repeat period and initial delay
         * before sending the
         * first message. <b>The message will be repeated as long as the writer
         * allows it</b>.
         *
         * @param command      The command to transmit
         * @param initialDelay The initial delay before sending the first one
         * @param periodLength The period length between the transmissions
         */
        public void enqueue( DeviceCommand command, float initialDelay, float periodLength )
        {
            this.enqueue( command, false, initialDelay, periodLength );
        }


        /**
         * Enqueue a command with a certain repeat period and initial delay
         * before sending the
         * first message. <b>The message will be repeated as long as the writer
         * allows it</b>.
         *
         * @param command       The command to send
         * @param systemCommand True for a system command, false otherwise
         * @param initialDelay  The initial delay for sending
         * @param periodLength  The period length between transmissions
         */
        public void enqueue( DeviceCommand command, boolean systemCommand, float initialDelay, float periodLength )
        {
            if ( !this.stop && !this.stopAccepting )
                this.schedule( new CommandTask( new Pair<DeviceCommand, Boolean>( command, systemCommand ) ), ( long ) initialDelay, ( long ) periodLength );
        }


        /**
         * Enqueue an already existing command task to run at a certain initial
         * delay and
         * a certain period length
         *
         * @param task  The task to run after the timer runs
         * @param delay The delay before running the task in milliseconds
         */
        private void enqueue( CommandTask task, float delay )
        {
            if ( !this.stop && !this.stopAccepting )
                this.schedule( task, ( long ) delay );
        }


        /**
         * Enqueue a single command to be sent after a specific delay
         *
         * @param command       The command to send
         * @param delay         The delay to send after (in ms)
         * @param systemCommand True if the command is a system command, false
         * otherwise
         */
        public void enqueue( DeviceCommand command, float delay, boolean systemCommand )
        {
            if ( !this.stop && !this.stopAccepting )
                this.schedule( new CommandTask( new Pair<DeviceCommand, Boolean>( command, systemCommand ) ), ( long ) delay );
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
            private Pair<DeviceCommand, Boolean> execute;
            private int repeat = 0;
            private float delay;
            private boolean repeating = false;


            /**
             * Create a command task to send a command
             *
             * @param execute The command together with a boolean value
             * describing if it's a system message or not
             */
            private CommandTask( Pair<DeviceCommand, Boolean> execute )
            {
                this.execute = execute;
            }


            /**
             * Create a command task with a repeated number of runs
             *
             * @param execute The command to execute
             * @param delay   The delay between the commands
             * @param repeat  The number of repeats to perform (-1 for infinite
             * repeats)
             */
            private CommandTask( Pair<DeviceCommand, Boolean> execute, float delay, int repeat )
            {
                this( execute );
                this.repeat = repeat;
                this.delay = delay;

                if ( repeat != -1 ) // Infinite command, will be sent until the end of time!
                    this.repeating = true;
            }


            @Override
            public void run()
            {
                // Enqueue the command directly to the writer
                enqueue( execute.getFirst(), execute.getSecond() );

                // Check if we want to repeat the command
                if ( repeating )
                {
                    if ( repeat == -1 || --repeat > 0 )
                        enqueue( this, delay );

                }
            }
        }

        /**
         * Handles all writing to the robot
         */
        private class Writer extends Thread
        {
            @Override
            public void run()
            {
                // Holds number of added messages
                int added = 0;

                // Run until we manually stop the thread or
                // a connection error occurs.
                while ( !stop )
                {
                    try
                    {
                        // Try and fetch some message
                        Pair<DeviceCommand, Boolean> p = sendingQueue.take();
                        ByteArrayBuffer sendingBuffer = new ByteArrayBuffer( 256 );

                        // Append message to sending buffer
                        sendingBuffer.append( p.getFirst().getPacket(), 0, p.getFirst().getPacketLength() );

                        // Lock until we have sent our messages in-case someone
                        // else tries to do this at the same time
                        synchronized ( btc )
                        {
                            try
                            {
                                // Add command to listening queue
                                listeningThread.enqueue( p );
                                added++;

                                // Check if we can send more
                                if ( sendingQueue.size() > 0 )
                                {
                                    // Go through all the messages that we can
                                    for ( int i = 0; i < sendingQueue.size(); i++ )
                                    {
                                        // Peek at the the rest of the messages
                                        int length = sendingQueue.peek().getFirst().getPacketLength();

                                        // Check that we have enough space to add the next message to, if not
                                        // send what we got and continue later on
                                        if ( sendingBuffer.length() - length < 0 )
                                            break;

                                        // Enqueue the next command
                                        Pair<DeviceCommand, Boolean> c = sendingQueue.remove();
                                        listeningThread.enqueue( c );
                                        sendingBuffer.append( c.getFirst().getPacket(), 0, c.getFirst().getPacketLength() );

                                        // Increase number of added messages in case
                                        // we go haywire
                                        added++;
                                    }
                                }

                                // Write to socket
                                btc.write( sendingBuffer.toByteArray() );
                                btc.flush();

                                // Clear buffer
                                sendingBuffer.clear();
                                added = 0;
                            }
                            catch ( IOException e )
                            {
                                // Remove last until we have removed all messages that we tried to send
                                //for ( int i = 0; i < added; i++ ) // TODO: Remove this
                                //    listeningThread.removeLast();

                                // Close unexpectedly
                                if ( connected )
                                    Logging.fatal( "Writing thread closed down unexpectedly", e );
                                connectionClosedUnexpected();
                            }
                        }
                    }
                    catch ( InterruptedException e )
                    {
                    } // Nothing important, just continue on
                }
            }
        }
    }
}
