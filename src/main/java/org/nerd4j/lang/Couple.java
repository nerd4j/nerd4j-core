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

import java.util.Arrays;
import java.util.HashMap;

import org.nerd4j.format.AnnotatedFormattedBean;
import org.nerd4j.format.Formatted;
import org.nerd4j.util.HashCoder;

/**
 * Represents a generic couple of values.
 * <p>
 *  This object is supposed to be used in combination with
 *  such classes which use {@link #hashCode()} (like {@link HashMap});
 *  The computed hash code is safe also in the case where
 *  one or both values are arrays.
 * </p>
 * 
 * @author Nerd4j Team
 * 
 * @param <A> type of the first element.
 * @param <B> type of the second element.
 */
public class Couple<A,B> extends AnnotatedFormattedBean
{

	/** Il serial version UUID. */
	private static final long serialVersionUID = 1L;
	
	/** First element of the couple. */
	@Formatted
	private A first;
	
	/** Second element of the couple. */
	@Formatted
	private B second;
	
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param first  first element of the couple.
	 * @param second second element of the couple.
	 */
	public Couple( A first, B second )
	{
		
		super();
		
		this.first  = first;
		this.second = second;
		
	}
	
	
	/* ********************* */
	/*   GETTERS & SETTERS   */
	/* ********************* */
	
	/**
	 * Returns the first element of the couple.
	 * 
	 * @return first element of the couple.
	 */
	public A getFirst()
	{
		return first;
	}

	/**
	 * Sets the first element of the couple.
	 * 
	 * @param first the first element of the couple.
	 */
	public void setFirst( A first )
	{
		this.first = first;
	}

	/**
	 * Returns the second element of the couple.
	 * 
	 * @return the second element of the couple.
	 */
	public B getSecond()
	{
		return second;
	}

	/**
	 * Sets the second element of the couple.
	 * 
	 * @param second the second element of the couple.
	 */
	public void setSecond( B second )
	{
		this.second = second;
	}
	
	
	/* ******************** */
	/*   FACTORY  METHODS   */
	/* ******************** */
	
	
	/**
	 * Factory method to produce a couple
	 * with the given values.
	 * 
	 * @param x the first element of the couple.
	 * @param y the second element of the couple.
	 * 
	 * @return the couple with the given values.
	 */
	public static <X,Y> Couple<X,Y> valueOf( X x, Y y )
	{
		
		return new Couple<X,Y>( x,y );
		
	}
	
	
	/* ******************** */
	/*  COMPARISON METHODS  */
	/* ******************** */
	
	
	/**
	 * Indicates whether some other {@link Couple} is "equal to" this one.
	 * This method implements the deep equality, i.e. if the couple contains
	 * arrays checks the equality of the arrays element by element.
	 * 
	 * @return <tt>true</tt> if the two values are equals.
	 */
	private boolean elementEquals( Object element, Object other )
	{
		
		if( element == other ) return true;
		if( element == null && other == null ) return true;
		if( element == null || other == null ) return false;
		
		if( ! element.getClass().isArray() )
			return element.equals(other);
		
		// Questo ramo gestisce anche array multidimensionali di nativi
		else if ( element instanceof Object[] && other instanceof Object[] )
			return Arrays.deepEquals( (Object[]) element, (Object[]) other);
		
		else if ( element instanceof int[] && other instanceof int[] )
			return Arrays.equals( (int[]) element, (int[]) other);
		
		else if ( element instanceof byte[] && other instanceof byte[] )
			return Arrays.equals( (byte[]) element, (byte[]) other);
		
		else if ( element instanceof long[] && other instanceof long[] )
			return Arrays.equals( (long[]) element, (long[]) other);
		
		else if ( element instanceof short[] && other instanceof short[] )
			return Arrays.equals( (short[]) element, (short[]) other);
		
		else if ( element instanceof boolean[] && other instanceof boolean[] )
			return Arrays.equals( (boolean[]) element, (boolean[]) other);
		
		else if ( element instanceof float[] && other instanceof float[] )
			return Arrays.equals( (float[]) element, (float[]) other);
		
		else if ( element instanceof double[] && other instanceof double[] )
			return Arrays.equals( (double[]) element, (double[]) other);
		
		else if ( element instanceof char[] && other instanceof char[] )
			return Arrays.equals( (char[]) element, (char[]) other);
		
		return false;
	}

	
    /**
	 * Returns the an hash code for this {@link Couple}
	 * based on the hash codes of the related values. 
	 * 
	 * @return hash code for this object.
	 */
	@Override
	public int hashCode()
	{
		
		return HashCoder.hashCode( 100169, first, second );
		
	}
	
	/**
     * Indicates whether some other {@link Object} is "equal to" this one.
     *
     * @param obj the object to evaluate.
     * @return <tt>true</tt> if the two values are equals.
     */
    @Override
	public boolean equals( Object obj )
	{
		if (this == obj) return true;
		if (obj == null) return false;
		
		if( ! (obj instanceof Couple<?,?>) ) return false;
		
		final Couple<?,?> other = (Couple<?,?>) obj;
		
		if( ! elementEquals(first, other.first) )
			return false;
		
		if( ! elementEquals(second, other.second) )
			return false;
		
		return true;
		
	}
	
}
