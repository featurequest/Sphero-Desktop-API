package site.nicklas.sphero.response;

import java.io.UnsupportedEncodingException;
import site.nicklas.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * A response for the GetBluetoothInfoCommand. Will be created
 * when a response for the command is received.
 *
 * @author Orbotix
 * @author Nicklas Gavelin
 */
public class GetBluetoothInfoResponse extends DeviceResponse
{
    private static final int INFO_DATA_LENGTH = 64,
            OLD_INFO_DATA_LENGTH = 32,
            INFO_NAME_LENGTH = 48,
            OLD_INFO_NAME_LENGTH = 16,
            INFO_ADDRESS_LENGTH = 16;
    
    private String name, address;


    /**
     * Create the Bluetooth info response from the received data
     *
     * @param data The received data
     */
    public GetBluetoothInfoResponse( byte[] data )
    {
        super( DEVICE_COMMAND.GET_BLUETOOTH_INFO, data );

        if ( !isDataCorrupt() )
        {
            if ( data == null || (data.length != INFO_DATA_LENGTH && data.length != OLD_INFO_DATA_LENGTH) )
                setCorrupt( true );
            else
            {
                // Data is ok, continue reading bluetooth information
                int name_data_length = (data.length == INFO_DATA_LENGTH ? INFO_NAME_LENGTH : OLD_INFO_NAME_LENGTH);
                int name_length = name_data_length;

                // Check for the end of the name
                for (int i = 0; i < name_data_length; i++)
                {
                    if ( data[i] == 0 )
                    {
                        name_length = i;
                        break;
                    }
                }

                // Check for the end of the address
                int address_length = INFO_ADDRESS_LENGTH;
                for (int i = 0; i < INFO_ADDRESS_LENGTH; i++)
                {
                    if ( data[ i + name_data_length] == 0 )
                    {
                        address_length = i;
                        break;
                    }
                }

                try
                {
                    // Try to set the name and address
                    this.name = new String( data, 0, name_length, "UTF-8" );
                    this.address = new String( data, name_data_length, address_length, "US-ASCII" );
                }
                catch ( UnsupportedEncodingException e )
                {
                    // Encoding not supported
                    this.name = null;
                    this.address = null;
                }
            }
        }
    }


    /**
     * Returns the name of the device
     *
     * @return the name of the device or null if no name could be fetched
     */
    public String getName()
    {
        return this.name;
    }


    /**
     * Returns the Bluetooth address of the device
     *
     * @return The Bluetooth address of the device, or null if no address could
     * be fetched
     */
    public String getAddress()
    {
        return this.address;
    }
}
