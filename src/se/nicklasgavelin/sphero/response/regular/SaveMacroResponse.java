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
public class SaveMacroResponse extends ResponseMessage
{
	/**
	 * Create a save macro response message
	 * 
	 * @param rh The response header containing the response data
	 */
	public SaveMacroResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
