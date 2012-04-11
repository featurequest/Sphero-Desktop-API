package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Create a respons for the RawMotorCommand
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RawMotorResponse extends ResponseMessage
{
	/**
	 * Create a RawMotorResponse from the received data
	 * 
	 * @param  rh The response header containing the response data
	 */
	public RawMotorResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
