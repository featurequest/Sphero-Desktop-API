/*
 * Please read the LICENSE file that is included with the source
 * code.
 */

package se.nicklasgavelin.sphero.response.information;

import se.nicklasgavelin.sphero.response.InformationResponseMessage;

/**
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class DataResponse extends InformationResponseMessage
{
    private byte[] data;

    /**
     * Response message for sensor data
     *
     * @param rh The response header
     */
    public DataResponse( ResponseHeader rh )
    {
        super( rh );

//        System.err.println( "DATA: " + rh.getPacketData() );

        // Internal data storage
        this.data = rh.getPacketData().toByteArray( InformationResponseMessage.INFORMATION_RESPONSE_HEADER_LENGTH, rh.getPacketLength() - 1 );
//        System.err.println( "CONTENT: " + Array.stringify( data ) );
    }


    /**
     * Returns the data received in the data message
     *
     * @return The data for the sensors
     */
    public byte[] getSensorData()
    {
        return this.data;
    }


    /**
     * Returns the length of the sensor data
     *
     * @return The sensor data length
     */
    public int getSensorDataLength()
    {
        return this.data.length;
    }
}
