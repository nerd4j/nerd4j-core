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
package org.nerd4j.lang;

import org.nerd4j.format.AnnotatedFormattedBean;
import org.nerd4j.format.Formatted;
import org.nerd4j.util.HashCoder;

/**
 * Base abstract implementation of the {@link Interval} interface.
 * 
 * @author Nerd4j Team
 */
public abstract class AbstractInterval<T> extends AnnotatedFormattedBean implements Interval<T>
{

	/** Default Serial Version UID. */
	private static final long serialVersionUID = 1L;
    
	/** The value that defines the begin of the interval. */
    @Formatted
    protected T begin;

    /** The value that defines the end of the interval. */
    @Formatted
    protected T end;
    
    
	/**
	 * Creates a new {@link Interval} with the given {@code begin} and {@code end} values.
	 * 
	 * @param begin the begin of the interval.
	 * @param end   the end of the interval.
	 * 
	 * @throws NullPointerException if one of the given values is {@code null}.
	 * @throws IllegalArgumentException if the {@code begin} is greater or equal to the {@code end}.
	 */
    protected AbstractInterval( T begin, T end )
    {
    	
    	if( begin == null || end == null )
    		throw new NullPointerException( "Both begin and end must not be null" );
    	
    	if ( !isLessThan( begin, end ) )
    		throw new IllegalArgumentException( "Interval begin value must be less than its end" );
    	
    	this.begin = begin;
    	this.end   = end;
    	
    }
    
    
    /* ******************* */
    /*  INTERFACE METHODS  */
    /* ******************* */
    
    
	/**
	 * {@inheritDoc}
	 */
    @Override
	public T getBegin()
	{
		
		return begin;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T getEnd()
	{
		
		return end;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean overlaps( Interval<T> interval )
	{
		
		/*
		 * We assume the begin to be strictly smaller than the end.
		 * The begin value is always included in the interval, and
		 * the end value is always excluded. So we just need to 
		 * check if the begin value of one interval belongs to the
		 * other interval or vice versa.
		 */
		return isInRange( interval.getBegin() ) || interval.isInRange( getBegin() );
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean includes( Interval<T> interval )
	{
		
		final T begin = interval.getBegin();
		final T end   = interval.getEnd();
		
		if ( isInRange( begin ) && (end.equals( getEnd() ) || isInRange( end )) )
			return true;
		
		return false;
		
	}
	
	
	/* **************** */
    /*  STATIC METHODS  */
    /* **************** */
	
	
	/**
	 * Checks if the given value is within the given interval.
	 * 
	 * @param value    the value to check.
	 * @param interval the related interval.
	 * @return {@code true} if the value belongs to the interval;<br/>
	 *         {@code false} otherwise.
	 */
	public static <X> boolean isInRange( X value, Interval<X> interval )
	{
		
		return interval.isInRange( value );
		
	}
	
	/**
	 * Checks if the given intervals have at least one value in common.
	 * 
	 * @param i1 the first interval to check.
	 * @param i2 the second interval to check.
	 * @return {@code true} if there is at least one value in common;<br/>
	 *         {@code false} otherwise.
	 */
	public static <X> boolean overlaps( Interval<X> i1, Interval<X> i2 )
	{
		
		return i1.overlaps( i2 );
		
	}
	
	/**
	 * Checks if the second interval is included in the first one.
	 * 
	 * @param i1 i1 the first interval to check.
	 * @param i2 the second interval to check.
	 * @return {@code true} if the second interval is included in the first one;<br/>
	 *         {@code false} otherwise.
	 */
	public static <X> boolean includes( Interval<X> i1, Interval<X> i2 )
	{
		
		return i1.includes( i2 );
		
	}
	
	
	/* ***************** */
    /*  EXTENSION HOOKS  */
    /* ***************** */
	
	
	/**
	 * Checks if the first value is strictly smaller than the second.
	 * <p>
	 *	The parameters given to this method are granted to be not null·   
	 * </p>
	 * 
	 * @param t1 the first value to check.
	 * @param t2 the second value to check.
	 * @return {@code true} if the first value is less than the second;<br/>
	 *         {@code false} otherwise.
	 */
    protected abstract boolean isLessThan( T t1, T t2 );
	
	
	/* ******************** */
    /*  COMPARISON METHODS  */
    /* ******************** */

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{

		return HashCoder.hashCode(getBegin().hashCode(), getEnd().hashCode());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object object)
	{

		if( object instanceof Interval<?> )
		{

			final Interval<?> other = (Interval<?>) object;

			return this.getBegin().equals(other.getBegin()) &&
				   this.getEnd().equals(other.getEnd());

		} else
			return false;

	}

}
