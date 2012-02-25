package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.response.DeviceResponse.RESPONSE_CODE;
import se.nicklasgavelin.util.ByteArrayBuffer;

/**
 * Header for responses received from robot.
 * Used for deciding package length and content quickly without needing
 * the complete packet.
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class DeviceResponseHeader
{
    // Internal storage that are packet specific
    private RESPONSE_CODE code;
    private int sequenceNumber;
    private int packetLength;
    private ByteArrayBuffer data;
    private DeviceResponseHeader.HEADER_TYPE header;

    public static enum HEADER_TYPE
    {
        RESPONSE( -1, -1 ),
        INFORMATION( -1, -2 );
        private int a, b;

        private HEADER_TYPE( int a, int b )
        {
            this.a = a;
            this.b = b;
        }

        public String getValue()
        {
            return a + " " + b;
        }

        public int getValueB()
        {
            return b;
        }

        public static DeviceResponseHeader.HEADER_TYPE valueOf( int a, int b )
        {
            DeviceResponseHeader.HEADER_TYPE[] ht = DeviceResponseHeader.HEADER_TYPE.values();
            for ( int i = 0; i < ht.length; i++ )
                if ( ht[i].getValue().equals( a + " " + b ) )
                    return ht[i];
            return null;
        }
    }


    /**
     * Create a header from the data (may be only the header of the data or the
     * complete data array)
     *
     * @param data The header or the data
     */
    public DeviceResponseHeader( byte[] data )
    {
        this( data, 0 );
    }


    /**
     * Create a device response header from a data array and begin
     * at a specific offset in the array
     *
     * @param data   The data array with the header content
     * @param offset The offset to start reading the header from
     */
    public DeviceResponseHeader( byte[] data, int offset )
    {
        this.header = DeviceResponseHeader.HEADER_TYPE.valueOf( data[ DeviceResponse.INDEX_START_1 + offset], data[DeviceResponse.INDEX_START_2 + offset] );
        this.code = RESPONSE_CODE.valueOf( data[ DeviceResponse.RESPONSE_CODE_INDEX + offset] );
        this.sequenceNumber = data[ DeviceResponse.SEQUENCE_NUMBER_INDEX + offset];
        this.packetLength = data[ DeviceResponse.PACKET_LENGTH_INDEX + offset];
        this.data = new ByteArrayBuffer( this.packetLength + DeviceResponse.RESPONSE_HEADER_LENGTH );
        this.data.append( data, offset, this.packetLength + DeviceResponse.RESPONSE_HEADER_LENGTH );
    }


    /**
     * Returns the response code for the message
     *
     * @author Nicklas Gavelin
     * @return The response code for the message
     */
    public RESPONSE_CODE getResponseCode()
    {
        return this.code;
    }


    /**
     * Returns the sequence number of the message
     *
     * @return The sequence number of the message
     */
    public int getSequenceNumber()
    {
        return this.sequenceNumber;
    }


    /**
     * Returns the packet length of the message
     *
     * @return The packet length
     */
    public int getPacketLength()
    {
        return this.packetLength;
    }


    public DeviceResponseHeader.HEADER_TYPE getHeader()
    {
        return this.header;
    }

    public ByteArrayBuffer getData()
    {
        return this.data;
    }

    @Override
    public String toString()
    {
        return "< " + getClass().getCanonicalName() + " [ header type: " + this.header + ", data : " + this.data + ", response code: " + this.getResponseCode().toString() + ", sequence number: " + this.sequenceNumber + ", packet length: " + this.getPacketLength() + " ] >";
    }
}
