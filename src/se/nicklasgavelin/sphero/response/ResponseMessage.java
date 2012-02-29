/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.response;

import java.lang.reflect.Constructor;
import java.util.EnumMap;
import java.util.Map;
import se.nicklasgavelin.log.Logging;
import se.nicklasgavelin.sphero.command.CommandMessage;
import se.nicklasgavelin.sphero.response.information.DeviceInformationResponse;
import se.nicklasgavelin.util.ByteArrayBuffer;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class ResponseMessage
{
    /* Static values */
    public static final int INDEX_START_1 = 0,
            INDEX_START_2 = 1,
            RESPONSE_CODE_INDEX = 2,
            SEQUENCE_NUMBER_INDEX = 3,
            PACKET_LENGTH_INDEX = 4,
            RESPONSE_HEADER_LENGTH = 5;

    public static final int INFORMATION_RESPONSE_CODE_INDEX = 3,
                            INFORMATION_RESPONSE_HEADER_LENGTH = 5,
                            INFORMATION_PACKET_LENGTH_INDEX = PACKET_LENGTH_INDEX;

    /* Internal storage */
    private ResponseHeader drh;
    private boolean corrupt = false;


    /**
     * Create a response message from a response header
     *
     * @param _drh The response header
     */
    public ResponseMessage( ResponseHeader _drh )
    {
        this.drh = _drh;
        this.updateCorrupt();
    }


    /**
     * Update corrupt
     */
    private void updateCorrupt()
    {
        byte claimed = this.drh.getChecksum();
        int checksum = 0;
        byte[] data = this.drh.getPacketData().toByteArray();

        for( int i = 2; i < data.length - 1; i++ )
            checksum += data[ i ];
        checksum ^= 0xFFFFFFFF;

        this.setCorrupt( ((byte)checksum) != claimed );
    }

    /**
     * Returns the packet header
     *
     * @return The packet header
     */
    public ResponseHeader getHeader()
    {
        return this.drh;
    }


    public void setCorrupt( boolean _corrupt )
    {
        this.corrupt = _corrupt;
    }

    public boolean isCorrupt()
    {
        return this.corrupt;
    }


    public RESPONSE_CODE getResponseCode()
    {
        return this.drh.getResponseCode();
    }

    public ResponseHeader.RESPONSE_TYPE getResponseType()
    {
        return this.drh.getResponseType();
    }

    public byte[] getPacketData()
    {
        return this.getDataArray();
    }

    /**
     * Returns the packet data
     *
     * @return The packet data
     */
    public ByteArrayBuffer getData()
    {
        return this.drh.getPacketData();
    }


    /**
     * Returns the packet data
     *
     * @return The packet data
     */
    public byte[] getDataArray()
    {
        return this.getData().toByteArray();
    }

    @Override
    public String toString()
    {
        return "{ " + getClass().getCanonicalName() + " [ Code: " + this.getResponseCode() + ", Type: " + this.getResponseType() + " ] }";
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
    public static ResponseMessage valueOf( CommandMessage dc, ResponseHeader rh )
    {
        // Fetch packet data
        byte[] data = rh.getPacketData().toByteArray();

        // Switch between the different message types
        switch ( rh.getResponseType() )
        {
            // Information message that we received without doing any command message
            case INFORMATION:
                try
                {
                    Logging.debug( "Creating information packet from recevied data" );

                    // Continue with information
                    DeviceInformationResponse.INFORMATION_RESPONSE_CODE ir = DeviceInformationResponse.INFORMATION_RESPONSE_CODE.valueOf( data[ResponseMessage.RESPONSE_CODE_INDEX] );

                    // Create our class name for the message
                    String className = ir.name().toLowerCase();
                    className = className.substring( 0, 1 ).toUpperCase() + className.substring( 1 );

                    Logging.debug( "Parsed received data as a " + className + " information response" );

                    // Construct our new response message
                    @SuppressWarnings( "unchecked" )
                    Constructor<DeviceInformationResponse> cons = ( Constructor<DeviceInformationResponse> ) Class.forName(
                                                                        DeviceInformationResponse.class.getCanonicalName().replace( "DeviceInformation", className ) )
                                                                        .getConstructor( ResponseHeader.class );/*new Class[]
                            {
                                (new byte[]
                                    {
                                    }).getClass()
                            } );*/

                    ResponseMessage dim = ( ResponseMessage ) cons.newInstance( rh );
                    Logging.debug( "Successfully created information response message " + dim );

                    return dim;
                }
                catch ( Exception ex )
                {
                    ex.printStackTrace();
                    Logging.error( "Failed to create information response packet from received data ", ex );
                }
                break;

            // Message received in return for a command message being sent
            case RESPONSE:
                if ( dc == null )
                    return null;

                // Fetch our message name
                String[] c = dc.getClass().getCanonicalName().split( "\\." );
                String cls = c[ c.length - 1];

                // Fetch prefix name
                String name = cls.split( "Command" )[0];

                try
                {
                    Logging.debug( "Creating response packet from received data" );

                    // Create the new instance
                    @SuppressWarnings( "unchecked" )
                    Constructor<ResponseMessage> cons = ( Constructor<ResponseMessage> ) Class.forName(
                                                            ResponseMessage.class.getCanonicalName().replace( "ResponseMessage", name + "Response" ) )
                                                            .getConstructor( ResponseHeader.class );/*new Class[]
                            {
                                (new byte[]
                                    {
                                    }).getClass()
                            } );*/

                    // Return our created message
                    return ( ResponseMessage ) cons.newInstance( rh );
                }
                catch ( Exception e )
                {
                    Logging.error( "Failed to create response packet from received data", e );
                }
                break;
        }

        return null;
    }

    /* *******************
     * INNER CLASSES
     ******************* */
    public static class ResponseHeader
    {
        /* Code for the response */
        private RESPONSE_CODE code;

        /* Packet information */
        private int seqNum, dataLength;
        private byte checksum;
        private ByteArrayBuffer data;

        /* Type of the response */
        private RESPONSE_TYPE type;


        /**
         * Create a packet response header
         *
         * @param data The data for the packet
         */
        public ResponseHeader( byte[] data )
        {
            this( data, 0 );
        }

        /**
         * Create a packet response header
         *
         * @param _data The data for the packet
         * @param offset The offset to read the packet from
         */
        public ResponseHeader( byte[] _data, int offset )
        {
            // Packet information
            this.type           = RESPONSE_TYPE.valueOf( _data[ INDEX_START_1 + offset], _data[ INDEX_START_2 + offset] );
            int respCodeIndex = RESPONSE_CODE_INDEX, packetLengthIndex = PACKET_LENGTH_INDEX, respHeaderLength = RESPONSE_HEADER_LENGTH;

            switch( type )
            {
                /* Information response messages */
                case INFORMATION:
                    respCodeIndex       = INFORMATION_RESPONSE_CODE_INDEX;
                    packetLengthIndex   = INFORMATION_PACKET_LENGTH_INDEX;
                    respHeaderLength    = INFORMATION_RESPONSE_HEADER_LENGTH;

                    // Information response messages have no sequence number
                    this.seqNum         = -1;
                break;

                /* Regular response messages */
                case RESPONSE:
                    this.seqNum         = _data[ SEQUENCE_NUMBER_INDEX + offset];
                break;
            }

            // Set internal stuff
            this.code           = RESPONSE_CODE.valueOf( _data[ respCodeIndex + offset], this.type );
            this.dataLength     = _data[ packetLengthIndex + offset];
            int packetLength = (this.dataLength + respHeaderLength);
            this.checksum       = _data[ offset + (packetLength - 1) ];

//            System.err.println( "Data length: " + this.dataLength + ", Packet length: " + packetLength + ", Code: " + this.code + ", Checksum: " + this.checksum );

            // Data storage
            this.data           = new ByteArrayBuffer( packetLength );
            this.data.append( _data, offset, packetLength );
        }


        /**
         * Returns the response code for the packet
         *
         * @return The response code
         */
        public RESPONSE_CODE getResponseCode()
        {
            return this.code;
        }


        /**
         * Returns the checksum for the packet data
         *
         * @return The checksum for the packet data
         */
        public byte getChecksum()
        {
            return this.checksum;
        }

        /**
         * Returns the packet data
         *
         * @return The packet data
         */
        public ByteArrayBuffer getPacketData()
        {
            return this.data;
        }


        /**
         * Returns the sequence number for the packet
         *
         * @return The sequence number
         */
        public int getSequenceNumber()
        {
            return this.seqNum;
        }


        /**
         * Returns the length of the packet (WITHOUT THE HEADER!)
         *
         * @return The length of the packet
         */
        public int getPacketLength()
        {
            return this.dataLength;
        }


        /**
         * Returns the response type
         *
         * @return The response type
         */
        public RESPONSE_TYPE getResponseType()
        {
            return this.type;
        }

        @Override
        public String toString()
        {
            return "ResponseHeader [ ResponseCode: " + this.getResponseCode() + ", ResponseType: " + this.getResponseType() + " ]";
        }

        /* *************
         * INNER CLASSES
         */
        public static enum RESPONSE_TYPE
        {
            /* Device response headers */
            RESPONSE( -1, -1 ),
            INFORMATION( -1, -2 );

            /* Internal storage */
            private byte first, second;


            /**
             * Create a response header with first header value i and second j
             *
             * @param i First response header value
             * @param j Second response header value
             */
            private RESPONSE_TYPE( int i, int j )
            {
                this.first = ( byte ) i;
                this.second = ( byte ) j;
            }


            /**
             * Returns the internal response header values
             *
             * @return The response header values
             */
            public byte[] getHeaderType()
            {
                return new byte[]
                        {
                            this.first, this.second
                        };
            }


            /**
             * Returns a response header object that is represented by i and j.
             * Will return null if no header could be created from the two given
             * values.
             *
             * @param i The first header value
             * @param j The second header value
             *
             * @return a response header object or null if no object could be
             * created from the two given values
             */
            public static RESPONSE_TYPE valueOf( byte i, byte j )
            {
                RESPONSE_TYPE[] res = RESPONSE_TYPE.values();
                for ( RESPONSE_TYPE r : res )
                    if ( r.getHeaderType()[0] == i && r.getHeaderType()[1] == j )
                        return r;
                return null;
            }


            /**
             * Returns a response header object that is represented by i and j.
             * Will return null if no header could be created from the two given
             * values.
             *
             * @param i The first header value
             * @param j The second header value
             *
             * @return a response header object or null if no object could be
             * created from the two given values
             */
            public static RESPONSE_TYPE valueOf( int i, int j )
            {
                return RESPONSE_TYPE.valueOf( ( byte ) i, ( byte ) j );
            }
        }
    }

    /**
     * Response codes that are available
     *
     * @author Nicklas Gavelin
     */
    public static enum RESPONSE_CODE
    {
        /* Regular response codes */
        CODE_OK( new int[]{0, 0}, new ResponseHeader.RESPONSE_TYPE[]{ ResponseHeader.RESPONSE_TYPE.RESPONSE, ResponseHeader.RESPONSE_TYPE.INFORMATION } ),
        CODE_ERROR_GENERAL( new int[]{1}, new ResponseHeader.RESPONSE_TYPE[]{ResponseHeader.RESPONSE_TYPE.RESPONSE} ),
        CODE_ERROR_CHECKSUM( new int[]{2 }, new ResponseHeader.RESPONSE_TYPE[]{ResponseHeader.RESPONSE_TYPE.RESPONSE }  ),
        CODE_ERROR_FRAGMENT( new int[]{3 }, new ResponseHeader.RESPONSE_TYPE[]{ResponseHeader.RESPONSE_TYPE.RESPONSE } ),
        CODE_ERROR_BAD_COMMAND( new int[]{4 }, new ResponseHeader.RESPONSE_TYPE[]{ResponseHeader.RESPONSE_TYPE.RESPONSE } ),
        CODE_ERROR_UNSUPPORTED( new int[]{5 }, new ResponseHeader.RESPONSE_TYPE[]{ResponseHeader.RESPONSE_TYPE.RESPONSE } ),
        CODE_ERROR_BAD_MESSAGE( new int[]{6 }, new ResponseHeader.RESPONSE_TYPE[]{ResponseHeader.RESPONSE_TYPE.RESPONSE } ),
        CODE_ERROR_PARAMETER( new int[]{7 }, new ResponseHeader.RESPONSE_TYPE[]{ResponseHeader.RESPONSE_TYPE.RESPONSE } ),
        CODE_ERROR_EXECUTE( new int[]{8 }, new ResponseHeader.RESPONSE_TYPE[]{ResponseHeader.RESPONSE_TYPE.RESPONSE } ),
        CODE_ERROR_MAIN_APP_CORRUPT( new int[]{52 }, new ResponseHeader.RESPONSE_TYPE[]{ResponseHeader.RESPONSE_TYPE.RESPONSE } ),
        CODE_ERROR_TIME_OUT( new int[]{-1 }, new ResponseHeader.RESPONSE_TYPE[]{ResponseHeader.RESPONSE_TYPE.RESPONSE } ),
        CODE_ERROR_UNKNOWN( new int[]{53 }, new ResponseHeader.RESPONSE_TYPE[]{ResponseHeader.RESPONSE_TYPE.RESPONSE } ),
        UNKNOWN_RESPONSE_CODE( new int[]{-2 }, new ResponseHeader.RESPONSE_TYPE[]{ResponseHeader.RESPONSE_TYPE.RESPONSE } );


        private Map<ResponseHeader.RESPONSE_TYPE, Byte> codes;


        /**
         * Create a response code
         *
         * @param code The response code id
         */
        private RESPONSE_CODE( int[] _codes, ResponseHeader.RESPONSE_TYPE[] types )
        {
            this.codes = new EnumMap<ResponseHeader.RESPONSE_TYPE, Byte>( ResponseHeader.RESPONSE_TYPE.class );

            for( int i = 0; i < types.length; i++ )
                this.codes.put( types[i], (byte)_codes[i] );
        }

        private RESPONSE_CODE( byte[] _codes, ResponseHeader.RESPONSE_TYPE[] types )
        {
            this.codes = new EnumMap<ResponseHeader.RESPONSE_TYPE, Byte>( ResponseHeader.RESPONSE_TYPE.class );

            for( int i = 0; i < types.length; i++ )
                this.codes.put( types[i], _codes[i] );
        }

//        /**
//         * Returns the code id
//         *
//         * @return The response code id
//         */
//        public byte getValues()
//        {
//            return this.code;
//        }


        /**
         * Returns the internal code value for the given response type
         *
         * @param type The response type
         * @return The code value for the response type or null if no code could be found
         */
        public Byte getCode( ResponseHeader.RESPONSE_TYPE type )
        {
            return (Byte)this.codes.get( type );
        }


        /**
         * Returns the type for the given code
         *
         * @param code The code to fetch the type for
         * @return The type that is represented by the code or null if no type could be found
         */
        public ResponseHeader.RESPONSE_TYPE getType( byte code )
        {
            for( ResponseHeader.RESPONSE_TYPE t : this.codes.keySet() )
                if( ((Byte)this.codes.get( t )).equals( code ) )
                    return t;

            return null;
        }

        /**
         * Return the ENUM representation of the code value
         *
         * @param code   The code
         * @param header The header type
         *
         * @return The response code represented by the code
         */
        public static RESPONSE_CODE valueOf( int code, ResponseHeader.RESPONSE_TYPE type )
        {
            RESPONSE_CODE[] cmds = RESPONSE_CODE.values();
            for( RESPONSE_CODE rc : cmds )
            {
                Byte c = rc.getCode( type );
                if( c != null && c.intValue() == code )
                    return rc;
            }

            return null;
        }


        /**
         * Return the enum representation of the code value
         *
         * @param code   The code
         * @param header The header type
         *
         * @return The response code represented by the code
         */
        public static RESPONSE_CODE valueOf( byte code, ResponseHeader.RESPONSE_TYPE type )
        {
            return RESPONSE_CODE.valueOf( ( int ) code, type );
        }
    }
}
