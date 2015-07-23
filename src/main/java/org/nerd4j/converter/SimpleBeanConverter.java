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


/**
 * Simple implementation of the {@link BeanConverter} interface.
 * 
 * <p>
 *  This class implements all the methods declared in the
 *  {@link BeanConverter} interface and provides the following
 *  hook methods for the extending classes:
 *  <ol>
 *   <li>{@link SimpleBeanConverter#newTargetInstance()}</li>
 *   <li>{@link SimpleBeanConverter#performMapping(Object, Object)}</li>
 *  </ol>
 * </p>
 * 
 * @param <S> source bean from which to get the information.
 * @param <T> target bean to fill with the information.
 * 
 * @author Nerd4j Team
 */
public abstract class SimpleBeanConverter<S,T> extends AbstractBeanConverter<S,T>
{
    
    /**
     * Default constructor.
     * 
     */
    public SimpleBeanConverter() {}
    
    
    /* ******************* */
    /*  INTERFACE METHODS  */
    /* ******************* */
	
    
    /**
     * {@inheritDoc}
     */
    @Override
    public T convert( S source )
    {
        
        /* If the conversion of a null source is null. */
        if( source == null ) return null;
        
        /* We retrieve a new instance of the target bean. */
        final T target = newTargetInstance();
        
        /*
         * If the mapping of the properties is performed with
         * success returns the target bean else returns null. 
         */
        return performMapping( source, target ) ? target : null;
        
    }
    
    
    /* ***************** */
    /*  EXTENSION HOOKS  */
    /* ***************** */
    
    
    /**
     * This method is called by the conversion methods to get
     * a new target bean instance every time is needed.
     * 
     * @return a new target bean instance.
     */
    protected abstract T newTargetInstance();
    
    
    /**
     * Maps the properties of the given source bean of type {@code S}
     * into the related properties of the given target bean of type {@code T}.
     * <p>
     *  If all the properties are mapped successfully
     *  the method returns {@code true} else returns {@code false}.
     * </p>
     * <p>
     *  If the source bean or the target bean are both {@code null}
     *  nothing will be done and the method returns {@code true}.
     * </p>
     *
     * @param source the information source bean.
     * @param target the information target bean.
     * @return {@code true} if the mapping has been performed with success.
     */
    protected abstract boolean performMapping( S source, T target );
    

}