package site.nicklas.sphero;

import site.nicklas.sphero.command.DeviceCommand;
import site.nicklas.sphero.response.DeviceResponse;

/**
 * Interface for listening on a robot.
 * You need to perform Robot.addListener to listen for robot events
 *
 * @author Nicklas Gavelin
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
        NO_CONNECTION_EXISTS
    }


    /**
     * Called when a response is received for the robot the
     * robot listener is listening on
     *
     * @param r        The robot connected to the response
     * @param response The response received
     * @param dc       The command the response is for
     */
    public void responseReceived( Robot r, DeviceResponse response, DeviceCommand dc );


    /**
     * Called when a specific event has occurred, the code
     * given is the type of event that occurred.
     *
     * @author Nicklas Gavelin
     * @param r    The robot that is connected to the event
     * @param code The EVENT_CODE that occurred (RobotListener.EVENT_CODE)
     */
    public void event( Robot r, EVENT_CODE code );
}
