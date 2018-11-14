/*
 * #%L
 * Nerd4j Core
 * %%
 * Copyright (C) 2011 - 2018 Nerd4j
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
package org.nerd4j.lang.array;

import java.util.Iterator;

import org.nerd4j.util.Require;

/**
 * Implementation of {@link Iterator} that iterates
 * over an array of {@code byte}.
 * 
 * @author Nerd4j Team
 */
public class ByteArrayIterator extends AbstractArrayIterator<Byte>
{

	
	/** The source array to iterate. */
	private final byte[] array;
	
	
	/**
	 * Constructor with parameters.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 */
	public ByteArrayIterator( byte[] array, int startInclusive, int endExclusive )
	{
		
		super( startInclusive, endExclusive, Require.nonNull( array, "The source array is mandatory" ).length );
		
		this.array = array;
		
	}
	
	
	/* ***************** */
	/*  EXTENSION HOOKS  */
	/* ***************** */

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	Byte get( int index )
	{
		
		return array[index];
		
	}

}
