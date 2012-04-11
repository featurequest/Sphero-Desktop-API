package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 *         Technology
 */
public class RunMacroResponse extends ResponseMessage
{
	/**
	 * Create a run macro response message
	 * 
	 * @param rh The response header containing the response data
	 */
	public RunMacroResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
