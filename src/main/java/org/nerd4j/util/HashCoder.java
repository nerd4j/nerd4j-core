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

/**
 * Utility class with the aim of producing suitable hash codes.
 *
 * @author Nerd4j Team
 */
public class HashCoder
{

    /**
	 * Creates an hash code based on a given prime number
	 * and a given integer base.
	 * 
	 * @param prime  a number supposed to be prime.
	 * @param base   an base number to combine with the prime.
	 * @return the resulting hash code.
	 */
    public static int hashCode( int prime, int base )
    {
    	
    	final int actual = prime == 0 ? 79 : prime; 
        return base == 0 ? actual : actual * base;

    }
    
    
    /**
     * Creates an hash code based on a given prime number,
     * the given integer base and a given integer offset.
     * 
     * @param prime  a number supposed to be prime.
     * @param base   a base number to combine with the prime.
     * @param offset an additional offset.
     * @return the resulting hash code.
     */
    public static int hashCode( int prime, int base, int offset )
    {
    	
    	return hashCode( prime, base ) + offset;
    	
    }
    

    /**
	 * Creates an hash code based on a given prime number
	 * and a given base object.
	 * <p>
	 * It works in the same way as {@link HashCoder#hashCode(int,int)}
	 * using the {@link Object#hashCode()} method of the given object
	 * as base.
	 * 
	 * @param prime a number supposed to be prime.
	 * @param base  an object to combine with the prime.
	 * @return the resulting hash code.
	 */
    public static int hashCode( int prime, Object base )
    {

    	if( base == null )
    		return hashCode( prime, 0 );
    	
    	if( base instanceof Iterable )
    		return hashIterable( prime, (Iterable<?>) base );
    	
    	if( base.getClass().isArray() )
    		return hashCode( prime, hashArray(base) );
    		
    	return hashCode( prime, base.hashCode() );

    }
    
    
    /**
	 * Creates an hash code based on a given prime number,
	 * a base object and an arbitrary long series of offset objects.
	 * <p>
	 * It creates an hash code that is combination of the
	 * provided objects and the prime number.
	 * 
	 * @param prime   a number supposed to be prime.
	 * @param base    an object to combine with the prime.
	 * @param offsets the other offsets to combine with the prime.
	 * @return the resulting hash code.
	 */
    public static int hashCode( int prime, Object base, Object... offsets )
    {

    	int result = hashCode( prime, base );
    	for( Object offset : offsets )
    		result += hashCode( result, offset );
    	
        return result;

    }
    
    
    /* ***************** */
    /*  PRIVATE METHODS  */
    /* ***************** */
    
    
    /**
     * Creates an hash code based on a given prime number
     * and a given iterable object.
     * <p>
     * The returned value is based on the contents of the specified iterable object.
     * This method interpolates the hash codes of each element in the iterable object.
     * 
     * @param prime a number supposed to be prime.
     * @param iterable  an iterable object to combine with the prime.
     * @return the resulting hash code.
     */
    private static int hashIterable( int prime, Iterable<?> iterable )
    {
    	
    	int result = 0;
    	for( Object value : iterable )
    		result += hashCode( result, value );
    		
    	return result;
    	
    }
    
    
    /**
	 * Returns a hash code based on the "deep contents" of the given array.
	 * It build the hash code based on each element of the given array.
	 * 
	 * @param array the array to compute the hash code for.
	 * @return the "deep contents" hash code.
	 */
	private static int hashArray( Object array )
	{
		
		// This case handles also multi-dimensional array of natives.
		if ( array instanceof Object[] )
			return Arrays.deepHashCode( (Object[]) array );
		
		else if ( array instanceof int[] )
			return Arrays.hashCode( (int[]) array );
		
		else if ( array instanceof byte[] )
			return Arrays.hashCode( (byte[]) array );
		
		else if ( array instanceof long[] )
			return Arrays.hashCode( (long[]) array );
		
		else if ( array instanceof short[] )
			return Arrays.hashCode( (short[]) array );
		
		else if ( array instanceof boolean[] )
			return Arrays.hashCode( (boolean[]) array );
		
		else if ( array instanceof float[] )
			return Arrays.hashCode( (float[]) array );
		
		else if ( array instanceof double[] )
			return Arrays.hashCode( (double[]) array );
		
		else if ( array instanceof char[] )
			return Arrays.hashCode( (char[]) array );	
		
		return 0;
		
	}
    
}
