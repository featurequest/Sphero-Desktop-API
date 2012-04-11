package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * The response for the JumpToMainCommand.
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class JumpToMainResponse extends ResponseMessage
{
	/**
	 * Create the JumpToMainResponse from the received data
	 * 
	 * @param rh The response header containing the response data
	 */
	public JumpToMainResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
