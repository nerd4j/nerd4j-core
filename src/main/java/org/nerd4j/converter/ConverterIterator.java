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
package org.nerd4j.converter;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts data provided from a wrapped {@link Iterator}.
 *
 * @param <S> source bean from which to get the information.
 * @param <T> target bean to fill with the information.
 * 
 * @see BeanConverter
 * 
 * @author Nerd4j Team
 */
public class ConverterIterator<S,T> implements Iterator<T>
{

	/** Static class logger. */
	private static final Logger logger = LoggerFactory.getLogger( ConverterIterator.class );
	
	/** Converter from inner iterator type to external type. */
	private final BeanConverter<S, T> converter;
	
	/** Source data provider. */
	private final Iterator<S> iterator;
	
	/** Flag to ignore conversion failures. */
	private final boolean ignoreFailures;
	
	/** Next element */
	private T next;
	
	/**
	 * Create a new {@link ConverterIterator}.
	 * <p>
	 * Conversion failures will be propagated from methods {@link #hasNext()}
	 * and {@link #next()}.
	 * 
	 * @param converter bean converter
	 * @param iterator  bean data source
	 */
	public ConverterIterator( BeanConverter<S, T> converter, Iterator<S> iterator )
	{
		
		this( converter, iterator, false );
		
	}
	
	/**
	 * Create a new {@link ConverterIterator}.
	 * 
	 * @param converter bean converter
	 * @param iterator  bean data source
	 * @param ignoreFailures
	 *            ignore failures flag, set to true this instance won't throws
	 *            exceptions on conversion failures
	 */
	public ConverterIterator( BeanConverter<S, T> converter, Iterator<S> iterator, boolean ignoreFailures )
	{
		
		this.converter      = converter;
		this.iterator       = iterator;
		this.ignoreFailures = ignoreFailures;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext()
	{
		
		/* Next will be null if no more elements remains. */
		prepareNextIfNeeded();
		
		return next != null;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T next()
	{
		
		/* As side effect next element will be prepared. */
		if ( !hasNext() )
			throw new NoSuchElementException( "No more elements" );
		
		/* Return next element and clear it. */
		final T result = next;
		next = null;
		
		return result;
		
	}
	
	/**
	 * {@inheritDoc}
	 * <p>
	 * Remove operation not supported.
	 */
	@Override
	public void remove()
	{
		
		throw new UnsupportedOperationException( "Remove operation not nupported" );
		
	}
	
	/**
	 * Prepares {@link #next} element if needed. On return next element will be null if
	 * no more elements exists.
	 */
	private void prepareNextIfNeeded()
	{
		
		/*
		 * Convert until next has been found or no more elements exists. If no
		 * exception are thrown just one loop.
		 */
		S source;
		while( next == null && iterator.hasNext() )
		{
			
			source = iterator.next();
			
			try
			{
				
				next = converter.convert( source );
				
			} catch ( Exception e )
			{
				
				if ( ignoreFailures )
					logger.warn( "Got a uncovertible bean {} due to {}. Skipping it.", source, e.getMessage() );
				
				else
					throw new IllegalArgumentException( "Got a uncovertible bean " + source, e );
				
			}
			
		}
		
	}
	
}
