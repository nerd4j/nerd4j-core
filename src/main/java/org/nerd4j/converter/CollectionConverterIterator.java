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

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.nerd4j.util.EmptyIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Converts data provided from a wrapped {@link Iterator}.
 * <p>
 * This class is very similar to {@link ConverterIterator} but it differs for
 * accepted {@link BeanConverter}. Converters from bean to collection of bean
 * are accepted
 * </p>
 * 
 * @param <S> source bean from which to get the information.
 * @param <T> target bean to fill with the information.
 * 
 * @see BeanConverter
 * @see ConverterIterator
 * 
 * @author Nerd4j Team
 */
public class CollectionConverterIterator<S,T> implements Iterator<T>
{
	
	/** Static class logger. */
	private static final Logger logger = LoggerFactory.getLogger( CollectionConverterIterator.class );

	/** Converter from inner iterator type to a collection of external type. */
	private final BeanConverter<S,? extends Collection<T>> converter;
	
	/** Source data provider. */
	private final Iterator<S> iterator;
	
	/** Flag to ignore conversion failures. */
	private final boolean ignoreFailures;
	
	/**
	 * Next iterator, initialized to {@link EmptyIterator} to simplify
	 * {@link #prepareNextIfNeeded()} implementation.
	 */
	private Iterator<T> next = EmptyIterator.intance();
	
	/**
	 * Create a new {@link CollectionConverterIterator}.
	 * <p>
	 * Conversion failures will be propagated from methods {@link #hasNext()}
	 * and {@link #next()}.
	 * </p>
	 * 
	 * @param converter bean converter
	 * @param iterator  bean data source
	 */
	public CollectionConverterIterator( BeanConverter<S,? extends Collection<T>> converter, Iterator<S> iterator )
	{
		
		this( converter, iterator, false );
		
	}
	
	/**
	 * Create a new {@link CollectionConverterIterator}.
	 * 
	 * @param converter bean converter
	 * @param iterator  bean data source
	 * @param ignoreFailures
	 *            ignore failures flag, set to true this instance won't throws
	 *            exceptions on conversion failures
	 */
	public CollectionConverterIterator( BeanConverter<S,? extends Collection<T>> converter, Iterator<S> iterator, boolean ignoreFailures )
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
		
		/* Next will be a ended iterator if no more elements remains. */
		prepareNextIfNeeded();
		
		return next.hasNext();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public T next()
	{
		
		/* As side effect next iterator will be prepared. */
		if ( !hasNext() )
			throw new NoSuchElementException( "No more elements" );
		
		return next.next();
		
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
		
		throw new UnsupportedOperationException( "Remove operation not nupported" );
		
	}
	
	/**
	 * Prepares {@link #next} iterator advancing to next one if needed.
	 */
	private void prepareNextIfNeeded()
	{
		
		/*
		 * Convert until next has elements or no more source elements exists. If
		 * no exception are thrown just one loop.
		 */
		S source;
		while( !next.hasNext() && iterator.hasNext() )
		{
			
			source = iterator.next();
			
			try
			{
				
				next = converter.convert( source ).iterator();
				
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
