package site.nicklas.sphero.command;

/**
 * Command to get the configuration of the Sphero.
 * Havn't seen any visible return values.
 *
 * @author Nicklas Gavelin
 */
public class GetConfigurationBlockCommand extends DeviceCommand
{
    /**
     * Specifies the block
     *
     * @author Nicklas Gavelin
     */
    public enum BLOCK_SPECIFIER
    {
        FACTORY( 0 ),
        USER( 1 );
        private int block;


        private BLOCK_SPECIFIER( int block )
        {
            this.block = block;
        }


        /**
         * Returns the integer value for the block
         * specifier.
         *
         * @return The integer value of the block specifier
         */
        public int getValue()
        {
            return this.block;
        }


        /**
         * Creates a block specifier from a integer value
         *
         * @param i The integer value for the block specifier
         *
         * @return A block specifier or null if no specifier could be
         *         represented by the given integer value
         */
        public BLOCK_SPECIFIER valueOf( int i )
        {
            switch ( i )
            {
                case 0:
                    return FACTORY;
                case 1:
                    return USER;
            }

            return null;
        }
    }
    private BLOCK_SPECIFIER block;


    /**
     * Create a get configuration block command with a given block specifier
     *
     * @param block The block specifier for the command
     */
    public GetConfigurationBlockCommand( BLOCK_SPECIFIER block )
    {
        super( DEVICE_COMMAND.GET_CONFIGURATION_BLOCK );
        this.block = block;
    }


    /**
     * Returns the internal block specifier
     *
     * @return The block specifier for this command
     */
    public BLOCK_SPECIFIER getBlockSpecifierValue()
    {
        return this.block;
    }


    @Override
    protected byte[] getPacketData()
    {
        return new byte[]
                {
                    ( byte ) this.block.getValue()
                };
    }
}
