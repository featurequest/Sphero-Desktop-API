package experimental.sphero.command;

import se.nicklasgavelin.sphero.command.DeviceCommand;

public class RunMacroCommand extends DeviceCommand
{
    private byte identifier;


    public RunMacroCommand( byte macroId )
    {
        super( DEVICE_COMMAND.RUN_MACRO );
        this.identifier = macroId;
    }


    public RunMacroCommand( int macroId )
    {
        this( ( byte ) macroId );
    }


    @Override
    protected byte[] getPacketData()
    {
        byte[] data = new byte[ 1 ];

        data[0] = this.identifier;

        return data;
    }
}
