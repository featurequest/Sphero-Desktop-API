/*
 * Please read the LICENSE file that is included with the source
 * code.
 */
package se.nicklasgavelin.sphero.response.information;

import se.nicklasgavelin.sphero.response.InformationResponseMessage;

// TODO:  add the collision detection data fields

/**
 * Asynchronous response notifying when a collision has been detected
 * 
 * @author John Sichi, jsichi@gmail.com
 */
public class CollisionResponse extends InformationResponseMessage
{
	/**
	 * Create a CollisionResponse from the received data
	 * 
	 * @param rh The response header containing the response data
	 */
	public CollisionResponse( ResponseHeader rh )
	{
		super( rh );
	}
}
