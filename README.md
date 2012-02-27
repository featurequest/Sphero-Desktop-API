# Development Quick Start Guide

## Overview

## Compatibility
    Works in Windows, MAC and Linux (In linux an extra Bluecove library named Bluecove GPL located at
    [Bluecove GPL](http://bluecove.org/bluecove-gpl/) may be needed to get the API working)

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
The API is similar to that of the original Orbotix Sphero API with some modifications to support connecting to multiple
Sphero devices simultaniously and sending individual commands to these. There is examples in the se.nicklasgavelin.sphero.example
package that will show a quick example of how to use the API.

To connect to a Sphero device you may either perform a bluetooth device search as shown in one of the examples or you can use
the direct bluetooth address to connect to it directly without having to perform a search. Although the second method will prevent
some commands to be performed correctly and the bluetooth name will not be retrieved properly.

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

# Contact & Suggestions
If you think this README file doesn't cover all it should (as much as a basic readme file should) or if you
have some suggestions for improvements please send me a mail at nicklas.gavelin@gmail.com.

# Recognition
This Sphero API port was developed during my thesis work at Lule� University of Technology, http://www.ltu.se and
is currently actively being worked on (committing all recent updates).

# Versions
There is no guarantee that packages will maintain their naming standard or that classes will be left intact. Although I'm trying to keep
the impact on previous versions as small as possible regarding compatibility.

# License
Read the LICENSE file for more information :)