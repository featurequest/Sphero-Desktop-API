/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.command;

import java.util.Date;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class CommandMessage
{
    /* Static values */
    private static int nSeq = 0;

    /* Static indicies */
    private static final byte COMMAND_PREFIX = -1;
    private static final int CHECKSUM_LENGTH = 1,
            INDEX_START_1 = 0,
            INDEX_START_2 = 1,
            INDEX_DEVICE_ID = 2,
            INDEX_COMMAND = 3,
            INDEX_COMMAND_SEQUENCE_NO = 4,
            INDEX_COMMAND_DATA_LENGTH = 5, COMMAND_HEADER_LENGTH = 6;

    /* Internal storage */
    private Date timestamp;
    private boolean seqSet = false;
    private int seqNum;
    private COMMAND_MESSAGE_TYPE command;
    private byte[] packet;


    public CommandMessage( COMMAND_MESSAGE_TYPE _command )
    {
        this.command = _command;
    }


    /**
     * Returns the internal sequence number
     * Will also set the sequence number if it's not set
     *
     * @return The internal sequence number
     */
    public int getSequenceNumber()
    {
        if ( !this.seqSet )
        {
            this.seqNum = CommandMessage.nSeq++;
            this.seqSet = true;
        }

        return this.seqNum;
    }


    /**
     * Returns the command type
     *
     * @return The command type
     */
    public COMMAND_MESSAGE_TYPE getCommand()
    {
        return this.command;
    }


    /**
     * Returns the packet data.
     * WILL SET THE SEQUENCE NUMBER DURING THE FIRST CALL!
     *
     * @return The packet data
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
     * Returns the packet data
     *
     * @return Packet data
     */
    protected byte[] getPacketData()
    {
        return null;
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


    /**
     * Create the packet content
     *
     * @return The packet content
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

        byte cmd = this.command.getCommandId();
        checksum = ( byte ) (checksum + cmd);
        buffer[ INDEX_COMMAND] = cmd;

        int sequenceNumber = this.getSequenceNumber();
        checksum = ( byte ) (checksum + sequenceNumber);
        buffer[ INDEX_COMMAND_SEQUENCE_NO] = ( byte ) (sequenceNumber);

        byte response_length = getCommandLength( data_length );
        checksum = ( byte ) (checksum + response_length);
        buffer[ INDEX_COMMAND_DATA_LENGTH] = response_length;

        // Check if we need to calculate the checksum for the data we have added
        if ( data != null )
        {
            // Calculate the checksum for the data (also add the data to the array)
            for ( int i = 0; i < data_length; i++ )
            {
                buffer[(i + COMMAND_HEADER_LENGTH)] = data[i];
                checksum = ( byte ) (checksum + data[i]);
            }
        }

        buffer[(packet_length - CHECKSUM_LENGTH)] = ( byte ) (checksum ^ 0xFFFFFFFF);

        return buffer;
    }


    /**
     * Returns the timestamp when the message was made into a packet
     *
     * @return The timestamp when the message was packetized
     */
    public Date getTimestamp()
    {
        return this.timestamp;
    }

    /* *****************************
     * INTERNAL CLASSES
     ***************************** */
    /**
     * Different command types
     */
    public static enum COMMAND_MESSAGE_TYPE
    {
        /* Core commands */
        PING( 0, 0 ),
        VERSIONING( 2, 0 ),
        SET_BLUETOOTH_NAME( 16, 0 ),
        GET_BLUETOOTH_INFO( 17, 0 ),
        GO_TO_SLEEP( 34, 0 ),
        JUMP_TO_BOOTLOADER( 48, 0 ),
        LEVEL_1_DIAGNOSTICS( 64, 0 ),
        /* Bootloader command */
        JUMP_TO_MAIN( 4, 1 ),
        /* Sphero command */
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

        /* Internal storage */
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
        private COMMAND_MESSAGE_TYPE( int commandId, int deviceId )
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
            this.id = COMMAND_MESSAGE_TYPE.idCount++;
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
        public static COMMAND_MESSAGE_TYPE valueOf( int uniqueId ) // int commandId, int deviceId )
        {
            COMMAND_MESSAGE_TYPE[] cmds = COMMAND_MESSAGE_TYPE.values();
            for ( COMMAND_MESSAGE_TYPE dc : cmds )
                if ( dc.getId() == uniqueId )
                    return dc;
            return null;
        }
    }


}
