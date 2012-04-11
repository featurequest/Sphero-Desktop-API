package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Response for the RollCommand
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RollResponse extends ResponseMessage
{
	/**
	 * Create a RollResponse from the received data
	 * 
	 * @param rh The response header containing the response data
	 */
	public RollResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
