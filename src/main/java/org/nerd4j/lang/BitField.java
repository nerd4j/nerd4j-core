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

import java.util.Map;


/**
 * Represents a bit field, most commonly used to represent integral types
 * of known and fixed bit-width.
 * <p>
 *  Given a fixed number of ordered elements, {@link BitField} associates
 *  to the index of each element a related <code>boolean</code> value.
 * </p>
 * 
 * @param <I> type of the index used to access the field.
 * 
 * @author Nerd4j Team
 */
public interface BitField<I>
{
	
	/**
	 * Returns the value of the bit in the position identified by the given index.
	 * 
	 * @param index the index of the bit to read.
	 * @return the boolean value representing the bit.
	 * 
	 * @throws NullPointerException if the given index is <code>null</code>.
	 * @throws IndexOutOfBoundsException 
	 *         if the given index is out of the bit field limits.
	 */
	public boolean get( I index ) throws NullPointerException, IndexOutOfBoundsException;
	
	/**
	 * Sets the value of the bit in the position identified by the given index.
	 * 
	 * @param index the index of the bit to set.
	 * @param value value to set the bit with.
	 * 
	 * @return the old value of the bit.
	 * 
	 * @throws NullPointerException if the given index is <code>null</code>.
	 * @throws IndexOutOfBoundsException 
	 *         if the given index is out of the bit field limits.
	 */
	public boolean set( I index, boolean value ) throws NullPointerException, IndexOutOfBoundsException;
	
	/**
	 * Toggles the value of the bit in the position identified by the given index.
	 * 
	 * @param  index the index of the bit to toggle.
	 * @return the value of the bit after toggle.
	 * 
	 * @throws NullPointerException if the given index is <code>null</code>.
	 * @throws IndexOutOfBoundsException 
	 *         if the given index is out of the bit field limits.
	 */
	public boolean toggle( I index ) throws NullPointerException, IndexOutOfBoundsException;
		
	/**
	 * Returns the size of the bit field.
	 * 
	 * @return the size of the bit field.
	 */
	public int size();
	
	/**
	 * Returns a {@link Map} representation of this {@link BitField}.
	 * 
	 * @return a map representation of the bit field.
	 */
	public Map<I,Boolean> toMap();
	
}
