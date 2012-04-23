# Development Quick Start Guide

## Overview

## Compatibility
    Works in Windows, Mac and Linux (In linux an extra Bluecove library named Bluecove GPL located at
    http://bluecove.org/bluecove-gpl/ may be needed to get the API working)

    Only tested on Windows and Mac (10.7). A hardware bluetooth module is required to allow for the connection
    between Sphero and desktop. All bluetooth modules supported by bluecove should work just fine with the API.
    If there is anything that doesn't work, feel free to contact me (I will respond when time allows it).

## IDE
	Sphero API Desktop Port will work with Netbeans and Eclipse out of the box (hopefully ;))

There already exists files that support importing the API directly into Eclipse or Netbeans
without creating a new project and setting up the classpaths if you download the source code directly.

If you instead download the .zip file that contains the compressed version of the library it already got
all necessary native libraries included and you just need to include the jar file together with the java doc
in your project.

## Source Compiling
If you are gonna compile the code directly there is a small script that will compile a releasable version of the
API named compile.sh. The compile script requires the "ant" and "zip" command and uses the build.xml file to
compile the code into a single .jar file.

	./compile.sh

The above command will compile the source into the dist/ directory and create a .zip file in the base dir that contains
both the .jar file.

	./compile.sh 1

The above command will compile the source the same way as the previous command and will also generate the java doc for
the source code into the dist/ directory and also include this in the zip file.

There may be bugs in both the compile script and the build file if you are building it as its only been tested on a single
workstation during the development.

To manually compile the code directly using ant you may run

	ant nojavadoc

to compile the code into dist/ without the java doc or you can run

	ant withjavadoc

to compile the code into dist/ WITH the java doc.

## How to use the API
### Connect
The API is similar to that of the original Orbotix Sphero API with some modifications to support connecting to multiple
Sphero devices simultaniously and sending individual commands to these. There is examples in the se.nicklasgavelin.sphero.example
package that will show a quick example of how to use the API.

To connect to a Sphero device you may either perform a bluetooth device search as shown in one of the examples or you can use
the direct bluetooth address to connect to it directly without having to perform a search. Although the second method will prevent
some commands to be performed correctly and the bluetooth name will not be retrieved properly.

    String id = "<BluetoothIdForSphero>";
	Bluetooth bt = new Bluetooth( this, Bluetooth.SERIAL_COM );
	BluetoothDevice btd = new BluetoothDevice( bt, "btspp://" + id + ":1;authenticate=true;encrypt=false;master=false" );
	Robot r = new Robot( btd );

	if( r.connect() )
	{
		// Successfully connected to Sphero device
		// may start sending commands now

		// Send a RGB command that will turn the RGB LED red
		r.sendCommand( new RGBCommand( 255, 0, 0 ) );

		// Send a roll command to the Sphero with a given heading
		// Notice that we havn't calibrated the Sphero so we don't know
		// which way is which atm.
		r.sendCommand( new RollCommand( 1, 180, false ) );

		// Now send a time delayed command to stop the Sphero from
		// rolling after 2500 ms (2.5 seconds)
		r.sendCommand( new RollCommand( 1, 180, true ), 2500 );
	}
	else
		// Failed to connect to Sphero device due to an error

Notice that you can add a RobotListener to the Robot object to get events and responses from
the Sphero device

	r.addListener( <RobotListener> );

### Macro usage
For creating and sending macros there are two different methods. One is streaming a macro
to the Sphero which allows for larger macros than the normal method. When streaming you record
your macro and then asks the Robot.class to send the macro to the Sphero using the streaming method.
The Robot.class then divides the macro into chunks and sends them when there is enough required space
on the Sphero device to store and run the macro.

    Notice that macro usage was not supported until later versions of this API and if you have an older version I
    suggest you pull the newest one from the repository.

#### Normal method
The normal method is the default method and can be run like this.

    <Already created and connected to Robot r>

    // Create our macro object (seen as a command)
    MacroObject mo = new MacroObject();

    // Add macro commands to the macro object
    mo.addCommand( new RGBSD2( Color.RED ) );
    mo.addCommand( new Delay( 2000 ) );
    mo.addCommand( new RGBSD2( Color.BLUE ) );
    mo.addCommand( new Delay( 2000 ) );

    // Send the macro object to the Sphero
    r.sendCommand( mo );

The above example will first turn the Sphero RED and stay RED for two seconds. Then it will quickly
turn BLUE and stay that way for another two seconds and then it should return to its original state.

So the above command describes the normal method for sending a single macro. Although this method
only allows for up to 256 byte macros (including surrounding packet headers). So instead a better
method is to use the streaming method described below.

#### Cached streaming method
The cached streaming method is similar to the normal method with the difference that a huge macro
is divided into chunks instead of being transmitted in its complete form.

    <Already created and connected to Robot r>

    MacroObject mo = new MacroObject();
    // .... As in previous example

    // Set cached streaming mode
    mo.setMode( MacroObject.MacroObjectMode.CachedStreaming );

    // Send the command to the Sphero
    r.sendCommand( mo );

### Experimental code
There are some code that is experimental and is implemented and tested in the "experimental" package. You can look
there from time to time to see what is up and example methods and usages of the Sphero device.

    Notice however that this code is not under any documentation requirements and may or may not work at time, temporarily lock
    your Sphero (will require a "reboot" by setting it in the charger until it turns off by blinking in a range of colors) or
    may in some cases literally run your Sphero into the wall (speed and special movement experiments).

    So folks, no promices about experimental code! Sphero is after all our own little gunnie pig ;-)


# Contact & Suggestions
If you think this README file doesn't cover all it should (as much as a basic readme file should) or if you
have some suggestions for improvements please send me a mail at nicklas.gavelin@gmail.com.

# Recognition
This Sphero API port was developed during my thesis work at Luleå University of Technology, http://www.ltu.se, and for the SITE project.
Updates are pushed when time is found during the thesis work (as this is only a part of my work development will not solely be aimed at this API).

The API was developed by Nicklas Gavelin, http://nicklasgavelin.se from backwards-engineering of the original Android API for Sphero that was developed by Orbotix.

# Versions
There is no guarantee that packages will maintain their naming standard or that classes will be left intact. Although I'm trying to keep
the impact on previous versions as small as possible regarding compatibility (although as in new projects this isn't always possible).

## Sphero versions
This API is tested on Sphero robotic balls with versions:
	
	recordVersion=0.1, 
	overlayManagerVersion=0.2, 
	orbBasicVersion=0.0, 
	modelNumber=2, 
	mainApplicationVersion=0.99, 
	hardwareVersion=0.2, 
	bootloaderVersion=1.7
	
I can't guarantee that it will work on older/newer versions of the Sphero ball.

# License
Read the LICENSE file for more information :-)


# Sphero
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWWWNWNWWWWWWWWWMWMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNNHHHHHHHHNNNNNNWWWWWWWWWMWMMMMMMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWHHHKKKQKKKKKHHHHHNNNNNWWWWWWWMWMMWMMMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNHKQDDDDDDQQQKKKHHHHNNNNNWWWWWWWWWWWWMWMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWNHKDDXSSSXXXDDQQKKKKHHHNNNNNWWWWWWWWWWWWWWWMMWMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNHKDXSS6SSSSXXXDDQQKKKHHHHNNNNNWWWWWWWWWWWWWWWWWMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMNHQDSS655566SSXXDDDQQQKKKHHHHHNNNNNNWNWWWWWWWWWWWWMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWHKQXS55YY5566SSXXDDDQQKKKKHHHHHNNNNNNNNNWWWWWWWWWWWWMWMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMNHQXS5YJJJY5556SSXXXDDDQQKKKKHHHHHNNNNNNNNNNNWWWWWWWWWWMWMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMNHQX65JJJJJYY556SSXXDDDQQKKKKKKKKHHHHHHNNHNWNNNNWNWWWWWWWMWMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMNHDX65JJtttJYY566SSXDDDQQQQKKKKD6jccccccccccccjcJNNNWWWWWWWMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMNHQS65JtjjtttJY566SSXDDDQQQQQDYccccccccjQHNNNNNK5jccYNWWWWWWWMWMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMNHQX65JtjjjjjtJY566SXXDDQQQKKKQKQQ6ccc5HHNNNNNNWNNWQccjWWWWWWWWMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMHKDS5JtjjjjjttJY56SSXDDDQQKKKKKKKYcciHHHNNNNNNNNWWNNWXjjKWWWWWMMWMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMNKQX6YJjjcjjjjJY556SSXDDDQQKKKKKKHcccKKHNHNKjjjXNWKjjKW5jjWWWWWMWMWMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMKQDS5JJjjcjjjjJY556SSXDDDQQKKKKKKQcccHHHNNNtjjjjQWXjjjMHjjWWWMMMWMWMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMWKQXS5JtjjcjjjtJY556SSXDDQQKKKKHKKKcciHHHNNNSjjjjSMMYj6MHjjWWMWMMMMWMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMWQDXSYJtjcjcjjjJY56SSXXDDDQKKKKKHKHcccDHHNNNNQjjjMMMMMMMXjjWWWMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMWQDX6YJtjcccjjjJY55SSXXDDQKKKKHHHKKQcccHNNNNNNWWMMMMMMMHtjQWWWWMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMQDX6YJtjcccjjjJY566SXXDDQQKKKKHHHHHHcccjHNNNNNWWWWMMMXJtQWWWWMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMKDX6YJtjjcjjjjJY55SSXXDDQQKKKHHHHHHHH5jcjjJHNNNNWNXjttYWWWWWWMWMMMMWMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMHQXS5JtjcjcjttJY56SSXXDDQQKKKHKHHHHNHHHKcjcccjjjjjjtSWWWWWWMMMWMMMWMMMMMWMMM
    MMMMMMMMMMMMMMMMMMMMMMMMWQDS6YJtjccjjtJY55SSXDDDQQKKKHHHHNHNNNNNNHNNWNNNWNNWWWWWWWWMMMMMMMMMMMMWMMMW
    MMMMMMMMMMMMMMMMMMMMMMMMWHQXS5YJjjcjjtJY556SXXDDQQKKKKHHHHNNNNNNNNNNNNNWWWWWWWWWWWMWMWMMMMMMMMMWWWWM
    MMMMMMMMMMMMMMMMMMMMMWMWWWHQXS5YJtjjjjtJ556SSXDDQQKKKKHHHNNNNNNNWNNWWWWWWWWWWWWWWWMMWWMMWMMMMMWWWWWW
    MMMMMMMMMMMMMMMMMMMMMWWWWWWKQXS5YJtttttJY566SXDDDQKKKKHHHHNNNNNNNWWWWWWWWWWWWWWWWMWWMWWMWMWMMWWWWWWW
    MMMMMMMMMMMMMMMMMMWMWWWWWWWWKQDX65YJJtJJY566SXDDDQKKKKKHHHNNNNNNNWNWWWWWWWWWWWWWWWWWWMMMMMMWWWWWWWWW
    MMMMMMMMMMMMMMMMMWWWWWWWWWWWWKKDXS55YYYYY566SXXDDQQKKKKHHHNNNNNNNWNWWWWWWWWWWWWWWWWWWMWMMMMWNWNWWWWW
    MMMMMMMMMMMMMMMMWWWWWWWWWWWNNNNKQDXS65555566SSXDDQKKKKKHHHNHNNNNNNWNWWWWWWWWWWWWWWWWMWMMMWKHHHHHHHHN
    MMMMMMMMMMMMMMMWWWWWWWWWNNNNNNHNKKQDXSS6666SSSXDDQQKKKKHHHHNNNNNNNWWNWWWWWWWWWWWWWWWWMMMKDDDQQQKKKKK
    MMMMMMMMMMMMMMWWWWWWWWWNNNNHHHHHKNKKQDXSSSSSXSXDDDQKKKKKHHNHNNNNNNWWWWWWWWWWWWWWWWWWMMNSXXXXDDDDDQQQ
    MMMMMMMMMMMMWWWWWWWWWNNNHHHHKKQQQDDHHKKQDDXXDXDDDDQKKKKKHHHNNNNNNNWNWWWWWWWWWWWWWWWMSY566SSSSXXXDDDD
    MMMMMMMMMMWWWWWWWWWNNNNHHHKKQQQDDXXSSXHHKKQQQQDQQQKKKKKKHHHHNHNNNNWNWWWWWWWWWWWWWMtjtJJY5566SSSXXXDD
    MMMMMMMMMWWMWWWWNNNNNHHHHKKQQDDDXXSS655YDHHHHKKKKKKKKKHHHHNHNNNNNWWWWWWWWWWWWWMciccjjttJJY5566SSSXXX
    MMMMMMMMMMWWWWWWWNNNNHHHKKKQQDDDXSSS6655YYJYHNNHHHHHHHHNHNNNNNNWNWWWWWWWWWWQiiiccccjjtttJJYY566SSSXX
    MMMMMMMMMMWWWWWWNNNNNHHHHKKQQQDDXXXSSSS66S655YJtjccii===============iiiiccccjjjtjjjtttJJJY55566SSSXX
    MMMMMMMMMMMMMWWWWWWWNNNNNHHHHKKKQQDDDQDDDXSSSSS6665YYJJJttttttjttjjjtjtjttttttJJJYYYYY55566SSSSXXXDD
    MMMMMMMMMMMMMMMMWWWWWWWWNNNNHHHHHKQKKKQQDDDDDXXXXSSSS6655YYYJJJJJJJJJJJJJJJYYJYY55566666SSSSXXDDDDDD
    MMMMMMMMMMMMMMMMMMWWWWWWWWWNNNNNHHHHHHKKKKKKQQQQDDDDDDXXXXSSSSS666665656566666S6SSSXXDDXXDDDDDDDDQQQ
    MMMMMMMMMMMMMMMMMMMMMMMWWWWWWWWWNWNNNNNHHHHHHKKKKKKQKQQQQQQDDDDDDDDDDDDDDDDDDDDDDDQQQQKKQQQKKKKKKKKH
    MMMMMMMMMMMMMMMMMMMMMMMMMMMWWWWWWWWWWWWWNNNNNNHHHHHHHHHHHHHKKHKHHHHKHHHHHHHHHHHHHHHHNNNNNHHNNNNNNHNN
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWMMMWMWWWWWWWWWWWWNNNNNNNNNNNNNNNNNNNNNNNNNNWNWWWWWWWWWWWWWWWWWWWWWWWW
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWWMWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWMWWMWMWWMWWWWWWW
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMWWWWWWWWMWWWWWWWWWWMMMWMWMWMMMMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM