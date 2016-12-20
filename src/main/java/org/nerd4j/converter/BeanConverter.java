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

import java.util.List;
import java.util.Set;

/**
 * Represents a {@code Java Bean} converter, its aim is to transfer
 * information from a source {@code Java Bean} into a target
 * {@code Java Bean} with a different format.
 * 
 * <p>
 * This class is expected to handle  {@code Java Bean}s.
 * As defined in the Java specifications, a {@code Java Bean}
 * must always provide a public default constructor and public
 * access method (so called {@code getter}s and {@code setter}s).
 * 
 * @param <S> source bean from which to get the information.
 * @param <T> target bean to fill with the information.
 * 
 * @author Nerd4j Team
 */
public interface BeanConverter<S,T>
{
	
	/**
	 * Creates and fills a target bean of type {@code T}
	 * with the informations provided by the source bean
	 * of type {@code S}.
	 * <p>
	 * If the source bean is {@code null} returns {@code null}.
     *
     * @param source the information source bean.
     * @return the information target bean.
     */
	public T convert( S source );
    	
	/**
	 * Creates and fills a {@link Set} of target beans of type {@code T}
	 * with the informations provided by the {@link Set} of source bean
	 * of type {@code S}.
	 * 
	 * <p>
	 * If the source {@link Set} is {@code null} returns {@code null}.
     *
     * @param source the information source {@link Set}.
     * @return the information target {@link Set}.
     */
	public Set<T> convert( Set<? extends S> source );

	/**
	 * Creates and fills a {@link List} of target beans of type {@code T}
	 * with the informations provided by the {@link List} of source bean
	 * of type {@code S}.
	 * 
	 * <p>
	 * If the source {@link List} is {@code null} returns {@code null}.
     *
     * @param source the information source {@link List}.
     * @return the information target {@link List}.
     */
	public List<T> convert( List<? extends S> source );
    	
}