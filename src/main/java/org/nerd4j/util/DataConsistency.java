/*
 * #%L
 * Nerd4j Utils
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

import java.util.Collection;
import java.util.Map;

import org.nerd4j.exception.DataConsistencyException;

/**
 * Utility class useful to quickly perform data consistency checks.
 * 
 * @author Nerd4j Team
 * @deprecated use {@link org.nerd4j.util.Require} instead.
 * @since 1.1.1
 */
@Deprecated
public class DataConsistency
{

	/**
	 * Ensures that the value of the given property is not {@code null}.
	 * 
	 * @param name  name of the property to check.
	 * @param value value of the property to check.
	 * @throws DataConsistencyException if the check fails.
	 */
	public static void checkIfNotNull( String name, Object value )
	{
		
		if( value == null )
			throw new DataConsistencyException( "Invalid argument, field " + name + " must be not null" );
			
	}

	/**
	 * Ensures that the given text value is not {@code null} and not empty.
	 * 
	 * @param name  name of the property to check.
	 * @param value value of the property to check.
	 * @throws DataConsistencyException if the check fails.
	 */
	public static void checkIfValued( String name, String value )
	{
		
		if( value == null || value.isEmpty() )
			throw new DataConsistencyException( "Invalid argument, field " + name + " must be valued, but was " + value );
		
	}
	
	/**
	 * Ensures that the given {@link Collection} is not {@code null} and not empty.
	 * 
	 * @param name  name of the property to check.
	 * @param value value of the property to check.
	 * @throws DataConsistencyException if the check fails.
	 */
	public static void checkIfValued( String name, Collection<?> value )
	{
		
		if( value == null || value.isEmpty() )
			throw new DataConsistencyException( "Invalid argument, field " + name + " must be not null and have at least one element, but was " + value );
		
	}
	
	/**
	 * Ensures that the given {@link Map} is not {@code null} and not empty.
	 * 
	 * @param name  name of the property to check.
	 * @param value value of the property to check.
	 * @throws DataConsistencyException if the check fails.
	 */
	public static void checkIfValued( String name, Map<?,?> value )
	{
		
		if( value == null || value.isEmpty() )
			throw new DataConsistencyException( "Invalid argument, field " + name + " must be not null and have at least one element, but was " + value );
		
	}
	
	
	/**
	 * Ensures that the given value is strict positive.
	 * 
	 * @param name  name of the property to check.
	 * @param value value of the property to check.
	 * @throws DataConsistencyException if the check fails.
	 */
	public static  void checkIfStrictPositive( String name, long value )
	{
		
		if( value <= 0L )
			throw new DataConsistencyException( "Invalid argument, field " + name + " must be positive, but was " + value );
		
	}
	
	
	/**
	 * Ensures that the given value is strict positive.
	 * 
	 * @param name  name of the property to check.
	 * @param value value of the property to check.
	 * @throws DataConsistencyException if the check fails.
	 */
	public static  void checkIfStrictPositive( String name, double value )
	{
		
		if( value <= 0D )
			throw new DataConsistencyException( "Invalid argument, field " + name + " must be positive, but was " + value );
		
	}
	
	
	/**
	 * Ensures the given proposition to be {@code true}.
	 * 
	 * @param proposition  the proposition to check.
	 * @param value        value of the proposition.
	 * @throws DataConsistencyException if the check fails.
	 */
	public static void checkIfTrue( String proposition, boolean value )
	{
		
		if( ! value )
			throw new DataConsistencyException( "The proposition '" + proposition +"' has to be true" );
		
	}
	
	
	/**
	 * Ensures the given proposition to be {@code false}.
	 * 
	 * @param proposition  the proposition to check.
	 * @param value        value of the proposition.
	 * @throws DataConsistencyException if the check fails.
	 */
	public static void checkIfFalse( String proposition, boolean value )
	{
		
		if( value )
			throw new DataConsistencyException( "The proposition '" + proposition +"' has to be false" );
		
	}
	
}
