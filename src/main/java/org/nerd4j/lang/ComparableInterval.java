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
package org.nerd4j.lang;


/**
 * Generic implementation of the {@link Interval} interface
 * that handles any type of {@link Comparable} data.
 * 
 * <p>
 *  If for a specific implementation of {@link Comparable}
 *  the {@link Comparable#compareTo(Object)} method is too
 *  heavy it is recommended to create an ad hoc extension
 *  of the {@link AbstractInterval} class instead of using
 *  this one.
 * </p>
 * 
 * @author Nerd4j Team
 */
public class ComparableInterval<T extends Comparable<T>> extends AbstractInterval<T>
{

	/** Default Serial Version UID. */
	private static final long serialVersionUID = 1L;
	

	/**
	 * Creates a new {@link Interval} with the given {@code begin} and {@code end} values.
	 * 
	 * @param begin the begin of the interval.
	 * @param end   the end of the interval.
	 * 
	 * @throws NullPointerException if one of the given values is {@code null}.
	 * @throws IllegalArgumentException if the {@code begin} is greater or equal to the {@code end}.
	 */
	public ComparableInterval( T begin, T end )
	{
		
		super( begin, end );
		
	}
	
	
	/* ******************* */
    /*  INTERFACE METHODS  */
    /* ******************* */
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isInRange( T value )
	{
		
		return value.compareTo( getBegin() ) >= 0  && value.compareTo( getEnd() ) < 0;
		
	}
	
	
	/* ***************** */
    /*  EXTENSION HOOKS  */
    /* ***************** */
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected boolean isLessThan( T t1, T t2 )
	{
		
		return t1.compareTo( t2 ) < 0;
		
	}
	
}
