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
	 * @param data The received data
	 */
	public JumpToMainResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
