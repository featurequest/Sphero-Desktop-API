package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Response for the PingCommand
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class PingResponse extends ResponseMessage
{
	/**
	 * Create a PingResponse from the received data
	 * 
	 * @param data The received data
	 */
	public PingResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
