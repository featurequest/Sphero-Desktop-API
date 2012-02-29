package se.nicklasgavelin.sphero;

import se.nicklasgavelin.sphero.command.CommandMessage;
import se.nicklasgavelin.sphero.response.ResponseMessage;
import se.nicklasgavelin.sphero.response.information.DeviceInformationResponse;

/**
 * Interface for listening on a robot.
 * You need to perform Robot.addListener to listen for robot events
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public interface RobotListener
{
    /**
     * Event codes for the Robot
     */
    public enum EVENT_CODE
    {
        CONNECTION_ESTABLISHED,
        DISCONNECTED,
        CONNECTION_FAILED,
        CONNECTION_CLOSED_UNEXPECTED,
        NO_CONNECTION_EXISTS,
        MACRO_DONE;
    }


    /**
     * Called when a response is received for the robot the
     * robot listener is listening on
     *
     * @param r        The robot connected to the response
     * @param response The response received
     * @param dc       The command the response is for
     */
    public void responseReceived( Robot r, ResponseMessage response, CommandMessage dc );


    /**
     * Called when a specific event has occurred, the code
     * given is the type of event that occurred.
     *
     * @author Nicklas Gavelin
     * @param r    The robot that is connected to the event
     * @param code The EVENT_CODE that occurred (RobotListener.EVENT_CODE)
     */
    public void event( Robot r, EVENT_CODE code );


    /**
     * Called when an information response is received (streaming event or similar)
     *
     * @param r The robot the message was received for
     * @param response The response itself
     */
    public void informationResponseReceived( Robot r, DeviceInformationResponse response );
}
