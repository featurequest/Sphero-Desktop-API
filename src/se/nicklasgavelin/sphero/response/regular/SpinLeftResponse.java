package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Response for the SpinLeftCommand
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SpinLeftResponse extends ResponseMessage
{
	/**
	 * Create a SpinLeftResponse from the received data
	 * 
	 * @param data The received data
	 */
	public SpinLeftResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
