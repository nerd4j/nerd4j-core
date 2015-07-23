/*
 * #%L
 * Nerd4j Core
 * %%
 * Copyright (C) 2011 - 2014 Nerd4j
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

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Simple {@link Iterator} <tt>EmptyObject</tt> pattern implementation
 * 
 * @author @author Nerd4j Team
 */
public final class EmptyIterator<E> implements Iterator<E>
{
	
	/** <i>Singleton</i> instance */
	private static final Iterator<?> INSTANCE = new EmptyIterator<Object>();
	
	
	/**
	 * <i>Singleton</i> factory method.
	 * @return an empty iterator
	 */
	@SuppressWarnings("unchecked")
	public static <X> Iterator<X> intance()
	{
		
		return (Iterator<X>) INSTANCE;
		
	}
	
	
	/**
	 * Default constructor.
	 * 
	 */
	private EmptyIterator()
	{
		
		super();
		
	}
	
	
	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext()
	{
		
		return false;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public E next()
	{
		
		throw new NoSuchElementException( "An empty iterator never has next" );
		
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Remove operation not supported.
	 * </p>
	 */
	@Override
	public void remove()
	{
		
		throw new UnsupportedOperationException( "Cannot remove anything from an empty iterator" );
		
	}
	
}
