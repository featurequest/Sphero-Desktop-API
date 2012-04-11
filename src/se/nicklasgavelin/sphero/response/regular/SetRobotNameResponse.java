package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Response for the SetRobotNameCommand
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class SetRobotNameResponse extends ResponseMessage
{
	/**
	 * Create a SetRobotNameResponse from the received data
	 * 
	 * @param rh The response header containing the response data
	 */
	public SetRobotNameResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
