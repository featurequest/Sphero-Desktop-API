package se.nicklasgavelin.sphero.command;

import se.nicklasgavelin.sphero.command.DeviceCommand.DEVICE_COMMAND;

/**
 * Macro command containing macros for the Sphero
 *
 * @deprecated Under development, doesn't work atm.
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class MacroCommand extends DeviceCommand
{
//  private ArrayList<site.nicklas.sphero.command.MacroCommandSphero> commands = new ArrayList<site.nicklas.sphero.command.MacroCommandSphero>();
//  private ArrayList<MacroCommand> lastCommands = new ArrayList<MacroCommand>();
//  private ArrayList<Integer> ballMemory = new ArrayList<Integer>();
//  private Robot mRobot;
    public MacroCommand()
    {
        super( DEVICE_COMMAND.MACRO );
    }
//  /**
//   * Returns the macro commands currently in the list
//   *
//   * @return The macros in the list
//   */
//  public ArrayList<site.nicklas.sphero.command.MacroCommandSphero> getCommands()
//  {
//    return this.commands;
//  }
//
//  /**
//   * Add a macro to the command
//   *
//   * @param macro The macro to add
//   */
//  public void addCommand( site.nicklas.sphero.command.MacroCommandSphero macro )
//  {
//    this.commands.add(macro);
//  }
//
//  /*public void setRobot(Robot robot) {
//    this.mRobot = robot;
//  }
//
//  public void playMacro()
//  {
//      if (this.mRobot == null)
//        return;
//
////      SaveTemporaryMacroCommand.sendCommand(this.mRobot, 1, generateMacroData());
////      RunMacroCommand.sendCommand(this.mRobot, -1);
//  }
//
//  public void stopMacro()
//  {
//    if (this.mRobot == null)
//      return;
//
//    //AbortMacroCommand.sendCommand(this.mRobot);
//
//    this.commands.clear();
//    this.ballMemory.clear();
//    this.running = false;
//  }*/
//
////  private Integer freeBallMemory()
////  {
////    Integer bytesInUse = Integer.valueOf(0);
////    for (Iterator i = this.ballMemory.iterator(); i.hasNext(); ) {
////      bytesInUse = Integer.valueOf(bytesInUse.intValue() + ((Integer)i.next()).intValue());
////    }
////    Integer freeBytes = Integer.valueOf(1000 - bytesInUse.intValue());
////    return freeBytes;
////  }
//
////  private void emptyCommandQueue()
////  {
////    if (this.commands.size() == 0) return;
////
////    int freeBytes = freeBallMemory().intValue();
////    if (freeBytes < 128) return;
////    if (freeBytes > 250) freeBytes = 250;
////
////    int maxIndex = 0;
////    int chunkSize = 0;
////    ArrayList chunk = new ArrayList();
////    for (Iterator i = this.commands.iterator(); i.hasNext(); ) {
////      MacroCommand command = (MacroCommand)i.next();
////      if (chunkSize + command.getLength().intValue() >= freeBytes) break;
////      chunkSize += command.getLength().intValue();
////      maxIndex++;
////      chunk.add(command);
////    }
////
////    chunk.add(new Emit(Integer.valueOf(1)));
////    chunkSize += 2;
////
////    ByteArrayBuffer data = new ByteArrayBuffer(chunkSize);
////    for (Iterator i = chunk.iterator(); i.hasNext(); ) {
////      MacroCommand command = (MacroCommand)i.next();
////      data.append(command.getByteRepresentation(), 0, command.getLength().intValue());
////    }
////
////    this.ballMemory.add(new Integer(chunkSize));
////
////    if (this.mRobot != null) SaveMacroCommand.sendCommand(this.mRobot, 1, -2, data.toByteArray());
////
////    this.commands.removeAll(chunk);
////
////    if ((freeBallMemory().intValue() > 128) && (this.commands.size() > 0))
////      emptyCommandQueue();
////  }
//
//  	@Override
//  	protected byte[] getPacketData()
//  	{
//  		byte[] macroData = generateMacroData();
//  		byte[] data = new byte[ macroData.length + 2];
//
//		data[0] = -1;
//		data[1] = 1;
//
//		for (int i = 0; i < macroData.length; i++)
//			data[(i + 2)] = macroData[i];
//
//		return data;
//  	}
//
//  	/**
//  	 * Converts the macro to a byte array
//  	 *
//  	 * @return The macro byte array
//  	 */
//	private byte[] generateMacroData()
//	{
//		ByteArrayBuffer data = new ByteArrayBuffer(256);
//		Integer currentLength = Integer.valueOf(0);
//
//		for ( Iterator<site.nicklas.sphero.command.MacroCommandSphero> i = this.commands.iterator(); i.hasNext(); )
//		{
//			if (currentLength.intValue() > 248)
//			{
//				Roll stop = new Roll( Double.valueOf( 0.0D ), Integer.valueOf( 0 ), Integer.valueOf( 0 ) );
//				data.append(stop.getByteRepresentation(), 0, stop.getLength().intValue());
//
//				break;
//			}
//
//			site.nicklas.sphero.command.MacroCommandSphero command = (site.nicklas.sphero.command.MacroCommandSphero) i.next();
//			data.append(command.getByteRepresentation(), 0, command.getLength().intValue());
//			currentLength = Integer.valueOf(currentLength.intValue() + command.getLength().intValue());
//		}
//
//		data.append(site.nicklas.sphero.command.MacroCommandSphero.MAC_END);
//
//		return data.toByteArray();
//	}
}