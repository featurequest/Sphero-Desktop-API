package se.nicklasgavelin.sphero.macro;

/**
 * Still experimental
 * 
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public abstract class MacroCommand
{
	private MACRO_COMMAND command;

	// /**
	// * Create a macro command with a given command value and data
	// * @param command
	// * @param data
	// */
	// public MacroCommand( MACRO_COMMAND command, byte[] data )
	// {
	// this.command = command;
	// }

	/**
	 * Create a macro command with the given command value
	 * 
	 * @param command The command value for the new command
	 */
	protected MacroCommand( MACRO_COMMAND command )
	{
		this.command = command;
	}

	/**
	 * Returns the byte length of the command
	 * 
	 * @return The byte length of the command
	 */
	public int getLength()
	{
		return this.command.getLength();
	}

	/**
	 * Returns the byte representation for the command
	 * 
	 * @return The command as a byte array
	 */
	public abstract byte[] getByteRepresentation();

	/**
	 * Returns the unique id for the command
	 * 
	 * @return The unique id for the command
	 */
	public byte getCommandID()
	{
		return this.command.getByteValue();
	}

	/**
	 * Macro command values
	 */
	public enum MACRO_COMMAND
	{
		MAC_END( 0, -1 ), // OK!
		MAC_SD1( 1, 3 ), // Working
		MAC_SD2( 2, 3 ), // Working
		// MAC_STABILIZATION( 3, -1 ),
		MAC_CALIBRATE( 4, 3 ), // Working
		MAC_ROLL( 5, 5 ), MAC_ROLL_SD1( 6, 4 ), // Working
		MAC_RGB( 7, 5 ), // Working
		MAC_RGB_SD2( 8, 4 ), // Working
		MAC_FRONT_LED( 9, 2 ), // Working
		MAC_RAW_MOTOR( 10, 6 ), // TODO: Debug
		MAC_DELAY( 11, 3 ), // Working
		// MAC_GOTO( 12, -1 ),
		// MAC_GOSUB( 13, -1 ),
		// MAC_SLEEP( 14, -1 ),
		MAC_SPD1( 15, 2 ), // Working
		MAC_SPD2( 16, 2 ), // Working
		MAC_ROLL_SPD1_SD1( 17, 3 ), // Working
		MAC_ROLL_SPD2_SD1( 18, 3 ), // Working
		MAC_ROTATION_RATE( 19, 2 ), MAC_FADE( 20, 6 ), // Working
		MAC_EMIT( 21, 2 ), // ?
		MAC_WAIT_UNTIL_STOP( 25, 3 ), // ?
		MAC_ROTATE( 26, 4 ), // NOT WORKING YET
		MAC_STREAM_END( 27, 1 ); // TODO: Maybe working?

		// Internal storage
		private int value, length;

		/**
		 * Creates a macro command with a specific id value
		 * 
		 * @param value The id for the command
		 */
		private MACRO_COMMAND( int value, int length )
		{
			this.value = value;
			this.length = length;
		}

		/**
		 * Returns the command id
		 * 
		 * @return The command id
		 */
		public int getValue()
		{
			return this.value;
		}

		/**
		 * Returns the command id as a byte value
		 * 
		 * @return The command id as a byte value
		 */
		public byte getByteValue()
		{
			return (byte) this.value;
		}

		/**
		 * Returns the command length value
		 * 
		 * @return The length of the command (in bytes)
		 */
		public int getLength()
		{
			return this.length;
		}

		/**
		 * Returns the MACRO_COMMAND that is represented by the given id value
		 * or null if no MACRO_COMMAND could be found for the given id value.
		 * 
		 * @param value The MACRO_COMMAND id value
		 * 
		 * @return The MACRO_COMMAND for the given value or null if no
		 *         MACRO_COMMAND could be represented by the given value
		 */
		public static MACRO_COMMAND valueOf( int value )
		{
			MACRO_COMMAND[] mc = MACRO_COMMAND.values();
			for( int i = 0; i < mc.length; i++ )
				if( mc[i].getValue() == value )
					return mc[i];
			return null;
		}
	}
}