package se.nicklasgavelin.sphero.response;

import se.nicklasgavelin.sphero.response.DeviceResponse.RESPONSE_CODE;

/**
 * Header for responses received from robot.
 * Used for deciding package length and content quickly without needing
 * the complete packet.
 *
 * @author Nicklas Gavelin
 */
public class DeviceResponseHeader
{
    // Internal storage that are packet specific
    private RESPONSE_CODE code;
    private int sequenceNumber;
    private int packetLength;

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
     * @param data The data array with the header content
     * @param offset The offset to start reading the header from
     */
    public DeviceResponseHeader( byte[] data, int offset )
    {
        this.code = RESPONSE_CODE.valueOf( data[ DeviceResponse.RESPONSE_CODE_INDEX + offset] );
        this.sequenceNumber = data[ DeviceResponse.SEQUENCE_NUMBER_INDEX + offset];
        this.packetLength = data[ DeviceResponse.PACKET_LENGTH_INDEX + offset];
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

    @Override
    public String toString()
    {
        return "< " + getClass().getCanonicalName() + " [ response code: " + this.getResponseCode().toString() + ", sequence number: " + this.sequenceNumber + ", packet lenght: " + this.getPacketLength() + " ] >";
    }
}
