package se.nicklasgavelin.util;

/**
 * Pair for holding two types of objects
 * 
 * @author Nicklas Gavelin, nicklas.gavelin@gmail.com, Luleå University of Technology
 * @param <X> The first type of object
 * @param <Y> The second type of object
 */
public class Pair<X, Y>
{
	private X x;
	private Y y;

	/**
	 * Create a pair from two objects
	 * 
	 * @param x The first object
	 * @param y The second object
	 */
	public Pair( X x, Y y )
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the first object
	 * 
	 * @return The first object
	 */
	public X getFirst()
	{
		return this.x;
	}

	/**
	 * Returns the second object
	 * 
	 * @return The second object
	 */
	public Y getSecond()
	{
		return this.y;
	}
}
