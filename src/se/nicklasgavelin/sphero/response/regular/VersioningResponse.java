package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Version response giving the version of the Sphero.
 * 
 * @author Orbotix
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class VersioningResponse extends ResponseMessage
{
	private int modelNumber = 0;

	// Indexes
	private static final int RECORD_VERSION_INDEX = 0, MODEL_VERSION_INDEX = 1,
			HARDWARE_VERSION_INDEX = 2, MAIN_APPLICATION_VERSION_INDEX_1 = 3,
			MAIN_APPLICATION_VERSION_INDEX_2 = 4,
			BOOTLOADER_VERSION_INDEX = 5, ORB_BASIC_VERSION_INDEX = 6,
			OVERLAY_MANAGER_VERSION_INDEX = 7;

	// Values
	private String recordVersion = null, hardwareVersion = null,
			mainApplicationVersion = null, overlayManagerVersion = null,
			bootloaderVersion = null, orbBasicVersion = null;

	/**
	 * Create a version response from a data array
	 * 
	 * @param rh The response header containing the response data
	 */
	public VersioningResponse( ResponseHeader rh )// byte[] data )
	{
		super( rh );// super( DEVICE_COMMAND.VERSIONING, data );

		// Check so that we got a valid response
		if( !isCorrupt() )
		{
			byte[] data = this.getPacketPayload();

			System.out.println( data.length );

			this.recordVersion = ( ( data[RECORD_VERSION_INDEX] >> 4 ) + "." + ( 0xF & data[RECORD_VERSION_INDEX] ) );
			this.modelNumber = data[MODEL_VERSION_INDEX];
			this.hardwareVersion = ( ( data[HARDWARE_VERSION_INDEX] >> 4 ) + "." + ( 0xF & data[HARDWARE_VERSION_INDEX] ) );
			this.mainApplicationVersion = ( data[MAIN_APPLICATION_VERSION_INDEX_1] + "." + data[MAIN_APPLICATION_VERSION_INDEX_2] );
			this.bootloaderVersion = ( ( data[BOOTLOADER_VERSION_INDEX] >> 4 ) + "." + ( 0xF & data[BOOTLOADER_VERSION_INDEX] ) );
			this.orbBasicVersion = ( ( data[ORB_BASIC_VERSION_INDEX] >> 4 ) + "." + ( 0xF & data[ORB_BASIC_VERSION_INDEX] ) );
			this.overlayManagerVersion = ( ( data[OVERLAY_MANAGER_VERSION_INDEX] >> 4 ) + "." + ( 0xF & data[OVERLAY_MANAGER_VERSION_INDEX] ) );
		}
	}

	/**
	 * Returns the bootloader version number
	 * 
	 * @return The bootloader version number or null if no version number
	 */
	public String getBootloaderVersion()
	{
		return this.bootloaderVersion;
	}

	/**
	 * Returns the hardware version number
	 * 
	 * @return The hardware version number null if no version number
	 */
	public String getHardwareVersion()
	{
		return this.hardwareVersion;
	}

	/**
	 * Returns the main application version number
	 * 
	 * @return The main application version number null if no version number
	 */
	public String getMainApplicationVersion()
	{
		return this.mainApplicationVersion;
	}

	/**
	 * Returns the model number
	 * 
	 * @return The model number or 0 if no version number
	 */
	public int getModelNumber()
	{
		return this.modelNumber;
	}

	/**
	 * Returns the orb basic version number
	 * 
	 * @return The orb basic version number null if no version number
	 */
	public String getOrbBasicVersion()
	{
		return this.orbBasicVersion;
	}

	/**
	 * Returns the overlay manager version number
	 * 
	 * @return The overlay manager version number null if no version number
	 */
	public String getOverlayManagerVersion()
	{
		return this.overlayManagerVersion;
	}

	/**
	 * Returns the record version number
	 * 
	 * @return The record version number null if no version number
	 */
	public String getRecordVersion()
	{
		return this.recordVersion;
	}
	
	@Override
	public String toString()
	{
		return getClass().getCanonicalName() + 
				"[recordVersion=" + getRecordVersion() + 
				", overlayManagerVersion=" + getOverlayManagerVersion() + 
				", orbBasicVersion=" + getOrbBasicVersion() + 
				", modelNumber=" + getModelNumber() + 
				", mainApplicationVersion=" + getMainApplicationVersion() + 
				", hardwareVersion=" + getHardwareVersion() +
				", bootloaderVersion=" + getBootloaderVersion() + 
				"]";
	} 
}
