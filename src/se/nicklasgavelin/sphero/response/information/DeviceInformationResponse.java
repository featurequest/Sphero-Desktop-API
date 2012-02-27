/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.response.information;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class DeviceInformationResponse extends ResponseMessage
{
    /**
     * Response codes for the information response messages
     */
    public enum INFORMATION_RESPONSE_CODE
    {
        EMIT( 6 );
        private byte code;


        private INFORMATION_RESPONSE_CODE( int code )
        {
            this.code = ( byte ) code;
        }


        public byte getCode()
        {
            return this.code;
        }


        public static INFORMATION_RESPONSE_CODE valueOf( byte code )
        {
            INFORMATION_RESPONSE_CODE[] res = INFORMATION_RESPONSE_CODE.values();
            for ( INFORMATION_RESPONSE_CODE r : res )
                if ( r.getCode() == code )
                    return r;
            return null;
        }


        public static INFORMATION_RESPONSE_CODE valueOf( int code )
        {
            return INFORMATION_RESPONSE_CODE.valueOf( ( byte ) code );
        }
    }

    /* Internal response code */
    private INFORMATION_RESPONSE_CODE responseType;


    /**
     * Create an information response from received data
     *
     * @param data The recevied data
     */
    public DeviceInformationResponse( ResponseHeader rh )
    {
        super( rh );
        this.responseType = INFORMATION_RESPONSE_CODE.valueOf( rh.getPacketData().toByteArray()[ResponseMessage.RESPONSE_CODE_INDEX] );
    }

    /**
     * Fetch the information response from the data
     *
     * @param data The data to fetch for
     * @return The information response or null
     */
    public static DeviceInformationResponse valueOf( ResponseHeader rh )
    {
        return ( DeviceInformationResponse ) ResponseMessage.valueOf( null, rh );
    }
}
