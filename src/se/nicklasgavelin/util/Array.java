package se.nicklasgavelin.util;

import java.util.Arrays;

/**
 * Array utilities
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 */
public class Array
{
	public static String stringify( byte[] data )
	{
		String s = "";
		for( byte b : data )
			s += b + " ";
		s = s.substring( 0, s.length() - 1 );

		return s;
	}

	/**
	 * Concatenate two arrays together
	 * 
	 * @param first The first array
	 * @param second The second array
	 * 
	 * @return The concatenated arrays
	 */
	public static byte[] concat( byte[] first, byte[] second )
	{
		byte[] result = Arrays.copyOf( first, first.length + second.length );
		System.arraycopy( second, 0, result, first.length, second.length );

		return result;
	}

	/**
	 * Concatenate two arrays together
	 * 
	 * @param <T> The class
	 * @param first The first array
	 * @param second The second array
	 * 
	 * @return The concatenated arrays
	 */
	public static <T> T[] concat( T[] first, T[] second )
	{
		T[] result = Arrays.copyOf( first, first.length + second.length );
		System.arraycopy( second, 0, result, first.length, second.length );

		return result;
	}

	/**
	 * Concatenate multiple arrays
	 * 
	 * @param first The first array
	 * @param rest The rest of the arrays
	 * 
	 * @return The concatenated arrays
	 */
	public static byte[] concatAll( byte[] first, byte[]... rest )
	{
		int totalLength = first.length;

		for( byte[] array : rest )
			totalLength += array.length;

		byte[] result = Arrays.copyOf( first, totalLength );

		int offset = first.length;
		for( byte[] array : rest )
		{
			System.arraycopy( array, 0, result, offset, array.length );
			offset += array.length;
		}

		return result;
	}

	/**
	 * Concatenate multiple arrays
	 * 
	 * @param <T> The class of the arrays
	 * @param first The first array
	 * @param rest The rest of the arrays
	 * 
	 * @return The concatenated arrays
	 */
	public static <T> T[] concatAll( T[] first, T[]... rest )
	{
		int totalLength = first.length;

		for( T[] array : rest )
			totalLength += array.length;

		T[] result = Arrays.copyOf( first, totalLength );

		int offset = first.length;
		for( T[] array : rest )
		{
			System.arraycopy( array, 0, result, offset, array.length );
			offset += array.length;
		}

		return result;
	}
}
