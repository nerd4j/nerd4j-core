/*
 * #%L
 * Nerd4j Core
 * %%
 * Copyright (C) 2011 - 2019 Nerd4j
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

import java.util.Collection;
import java.util.Map;

/**
 * This class consists of {@code static} utility methods for operating
 * {@code boolean} checks on objects.
 * 
 * <p>
 * This class provides {@code null-safe} operations for the most
 * common checks over {@link Object}, {@link String} and {@link Collection}.
 * 
 * <p>
 * The main aim of this class is to provide an easy and readable way
 * to perform the most common checks in conditional statements and in
 * the filter methods of {@link java.util.stream.Stream}  and
 * {@link java.util.Optional}.
 *  
 * <ul>
 *  <li><b>NULL</b>
 *      - checks if an Object is null</li>
 *  <li><b>empty</b>
 *      - checks if a String, a Collection, a Map or an array is empty</li>
 *  <li><b>blank</b>
 *      - checks if a String contains only characters satisfying {@link Character#isWhitespace(char)}</li>
 * </ul>
 * 
 * @author Nerd4j Team
 * @since 1.1.1
 */
public class Is
{
	
	
	/**
	 * This class is static
	 * so the constructor is
	 * definet private.
	 */
	private Is()
	{
		
		super();
		
	}
	

	/* ****************** */
	/*  CHECKS ON OBJECT  */
	/* ****************** */

	
	/**
	 * Checks if the given value is {@code null}.
	 * <p>
	 * Since {@code null} is a keyword and therefore
	 * cannot be used as a method name, we choose
	 * to name the method in upper case.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null}.
	 */
	public static boolean NULL( Object value )
	{
		
		return value == null;
		
	}
	
	
	/* ****************** */
	/*  CHECKS ON STRING  */
	/* ****************** */
	
	
	/**
	 * Checks if the given value is {@code null} or empty.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null} or empty.
	 */
	public static boolean empty( String value )
	{
		
		return value == null || value.isEmpty();
		
	}
	
	/**
	 * Checks if the given value is {@code null}, empty
	 * or contains only characters that satisfies the
	 * check performed by {@link Character#isWhitespace(char)}.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null}, empty
	 *         or contains only white spaces.
	 */
	public static boolean blank( String value )
	{
		
	    if( empty(value) ) return true;
	    
	    final int length = value.length();
        for( int i = 0; i < length; i++ )
        	if( ! Character.isWhitespace(value.charAt(i)) )
        		return false;
        	            
        return true;
		
	}
	
	
	/* ********************** */
	/*  CHECKS ON COLLECTION  */
	/* ********************** */
	
	
	/**
	 * Checks if the given value is {@code null} or empty.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null} or empty.
	 */
	public static boolean empty( Collection<?> value )
	{
		
		return value == null || value.isEmpty();
		
	}
	
	
	/* *************** */
	/*  CHECKS ON MAP  */
	/* *************** */
	
	
	/**
	 * Checks if the given value is {@code null} or empty.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null} or empty.
	 */
	public static boolean empty( Map<?,?> value )
	{
		
		return value == null || value.isEmpty();
		
	}
	
	
	/* ***************** */
	/*  CHECKS ON ARRAY  */
	/* ***************** */
	
	
	/**
	 * Checks if the given value is {@code null} or empty.
	 * 
	 * @param <V>   - type of the elements in the array.
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null} or empty.
	 */
	public static <V> boolean empty( V[] value )
	{
		
		return value == null || value.length == 0;
		
	}
	
	/**
	 * Checks if the given value is {@code null} or empty.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null} or empty.
	 */
	public static boolean empty( boolean[] value )
	{
		
		return value == null || value.length == 0;
		
	}
	
	/**
	 * Checks if the given value is {@code null} or empty.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null} or empty.
	 */
	public static boolean empty( char[] value )
	{
		
		return value == null || value.length == 0;
		
	}
	
	/**
	 * Checks if the given value is {@code null} or empty.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null} or empty.
	 */
	public static boolean empty( byte[] value )
	{
		
		return value == null || value.length == 0;
		
	}
	
	/**
	 * Checks if the given value is {@code null} or empty.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null} or empty.
	 */
	public static boolean empty( short[] value )
	{
		
		return value == null || value.length == 0;
		
	}
	
	/**
	 * Checks if the given value is {@code null} or empty.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null} or empty.
	 */
	public static boolean empty( int[] value )
	{
		
		return value == null || value.length == 0;
		
	}
	
	/**
	 * Checks if the given value is {@code null} or empty.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null} or empty.
	 */
	public static boolean empty( float[] value )
	{
		
		return value == null || value.length == 0;
		
	}
	
	/**
	 * Checks if the given value is {@code null} or empty.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null} or empty.
	 */
	public static boolean empty( long[] value )
	{
		
		return value == null || value.length == 0;
		
	}
	
	/**
	 * Checks if the given value is {@code null} or empty.
	 * 
	 * @param value - the value to check.
	 * @return {@code true} if the value is {@code null} or empty.
	 */
	public static boolean empty( double[] value )
	{
		
		return value == null || value.length == 0;
		
	}
	
}
