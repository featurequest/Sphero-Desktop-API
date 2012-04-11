/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.response;

/**
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class InformationResponseMessage extends ResponseMessage
{
	// private static final int INFORMATION_RESPONSE_CODE_INDEX = 2;

	/**
	 * Response codes for the information response messages
	 */
	public enum INFORMATION_RESPONSE_CODE
	{
		EMIT( 6 ), DATA( 3 );

		private byte code;

		private INFORMATION_RESPONSE_CODE( int code )
		{
			this.code = (byte) code;
		}

		public byte getCode()
		{
			return this.code;
		}

		public static INFORMATION_RESPONSE_CODE valueOf( byte code )
		{
			INFORMATION_RESPONSE_CODE[] res = INFORMATION_RESPONSE_CODE.values();
			for( INFORMATION_RESPONSE_CODE r : res )
				if( r.getCode() == code )
					return r;
			return null;
		}

		public static INFORMATION_RESPONSE_CODE valueOf( int code )
		{
			return INFORMATION_RESPONSE_CODE.valueOf( (byte) code );
		}
	}

	/* Internal response code */
	private INFORMATION_RESPONSE_CODE responseType;

	/**
	 * Create an information response from the response header
	 * 
	 * @param rh The response header
	 */
	public InformationResponseMessage( ResponseHeader rh )
	{
		super( rh );
		this.responseType = INFORMATION_RESPONSE_CODE.valueOf( rh.getRawPacket()[InformationResponseMessage.INFORMATION_RESPONSE_TYPE_INDEX] );
	}

	/**
	 * Returns the information response type
	 * 
	 * @return The information response type
	 */
	public INFORMATION_RESPONSE_CODE getInformationResponseType()
	{
		return this.responseType;
	}

	/**
	 * Fetch the information response from the data
	 * 
	 * @param rh The response header
	 * @return The information response or null
	 */
	public static InformationResponseMessage valueOf( ResponseHeader rh )
	{
		return (InformationResponseMessage) ResponseMessage.valueOf( null, rh );
	}
}
