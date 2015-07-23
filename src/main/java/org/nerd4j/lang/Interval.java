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

/**
 * Represents an interval with a {@code begin} value included
 * and an {@code end} value excluded.
 * 
 * <p>
 *  Note: All the implementations of this interface must grant
 *        that the {@code begin} value is strictly smaller than
 *        the {@code end} value.
 * </p>
 * 
 * @author Nerd4j Team
 */
public interface Interval<T>
{

	/**
	 * Returns the value that defines the begin of the interval.
	 * 
	 * @return value that defines the begin of the interval.
	 */
	public T getBegin();
	
	/**
	 * Returns the value that defines the end of the interval.
	 * 
	 * @return value that defines the end of the interval.
	 */
	public T getEnd();
	
	/**
	 * Checks if the given value is within interval.
	 * 
	 * @param value    the value to check.
	 * @return {@code true} if the value belongs to the interval;<br/>
	 *         {@code false} otherwise.
	 */
	public boolean isInRange( T value );

	/**
	 * Checks if this interval and the given one have at least
	 * one value in common.
	 * 
	 * @param interval the interval to check against this.
	 * @return {@code true} if there is at least one value in common;<br/>
	 *         {@code false} otherwise.
	 */
	public boolean overlaps( Interval<T> interval );
	
	/**
	 * Checks if the given interval is included in this one.
	 * 
	 * @param interval the interval to check against this.
	 * @return {@code true} if the given interval is included in this one;<br/>
	 *         {@code false} otherwise.
	 */
	public boolean includes( Interval<T> interval );
	
}

