package site.nicklas.sphero.command;

/**
 * The base class for the device commands that may be sent to
 * the Sphero. Contains the information about the command and
 * also the data.
 *
 * @author Nicklas Gavelin
 *
 */
public abstract class DeviceCommand extends DeviceMessage
{
    public enum DEVICE_COMMAND
    {
        // Core commands
        PING( 0, 0 ),
        VERSIONING( 2, 0 ),
        SET_BLUETOOTH_NAME( 16, 0 ),
        GET_BLUETOOTH_INFO( 17, 0 ),
        GO_TO_SLEEP( 34, 0 ),
        JUMP_TO_BOOTLOADER( 48, 0 ),
        LEVEL_1_DIAGNOSTICS( 64, 0 ),
        // Bootloader command
        JUMP_TO_MAIN( 4, 1 ),
        // Sphero command
        CALIBRATE( 1, 2 ),
        STABILIZATION( 2, 2 ),
        ROTATION_RATE( 3, 2 ),
        RGB_LED_OUTPUT( 32, 2 ),
        FRONT_LED_OUTPUT( 33, 2 ),
        ROLL( 48, 2 ),
        BOOST( 49, 2 ),
        RAW_MOTOR( 51, 2 ),
        GET_CONFIGURATION_BLOCK( 64, 2 ),
        RUN_MACRO( 80, 2 ),
        MACRO( 81, 2 ),
        SAVE_MACRO( 82, 2 ),
        ABORT_MACRO( 85, 2 ),
        SET_DATA_STREAMING( 17, 2 ),
        SPIN_LEFT( RAW_MOTOR.getCommandId(), RAW_MOTOR.getDeviceId() ),
        SPIN_RIGHT( RAW_MOTOR.getCommandId(), RAW_MOTOR.getDeviceId() ),
        CUSTOM_PING( FRONT_LED_OUTPUT.getCommandId(), FRONT_LED_OUTPUT.getDeviceId() );
        // Internal storage
        private static int idCount = 0;
        private byte commandId;
        private byte deviceId;
        private int id;


        /**
         * Create device command with a device id and command id
         *
         * @param commandId The command id
         * @param deviceId  The device id
         */
        private DEVICE_COMMAND( int commandId, int deviceId )
        {
            this.commandId = ( byte ) commandId;
            this.deviceId = ( byte ) deviceId;

            this.setId();
        }


        /**
         * Set the id of the command
         */
        private void setId()
        {
            this.id = DEVICE_COMMAND.idCount++;
        }


        /**
         * Returns the device id
         *
         * @return The device id
         */
        public byte getDeviceId()
        {
            return this.deviceId;
        }


        /**
         * Returns the command id
         *
         * @return The command id
         */
        public byte getCommandId()
        {
            return this.commandId;
        }


        /**
         * Returns the unique id for the command
         *
         * @return The unique id
         */
        public int getId()
        {
            return this.id;
        }


        /**
         * Returns the device command that corresponds to the given command and
         * device id.
         *
         * @param uniqueId The unique command id
         *
         * @return The device command or null if no command could be represented
         */
        public static DEVICE_COMMAND valueOf( int uniqueId ) // int commandId, int deviceId )
        {
            switch ( uniqueId )
            {
                case 0:
                    return PING;
                case 1:
                    return VERSIONING;
                case 2:
                    return SET_BLUETOOTH_NAME;
                case 3:
                    return GET_BLUETOOTH_INFO;
                case 4:
                    return GO_TO_SLEEP;
                case 5:
                    return JUMP_TO_BOOTLOADER;
                case 6:
                    return LEVEL_1_DIAGNOSTICS;
                case 7:
                    return JUMP_TO_MAIN;
                case 8:
                    return CALIBRATE;
                case 9:
                    return STABILIZATION;
                case 10:
                    return ROTATION_RATE;
                case 11:
                    return RGB_LED_OUTPUT;
                case 12:
                    return FRONT_LED_OUTPUT;
                case 13:
                    return ROLL;
                case 14:
                    return BOOST;
                case 15:
                    return RAW_MOTOR;
                case 16:
                    return GET_CONFIGURATION_BLOCK;
                case 17:
                    return RUN_MACRO;
                case 18:
                    return MACRO;
                case 19:
                    return SAVE_MACRO;
                case 20:
                    return ABORT_MACRO;
                case 21:
                    return SET_DATA_STREAMING;
                case 22:
                    return SPIN_LEFT;
                case 23:
                    return SPIN_RIGHT;
                case 24:
                    return CUSTOM_PING;
            }

            return null;
        }
    }
    // Internal storage
    private static final byte COMMAND_PREFIX = -1;
    private static final int CHECKSUM_LENGTH = 1;
    private static final int INDEX_START_1 = 0;
    private static final int INDEX_START_2 = 1;
    private static final int INDEX_DEVICE_ID = 2;
    private static final int INDEX_COMMAND = 3;
    protected static final int INDEX_COMMAND_SEQUENCE_NO = 4;
    private static final int INDEX_COMMAND_DATA_LENGTH = 5;
    private static final int COMMAND_HEADER_LENGTH = 6;
    private static int sequenceNumber = 0;
    private DEVICE_COMMAND command;
    private byte[] packet = null;


    /**
     * Create a device command for a given command
     *
     * @param command The command
     */
    protected DeviceCommand( DEVICE_COMMAND command )
    {
        super();
        this.command = command;
    }


    /**
     * Returns the device command
     *
     * @return The device command
     */
    public DEVICE_COMMAND getCommand()
    {
        return this.command;
    }


    /**
     * Returns the packet data as a byte array
     *
     * @return The packet data as a byte array
     */
    protected byte[] getPacketData()
    {
        return null;
    }


    /**
     * Create a byte array of the message content
     *
     * @author Orbotix
     * @return The message as a byte array
     */
    protected byte[] packetize()
    {
        byte[] data = getPacketData();

        int data_length = data != null ? data.length : 0;
        int packet_length = data_length + COMMAND_HEADER_LENGTH + CHECKSUM_LENGTH;
        byte[] buffer = new byte[ packet_length ];
        byte checksum = 0;

        buffer[ INDEX_START_1] = COMMAND_PREFIX;
        buffer[ INDEX_START_2] = COMMAND_PREFIX;

        byte device_id = this.command.getDeviceId();
        checksum = ( byte ) (checksum + device_id);
        buffer[ INDEX_DEVICE_ID] = device_id;

        byte cmd = this.command.getCommandId();//getCommandId();
        checksum = ( byte ) (checksum + cmd);
        buffer[ INDEX_COMMAND] = cmd;

        checksum = ( byte ) (checksum + sequenceNumber);
        buffer[ INDEX_COMMAND_SEQUENCE_NO] = ( byte ) (sequenceNumber++);

        byte response_length = getCommandLength( data_length );
        checksum = ( byte ) (checksum + response_length);
        buffer[ INDEX_COMMAND_DATA_LENGTH] = response_length;

        if ( data != null )
        {
            for (int i = 0; i < data_length; i++)
            {
                buffer[(i + COMMAND_HEADER_LENGTH)] = data[i];
                checksum = ( byte ) (checksum + data[i]);
            }
        }

        buffer[(packet_length - CHECKSUM_LENGTH)] = ( byte ) (checksum ^ 0xFFFFFFFF);
        return buffer;
    }


    /**
     * Returns the packet as a byte array
     *
     * @return The packet as a byte array
     */
    public byte[] getPacket()
    {
        if ( this.packet == null )
            this.packet = packetize();
        return this.packet;
    }


    /**
     * Returns the length of the command
     *
     * @param dataLength The length of the data
     *
     * @return The length of the command
     */
    public byte getCommandLength( int dataLength )
    {
        return ( byte ) (dataLength + 1);
    }


    /**
     * Returns the complete packet length including header, checksum and data
     * length
     *
     * @return The packet length
     */
    public int getPacketLength()
    {
        return (this.getPacket().length);
    }


    @Override
    public String toString()
    {
        return "[ DeviceCommand: " + getCommand() + ", " + getClass().getCanonicalName() + " ]";
    }
}