/*
 * #%L
 * Nerd4j Core
 * %%
 * Copyright (C) 2011 - 2013 Nerd4j
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as 
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */
package org.nerd4j.util;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Utility class with the aim of checking equality between objects.
 *
 * @author Nerd4j Team
 */
public class Equalizer
{

    /**
	 * Tells if the two given objects are comparable,
	 * i.e. tells if it's the same instance, or if
	 * they are both null and eventually if they
	 * are instances of the same class.
	 * <p>
	 *  <b>Note</b>
	 *  Those checks are common in all the implementations
	 *  of the method {@link #equals(Object)} so this
	 *  code can be used to avoid writing the same checks
	 *  all the time.
	 * </p>
	 * <p>
	 *  A common use case is to implement the method {@link #equals(Object)}
	 *  using this method like this {@code areComparable(this, other)} to
	 *  check if the istances are comparabe and eventually use the metod
	 *  {@link Equalizer#equals(Object, Object)} or
	 *  {@link Equalizer#deepEquals(Object, Object)} to check
	 *  the properties.
	 * </p>
	 * 
	 * @param fst first object to check.
	 * @param snd second object to check.
	 * @return {@code true} if the two objects are equal.
	 */
    public static boolean areComparable( Object fst, Object snd )
    {
    	
    	/* This check works also for fst == snd == null. */
    	if( fst == snd ) return true;
    	
    	/* Checks if (fst == null && snd != null) || (fst != null && snd == null). */
    	if( fst == null ^ snd == null ) return false;
    	
    	/*
    	 * If we are at this point then the given
    	 * objects are not the same instance and
    	 * both not null.
    	 */
    	return fst.getClass() == snd.getClass();

    }
    
    
    /**
     * Tells if the two given objects are equal.
     * <p>
     *  <b>Note</b>
     *  This method checks equality by invoking
     *  the {@link #equals(Object)} method on the
     *  given objects, so it can't be used inside
     *  a {@link #equals(Object)} itself.
     * </p>
     * 
     * @param fst first object to check.
     * @param snd second object to check.
     * @return {@code true} if the two objects are equal.
     */
    public static boolean equals( Object fst, Object snd )
    {
    	
    	if( ! areComparable(fst, snd) )
    		return false;
    	
    	/*
    	 * At this point fst and snd can be both null
    	 * or both not null and of the same class.
    	 */
    	if( fst == null ) return true;
    	
    	if( fst.getClass().isArray() )
    		return equalsArray( fst, snd );
    	
    	if( fst instanceof Iterable )
    		return equalsIterable( (Iterable<?>) fst, (Iterable<?>) snd );
    	
    	return fst.equals( snd );
    	
    }
    
    
    /**
	 * Tells if the two given list of objects are equal.
	 * <p>
	 *  The list of arguments must be in the form:
	 *  {@code fstA, sndA, fstB, sndB, ...} otherwise
	 *  the method will not be able to perform the
	 *  check properly.
	 * </p>
	 * 
	 * @param fst first object to check.
	 * @param snd second object to check.
	 * @return {@code true} if the given objects are two by two equal.
	 * @throws IllegalArgumentException If the number of arguments is inconsistent.
	 */
    public static boolean equals( Object fst, Object snd, Object... others )
    {

   		if( others != null && others.length % 2 == 1 )
    		throw new IllegalArgumentException( "Number of arguments must be even but was " + (others.length + 2) );
    	
    	if( ! equals(fst, snd) ) return false;
    	
    	if( others != null && others.length > 0 )
    		for( int i = 0; i < others.length; ++i )
    			if( ! equals(others[i], others[++i]) )
    				return false;

    	return true;

    }

    
    /**
     * Tells if the two given objects are equal.
     * <p>
     *  If the given objects are {@link Iterable} or
     *  they are arrays this method iterates the
     *  process to each contained element. 
     * </p>
     * 
     * @param fst first object to deep check.
     * @param snd second object to deep check.
     * @return {@code true} if the two objects are deeply equal.
     */
    public static boolean deepEquals( Object fst, Object snd )
    {
    	
    	if( ! areComparable(fst, snd) )
    		return false;
    	
    	/*
    	 * At this point fst and snd can be both null
    	 * or both not null and of the same class.
    	 */
    	if( fst == null ) return true;
    	
    	if( fst.getClass().isArray() )
    		return deepEqualsArray( fst, snd );
    	
    	if( fst instanceof Iterable )
    		return deepEqualsIterable( (Iterable<?>) fst, (Iterable<?>) snd );
    	
    	return fst.equals( snd );
    	
    }
    
    
    /**
     * Tells if the two given objects are equal.
     * <p>
     *  If the given objects are {@link Iterable} or
     *  they are arrays this method iterates the
     *  process to each contained element. 
     * </p>
     * 
     * @param fst first object to deep check.
     * @param snd second object to deep check.
     * @return {@code true} if the two objects are deeply equal.
     */
    public static boolean deepEquals( Object fst, Object snd, Object... others )
    {
    	
    	if( others != null && others.length % 2 == 1 )
    		throw new IllegalArgumentException( "Number of arguments must be even but was " + (others.length + 2) );
    	
    	if( ! deepEquals(fst, snd) ) return false;
    	
    	if( others != null && others.length > 0 )
    		for( int i = 0; i < others.length; ++i )
    			if( ! deepEquals(others[i], others[++i]) )
    				return false;
    	
    	return true;
    	
    }
    
    
    /* ***************** */
    /*  PRIVATE METHODS  */
    /* ***************** */
    
    
    /**
	 * Tells if the two {@link Iterable} are equal.
	 * 
	 * @param fst first {@link Iterable} to check.
	 * @param snd second {@link Iterable} to check.
	 * @return {@code true} if the two {@link Iterable} are equal.
	 */
    private static boolean equalsIterable( Iterable<?> fst, Iterable<?> snd )
    {
    	
    	final Iterator<?> fstIter = fst.iterator();
    	final Iterator<?> sndIter = snd.iterator();
    	
    	Object fstValue, sndValue;
    	while( fstIter.hasNext() )
    	{
    		if( ! sndIter.hasNext() )
    			return false;
    		
    		fstValue = fstIter.next();
    		sndValue = sndIter.next();
    		
    		if( ! equals(fstValue, sndValue) )
    			return false;
    		
    	}
    	
    	return true;
    	
    }
    
    
    /**
	 * Tells if the two arrays are equal.
	 * 
	 * @param fst first array to check.
	 * @param snd second array to check.
	 * @return {@code true} if the two arrays are equal.
	 */
	private static boolean equalsArray( Object fst, Object snd )
	{
		
		if ( fst instanceof Object[] )
			return Arrays.equals( (Object[])fst, (Object[])snd );
		
		else if ( fst instanceof int[] )
			return Arrays.equals( (int[])fst, (int[])snd );
		
		else if ( fst instanceof byte[] )
			return Arrays.equals( (byte[])fst, (byte[])snd );
		
		else if ( fst instanceof long[] )
			return Arrays.equals( (long[])fst, (long[])snd );
		
		else if ( fst instanceof short[] )
			return Arrays.equals( (short[])fst, (short[])snd );
		
		else if ( fst instanceof boolean[] )
			return Arrays.equals( (boolean[])fst, (boolean[])snd );
		
		else if ( fst instanceof float[] )
			return Arrays.equals( (float[])fst, (float[])snd );
		
		else if ( fst instanceof double[] )
			return Arrays.equals( (double[])fst, (double[])snd );
		
		else if ( fst instanceof char[] )
			return Arrays.equals( (char[])fst, (char[])snd );	
		
		return false;
		
	}
	
	
	/**
	 * Tells if the two {@link Iterable} are equal
	 * and iterates the process for each value.
	 * 
	 * @param fst first {@link Iterable} to check.
	 * @param snd second {@link Iterable} to check.
	 * @return {@code true} if the two {@link Iterable} are equal.
	 */
	private static boolean deepEqualsIterable( Iterable<?> fst, Iterable<?> snd )
	{
		
		final Iterator<?> fstIter = fst.iterator();
		final Iterator<?> sndIter = snd.iterator();
		
		Object fstValue, sndValue;
		while( fstIter.hasNext() )
		{
			if( ! sndIter.hasNext() )
				return false;
			
			fstValue = fstIter.next();
			sndValue = sndIter.next();
			
			if( ! deepEquals(fstValue, sndValue) )
				return false;
			
		}
		
		return true;
		
	}
	
	
	/**
	 * Tells if the two arrays are equal
	 * and iterates the process for each
	 * couple of values in the arrays.
	 * 
	 * @param fst first array to deep check.
	 * @param snd second array to deep check.
	 * @return {@code true} if the two arrays are deeply equal.
	 */
	private static boolean deepEqualsArray( Object fst, Object snd )
	{
		
		if ( fst instanceof Object[] )
		{
		
			final Object[] fstArray = (Object[]) fst;
			final Object[] sndArray = (Object[]) snd;
			
			if( fstArray.length != sndArray.length ) return false;
			
			for( int i = 0; i < fstArray.length; ++i )
				if( ! deepEquals(fstArray[i], sndArray[i]) )
					return false;
			
			return true;
			
		}
		
		else if ( fst instanceof int[] )
			return Arrays.equals( (int[])fst, (int[])snd );
		
		else if ( fst instanceof byte[] )
			return Arrays.equals( (byte[])fst, (byte[])snd );
		
		else if ( fst instanceof long[] )
			return Arrays.equals( (long[])fst, (long[])snd );
		
		else if ( fst instanceof short[] )
			return Arrays.equals( (short[])fst, (short[])snd );
		
		else if ( fst instanceof boolean[] )
			return Arrays.equals( (boolean[])fst, (boolean[])snd );
		
		else if ( fst instanceof float[] )
			return Arrays.equals( (float[])fst, (float[])snd );
		
		else if ( fst instanceof double[] )
			return Arrays.equals( (double[])fst, (double[])snd );
		
		else if ( fst instanceof char[] )
			return Arrays.equals( (char[])fst, (char[])snd );	
		
		return false;
		
	}
    
}
