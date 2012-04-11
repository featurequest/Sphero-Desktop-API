package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Response for the SleepCommand
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SleepResponse extends ResponseMessage
{
	/**
	 * Create a SleepResponse from the received data
	 * 
	 * @param rh The response header containing the response data
	 */
	public SleepResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
