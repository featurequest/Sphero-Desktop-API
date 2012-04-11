package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Response for the RotationRateResponse
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RotationRateResponse extends ResponseMessage
{
	/**
	 * Create a RotationRateResponse from the received data
	 * 
	 * @param rh The response header containing the response data
	 */
	public RotationRateResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
