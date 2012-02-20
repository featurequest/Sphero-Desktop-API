package site.nicklas.sphero.command;

/**
 * A command to get Bluetooth information from the Sphero
 * 
 * @author Nicklas Gavelin
 */
public class GetBluetoothInfoCommand extends DeviceCommand
{   
    /**
     * Create a GetBluetoothInfoCommand
     */
    public GetBluetoothInfoCommand()
    {
        super( DEVICE_COMMAND.GET_BLUETOOTH_INFO );
    }
}
