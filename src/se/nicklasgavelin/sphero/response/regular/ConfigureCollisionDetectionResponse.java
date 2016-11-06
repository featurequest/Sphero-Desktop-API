/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.response.regular;

import se.nicklasgavelin.sphero.response.ResponseMessage;

/**
 * Response for the CollisionDetectionCommand.  Note that this synchronous
 * response just acknowledges the configuration request; actual collision
 * detection notifications arrive asynchronously as CollisionResponse
 * information messages.
 * 
 * @author John Sichi, jsichi@gmail.com
 */
public class ConfigureCollisionDetectionResponse extends ResponseMessage
{
	/**
	 * Create a ConfigureCollisionDetectionResponse from the received data
	 * 
	 * @param rh The response header containing the response data
	 */
	public ConfigureCollisionDetectionResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
