package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

// TODO:  add the collision detection data fields

/**
 * Response for the CollisionDetectionCommand
 * 
 * @author John Sichi, jsichi@gmail.com
 */
public class CollisionDetectionResponse extends ResponseMessage
{
	/**
	 * Create a CollisionDetectionResponse from the received data
	 * 
	 * @param rh The response header containing the response data
	 */
	public CollisionDetectionResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
