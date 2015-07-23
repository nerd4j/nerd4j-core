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
package org.nerd4j.converter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Common abstract implementation of the {@link BeanConverter} interface.
 * 
 * <p>
 *  This class is common to all the bean converters.
 *  Provides a default implementation of the following methods:
 *  <ol>
 *   <li>{@link BeanConverter#convert(Set)}</li>
 *   <li>{@link BeanConverter#convert(List)}</li>
 *  </ol>
 *  used to convert collections of beans.
 * </p>
 * 
 * @param <S> source bean from which to get the information.
 * @param <T> target bean to fill with the information.
 * 
 * @author Nerd4j Team
 */
public abstract class AbstractBeanConverter<S,T> implements BeanConverter<S,T>
{
    
    /**
     * Default constructor.
     * 
     */
    public AbstractBeanConverter() {}
    
    
    /* ******************* */
    /*  INTERFACE METHODS  */
    /* ******************* */
    
    
    /**
	 * {@inheritDoc}
     */
    @Override
	public Set<T> convert( Set<? extends S> source )
    {

        if( source == null ) return null;

        final Set<T> target = new HashSet<T>( source.size() );
        for( S sourceBean : source )
            target.add( convert(sourceBean) );

        return target;

    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> convert( List<? extends S> source )
    {
    	
    	if( source == null ) return null;
    	
    	final List<T> target = new ArrayList<T>( source.size() );
    	for( S sourceBean : source )
            target.add( convert(sourceBean) );
    	
    	return target;
    	
    }

}