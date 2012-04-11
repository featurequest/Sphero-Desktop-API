package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class SaveTemporaryMacroResponse extends ResponseMessage
{
	/**
	 * Create a save temporary macro response message
	 * 
	 * @param rh The response header containing the response data
	 */
	public SaveTemporaryMacroResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
