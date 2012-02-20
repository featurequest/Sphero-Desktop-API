package site.nicklas.sphero.response;

import java.lang.reflect.Constructor;
import site.nicklas.sphero.command.DeviceCommand;
import site.nicklas.sphero.command.DeviceCommand.DEVICE_COMMAND;
import site.nicklas.sphero.command.DeviceMessage;

/**
 * Class for responses received from Sphero.
 * Responses are created automatically when messages are received from
 * the Sphero robots. Should NOT be created manually at all!
 *
 * @author Nicklas Gavelin
 */
public abstract class DeviceResponse extends DeviceMessage
{
    // Indexes
    public static final int INDEX_START_1 = 0,
            INDEX_START_2 = 1,
            RESPONSE_CODE_INDEX = 2,
            SEQUENCE_NUMBER_INDEX = 3,
            PACKET_LENGTH_INDEX = 4,
            RESPONSE_HEADER_LENGTH = 5;

    /**
     * Response codes that are available
     *
     * @author Nicklas Gavelin
     */
    public enum RESPONSE_CODE
    {
        CODE_OK( 0 ),
        CODE_ERROR_GENERAL( 1 ),
        CODE_ERROR_CHECKSUM( 2 ),
        CODE_ERROR_FRAGMENT( 3 ),
        CODE_ERROR_BAD_COMMAND( 4 ),
        CODE_ERROR_UNSUPPORTED( 5 ),
        CODE_ERROR_BAD_MESSAGE( 6 ),
        CODE_ERROR_PARAMETER( 7 ),
        CODE_ERROR_EXECUTE( 8 ),
        CODE_ERROR_MAIN_APP_CORRUPT( 52 ),
        CODE_ERROR_TIME_OUT( -1 );
        private byte code;


        /**
         * Create a response code
         *
         * @param code The response code id
         */
        private RESPONSE_CODE( int code )
        {
            this.code = ( byte ) code;
        }


        /**
         * Returns the code id
         *
         * @author Nicklas Gavelin
         * @return The response code id
         */
        public byte getValue()
        {
            return this.code;
        }


        /**
         * Return the enum representation of the code value
         *
         * @author Nicklas Gavelin
         * @param code The code
         *
         * @return The response code represented by the code
         */
        public static RESPONSE_CODE valueOf( int code )
        {
            switch ( code )
            {
                case 0:
                    return CODE_OK;
                case 1:
                    return CODE_ERROR_GENERAL;
                case 2:
                    return CODE_ERROR_CHECKSUM;
                case 3:
                    return CODE_ERROR_FRAGMENT;
                case 4:
                    return CODE_ERROR_BAD_COMMAND;
                case 5:
                    return CODE_ERROR_UNSUPPORTED;
                case 6:
                    return CODE_ERROR_BAD_MESSAGE;
                case 7:
                    return CODE_ERROR_PARAMETER;
                case 8:
                    return CODE_ERROR_EXECUTE;
                case 52:
                    return CODE_ERROR_MAIN_APP_CORRUPT;
                case -1:
                    return CODE_ERROR_TIME_OUT;
            }

            return null;
        }


        /**
         * Return the enum representation of the code value
         *
         * @author Nicklas Gavelin
         * @param code The code
         *
         * @return The response code represented by the code
         */
        public static RESPONSE_CODE valueOf( byte code )
        {
            return RESPONSE_CODE.valueOf( ( int ) code );
        }
    }
    // Code for the received message
    private DEVICE_COMMAND command;
    private DeviceResponseHeader drh;
    // Data in response
    private final byte[] data;
    private boolean setCorrupt = false;


    /**
     * Create a device response with a given command id, device id and code
     *
     * @param commandUniqueId The unique command id
     * @param data            The data for the response
     */
    protected DeviceResponse( int commandUniqueId, byte[] data )//byte commandId, byte deviceId, byte[] data ) 
    {
        super();

        // Store everything
        this.command = DEVICE_COMMAND.valueOf( commandUniqueId ); //commandId, deviceId );
        this.data = data;
        this.drh = new DeviceResponseHeader( data );
    }


    /**
     * Create a device response from a given command and its data
     *
     * @param command The command
     * @param data    The data received
     */
    protected DeviceResponse( DEVICE_COMMAND command, byte[] data )
    {
        this( command.getId(), data );
        this.drh = new DeviceResponseHeader( data );
    }


    /**
     * Get the device response from a given command and received data
     *
     * @param dc   The device command to receive response for
     * @param data The data received
     *
     * @return The device response or null if no device response could be
     * created
     */
    public static DeviceResponse valueOf( DeviceCommand dc, byte[] data )
    {
        String[] c = dc.getClass().getCanonicalName().split( "\\." );
        String cls = c[ c.length - 1];

        // Fetch prefix name
        String name = cls.split( "Command" )[0];

        // Create the new instance
        try
        {
            @SuppressWarnings( "unchecked" )
            Constructor<DeviceResponse> cons = ( Constructor<DeviceResponse> ) Class.forName( DeviceResponse.class.getCanonicalName().replace( "DeviceResponse", name + "Response" ) ).getConstructor( new Class[]
                    {
                        (new byte[]
                         {
                         }).getClass()
                    } );
            return ( DeviceResponse ) cons.newInstance( data );
        }
        catch ( Exception e )
        {
        }

        return null;
    }


    /**
     * Returns the command for the message
     *
     * @return The device command
     */
    public DEVICE_COMMAND getCommand()
    {
        return this.command;
    }


    /**
     * Returns the response code for the message
     *
     * @return The response code
     */
    public RESPONSE_CODE getCode()
    {
        return this.drh.getResponseCode();
    }


    /**
     * Returns the sequence number of the message
     *
     * @return The sequence number
     */
    public int getSequenceNumber()
    {
        return this.drh.getSequenceNumber();
    }


    /**
     * Returns the packet length
     *
     * @return The packet length
     */
    public int getPacketLength()
    {
        return this.drh.getPacketLength();
    }


    /**
     * Calculates the checksum and controls it against the message checksum
     *
     * @param data The whole message data
     *
     * @return True if the packet is corrupt, false otherwise
     */
    public static boolean calculateIsCorrupt( byte[] data )
    {
        byte checksum = 0;
        checksum += data[ RESPONSE_CODE_INDEX];
        checksum += data[ SEQUENCE_NUMBER_INDEX];
        checksum += data[ PACKET_LENGTH_INDEX];

        for (int i = PACKET_LENGTH_INDEX + 1; i < data.length - 1; i++)
            checksum += data[ i];

        checksum = ( byte ) (checksum ^ 0xFFFFFFFF);

        return (checksum != data[ data.length - 1]);
    }


    /**
     * Set data to be corrupt or not
     *
     * @param corrupt True for corrupt data, false otherwise
     */
    protected void setCorrupt( boolean corrupt )
    {
        this.setCorrupt = corrupt;
    }


    /**
     * Returns true if the received data is corrupt, false otherwise
     *
     * @return True if received data is corrupt, false otherwise
     */
    public boolean isDataCorrupt()
    {
        if ( !this.setCorrupt )
            return DeviceResponse.calculateIsCorrupt( this.data );
        return true;
    }


    @Override
    public String toString()
    {
        return "[ DeviceResponse: " + this.getCode() + ", " + getClass().getCanonicalName() + " ]";
    }
}
