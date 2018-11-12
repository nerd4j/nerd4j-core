package org.nerd4j.lang.array;

import java.util.Iterator;

import org.nerd4j.util.Require;

/**
 * Implementation of {@link Iterator} that iterates
 * over an array of {@code byte}.
 * 
 * @author Nerd4j Team
 */
public class ByteArrayIterator extends AbstractArrayIterator<Byte>
{

	
	/** The source array to iterate. */
	private final byte[] array;
	
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 */
	public ByteArrayIterator( byte[] array, int startInclusive, int endExclusive )
	{
		
		super( startInclusive, endExclusive, Require.nonNull( array, "The source array is mandatory" ).length );
		
		this.array = array;
		
	}
	
	
	/* ***************** */
	/*  EXTENSION HOOKS  */
	/* ***************** */

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	Byte get( int index )
	{
		
		return array[index];
		
	}

}
