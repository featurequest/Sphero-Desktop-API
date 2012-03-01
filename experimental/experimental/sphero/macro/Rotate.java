/*
 * Please read the LICENSE file that is included with the source
 * code.
 */

package experimental.sphero.macro;

import se.nicklasgavelin.sphero.macro.MacroCommand;

/**
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class Rotate extends MacroCommand
{
    public Rotate()
    {
        super( MACRO_COMMAND.MAC_ROTATE );
    }

    @Override
    public byte[] getByteRepresentation()
    {
//        int heading = 90;
//        int speed = 255;
//
//        byte[] data = new byte[ MACRO_COMMAND.MAC_ROTATE.getLength() ];
//        data[0] = getCommandID();
//        data[3] = (byte)(speed); // Rotations?
//        data[1] = (byte)(heading); // ?
//        data[2] = (byte)(heading);

        return null;
    }
}
