package se.nicklasgavelin.bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import javax.microedition.io.StreamConnection;

/**
 * This originated from the Mobile Processing project -
 * http://mobile.processing.org
 * 
 * Ported to Processing by extrapixel, http://www.extrapixel.ch/bluetooth/
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * @author Francis Li
 * @author extrapixel
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 *         (Modifier)
 */
public class BluetoothConnection
{
	private StreamConnection con;
	private DataInputStream is;
	private DataOutputStream os;

	/**
	 * The internal Bluetooth device that are used for communication
	 */
	protected BluetoothDevice device;

	/**
	 * Create a Bluetooth connection from a streamConnection
	 * 
	 * @param con The StreamConnection
	 */
	protected BluetoothConnection( StreamConnection con )
	{
		this.con = con;
	}

	/**
	 * Open upp the available input and output streams
	 * 
	 * @throws IOException If the opening failed
	 */
	public void open() throws IOException
	{
		os = con.openDataOutputStream();
		is = con.openDataInputStream();
	}

	/**
	 * Close all active stream
	 */
	public void stop()
	{
		try
		{
			os.close();
		}
		catch( IOException ioe )
		{
		}

		try
		{
			is.close();
		}
		catch( IOException ioe )
		{
		}

		try
		{
			con.close();
		}
		catch( IOException ioe )
		{
		}
	}

	/**
	 * Returns an estimate of the number of bytes that can be read (or skipped
	 * over) from this input stream without blocking by the next caller of a
	 * method for this input stream. The next caller might be the same thread or
	 * another thread. A single read or skip of this many bytes will not block,
	 * but may read or skip fewer bytes.
	 * 
	 * @return an estimate of the number of bytes that can be read (or skipped
	 *         over) from this input stream without blocking.
	 * 
	 * @throws IOException if and I/O error occurs
	 */
	public int available() throws IOException
	{
		return is.available();
	}

	/**
	 * Reads the next byte of data from this input stream. The value byte is
	 * returned as an int in the range 0 to 255. If no byte is available because
	 * the end of the stream has been reached, the value -1 is returned. This
	 * method blocks until input data is available, the end of the stream is
	 * detected, or an exception is thrown. This method simply performs
	 * in.read() and returns the result.
	 * 
	 * @return the next byte of data, or -1 if the end of the stream is reached.
	 * 
	 * @throws IOException if and I/O error occurs
	 */
	public int read() throws IOException
	{
		return is.read();
	}

	/**
	 * Read an amount of bytes
	 * 
	 * @param b The byte buffer to read to
	 * 
	 * @return The number of bytes read or -1 if the end of stream is reached
	 * 
	 * @throws IOException
	 */
	public int read( byte[] b ) throws IOException
	{
		return is.read( b );
	}

	/**
	 * Read a boolean value
	 * 
	 * @throws IOException If failure to read for some stream exception
	 * @return True or false
	 */
	public boolean readBoolean() throws IOException
	{
		return is.readBoolean();
	}

	/**
	 * Read a single char
	 * 
	 * @return The char read (the next two bytes)
	 * 
	 * @throws IOException
	 */
	public char readChar() throws IOException
	{
		return is.readChar();
	}

	/**
	 * Read a number of bytes
	 * 
	 * @param b The bytes read
	 * 
	 * @throws IOException
	 */
	public void readBytes( byte[] b ) throws IOException
	{
		readBytes( b, 0, b.length );
	}

	/**
	 * Read bytes to the given array
	 * 
	 * @param b The byte array to read to
	 * @param offset The offset to read
	 * @param length The length to read
	 * 
	 * @throws IOException
	 */
	public void readBytes( byte[] b, int offset, int length ) throws IOException
	{
		is.readFully( b, offset, length );
	}

	/**
	 * @see java.io.DataInputStream#readInt()
	 */
	public int readInt() throws IOException
	{
		return is.readInt();
	}

	/**
	 * @see java.io.DataInputStream#readUTF()
	 */
	public String readUTF() throws IOException
	{
		return is.readUTF();
	}

	/**
	 * @see java.io.DataInputStream#skipBytes(int)
	 */
	public int skipBytes( int bytes ) throws IOException
	{
		return is.skipBytes( bytes );
	}

	/**
	 * @see java.io.DataOutputStream#write(int)
	 */
	public void write( int data ) throws IOException
	{
		os.write( data );
	}

	/**
	 * @see java.io.DataOutputStream#write(byte[])
	 */
	public void write( byte[] data ) throws IOException
	{
		os.write( data );
	}

	/**
	 * @see java.io.DataOutputStream#writeBoolean(boolean)
	 */
	public void writeBoolean( boolean v ) throws IOException
	{
		os.writeBoolean( v );
	}

	/**
	 * @see java.io.DataOutputStream#writeBytes(String)
	 */
	public void writeBytes( String s ) throws IOException
	{
		os.write( s.getBytes() );
	}

	/**
	 * @see java.io.DataOutputStream#writeChar(int)
	 */
	public void writeChar( int v ) throws IOException
	{
		os.writeChar( v );
	}

	/**
	 * @see java.io.DataOutputStream#writeInt(int)
	 */
	public void writeInt( int v ) throws IOException
	{
		os.writeInt( v );
	}

	/**
	 * @see java.io.DataOutputStream#writeUTF(String)
	 */
	public void writeUTF( String s ) throws IOException
	{
		os.writeUTF( s );
	}

	/**
	 * @see java.io.DataOutputStream#flush()
	 */
	public void flush() throws IOException
	{
		os.flush();
	}
}
