package se.nicklasgavelin.util;

/**
 * A byte array buffer that stores bytes and gives the ability to append new
 * bytes easily
 *
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of
 * Technology
 */
public class ByteArrayBuffer
{
    // Internal storage buffer
    private byte[] buffer;
    
    // Current length
    private int length;


    /**
     * Create a byte array buffer with a given capacity.
     * The buffer will NOT be able to go above the given capacity
     *
     * @param capacity The capacity of the buffer
     */
    public ByteArrayBuffer( int capacity )
    {
        this.buffer = new byte[ capacity ];
        this.length = 0;
    }


    /**
     * Append a byte array to the buffer
     *
     * @param b The byte array to append
     */
    public void append( byte[] b )
    {
        for ( int i = 0; i < b.length; i++ )
            this.append( b[i] );
    }


    /**
     * Append a single byte to the buffer
     *
     * @param b The byte to append
     */
    public void append( byte b )
    {
        // Check if we have any more space to use
        if ( this.length < this.buffer.length )
            this.buffer[ this.length++] = b;
    }


    /**
     * Appends length bytes to this buffer from the given source array starting
     * at index offset.
     *
     * @param b      The byte array to append
     * @param offset The offset to start from
     * @param length The length to append
     */
    public void append( byte[] b, int offset, int length )
    {
        for ( int i = offset; i < length; i++ )
            this.append( b[i] );
    }


    /**
     * Returns the converted buffer as a byte array
     */
    public byte[] toByteArray()
    {
        // Create a new byte array with our current length
        byte[] ret = new byte[ this.length ];

        // Copy our values to our new correctly sized array
        System.arraycopy( this.buffer, 0, ret, 0, this.length );

        // Return the newly created array
        return ret;
    }


    /**
     * Clear the data in the buffer,
     * will only set the length to 0
     */
    public void clear()
    {
        this.length = 0;
    }


    /**
     * Returns the byte located at a specific index
     *
     * @param index The index to return
     *
     * @return The byte at the given index (null if the index is outside the
     * available range)
     */
    public byte byteAt( int index )
    {
        return (index < this.length ? this.buffer[ index] : null);
    }


    /**
     * Returns the index of the first occurrence of byte b
     *
     * @param b The byte to check index for
     *
     * @return Returns the index of the given byte or -1 if the byte isn't in
     * the buffer
     */
    public int indexOf( byte b )
    {
        // Go through all bytes
        for ( int i = 0; i < this.length; i++ )
            if ( this.buffer[i] == b )
                return i;
        return -1;
    }


    /**
     * Returns true if the buffer is empty, that is if the length is 0
     *
     * @return True if empty, false otherwise
     */
    public boolean isEmpty()
    {
        return (this.length == 0);
    }


    public boolean isFull()
    {
        return (this.length == this.buffer.length);
    }


    /**
     * Set the new capacity of the byte array
     *
     * @param capacity The new capacity
     */
    public void setCapacity( int capacity )
    {
        // Create our new buffer
        byte[] newBuffer = new byte[ capacity ];

        // Copy our old data to our new buffer
        System.arraycopy( this.buffer, 0, newBuffer, 0, this.buffer.length );

        // Replace our old buffer with our new one
        this.buffer = newBuffer;
    }


    /**
     * Returns the total capacity available for storage
     */
    public int capacity()
    {
        return this.buffer.length;
    }


    /**
     * Returns the current amount of data in the array
     *
     * @return The current amount of bytes
     */
    public int size()
    {
        return this.length;
    }


    /**
     * Returns the current amount of data in the array
     *
     * @return The current amount of bytes
     */
    public int length()
    {
        return this.length;
    }
}
