package org.nerd4j.lang.array;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.nerd4j.util.Require;

/**
 * Abstract implementation of {@link Iterator} common
 * to all array iterators.
 * 
 * @param <Type> the type returned by the {@link Iterator}.
 * 
 * @author Nerd4j Team
 */
abstract class AbstractArrayIterator<Type> implements Iterator<Type>
{
	
	
	/** Index immediately past the last index to cover. */
	private final int endIndex;
	
	/** The current index. */
	private int currentIndex;
	
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover.
	 * @param arrayLength    the length of the array.
	 */
	public AbstractArrayIterator( int startInclusive, int endExclusive, int arrayLength )
	{
		
		super();
		
		if( startInclusive < 0 || startInclusive > arrayLength )
			throw new ArrayIndexOutOfBoundsException( startInclusive );
		
		if( endExclusive < 0 || endExclusive > arrayLength )
			throw new ArrayIndexOutOfBoundsException( endExclusive );
			
		Require.toHold( startInclusive <= endExclusive,
				() -> "The start index (" + startInclusive + ") must be less or equals to the end index (" + endExclusive + ")" );
		
		this.endIndex     = endExclusive; 
		this.currentIndex = startInclusive;
		
	}
	
	
	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext()
	{	
		
		return currentIndex < endIndex;
		
	}

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Type next()
	{
		
		if( ! hasNext() )
			throw new NoSuchElementException();
		
		return get( currentIndex++ );
		
	}

	
	/* ***************** */
	/*  EXTENSION HOOKS  */
	/* ***************** */

	
	/**
	 * Returns the element in given position of the array.
	 * 
	 * @param index position of the element to retrieve.
	 * @return element in the given position.
	 */
	abstract Type get( int index );
		
}
