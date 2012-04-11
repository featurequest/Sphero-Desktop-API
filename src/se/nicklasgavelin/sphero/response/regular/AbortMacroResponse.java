/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class AbortMacroResponse extends ResponseMessage
{
	public static final int NO_MACRO_RUNNING = 0;
	public static final int MACRO_ID_INDEX = 0;
	private int macroId;

	
	/**
	 * Create an abort macro response message
	 * 
	 * @param rh The response header containing the response data
	 */
	public AbortMacroResponse( ResponseHeader rh )// byte[] data )
	{
		super( rh );
		this.macroId = rh.getPacketPayload()[MACRO_ID_INDEX];
	}

	/**
	 * Returns the internal macro id
	 * 
	 * @return The macro id that was aborted
	 */
	public int getMacroId()
	{
		return this.macroId;
	}
}
