package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Create a response for the RGBLEDCommand
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class RGBLEDResponse extends ResponseMessage
{
	/**
	 * Create a RGBLEDResponse from the received data
	 * 
	 * @param rh The response header containing the response data
	 */
	public RGBLEDResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
