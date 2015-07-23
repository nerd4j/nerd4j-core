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
package org.nerd4j.format;

import java.io.Serializable;

/**
 * Represents a generic <code> Java Bean </ code> equipped with a method
 * that simplifies and standardizes the textual representation provided
 * by the method {@link Object#toString()}.
 * 
 * <p>
 *  All classes that extend this must represent a <code>Java Bean</code>
 *  and therefore must fulfill the following requirements:
 *  <ol>
 *   <li>Must have a parameterless constructor (so-called <code>default constructor</code>).</li>
 *   <li>Must expose the property access methods (so-called  <code>getters</code> and <code>setters</code>).</li>   
 *   <li>Must implement the {@link java.io.Serializable} interface.</li>
 *  </ol>
 * </p>
 * 
 * <p>
 *  Using this type of <code>Java Bean</code> the method {@link Object#toString()}
 *  will return a text in the form:
 * <pre>
 *  class.simpleName [ field_1.name=field_1.value, field_2.name=field_2.value, ... ]
 * </pre>
 * </p>
 * <p>
 *  The fields to be printed must be annotated with {@link Formatted @Formatted}.
 * </p>
 * 
 * @author Nerd4j Team
 */
public abstract class AnnotatedFormattedBean implements Serializable
{
	
	/** The serial version UUID. */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Generates a string using reflection showing the fields
	 * annotated with {@link Formatted @Formatted}.
	 * 
	 * @return formatted representation of the current instance.
	 */
	protected String toStringContent()
	{
		
		return FormattedClassHandler.getInstance().toStringContent( this );
		
	}
	
	/**
	 * Returns a string representation of this object.
	 *
	 * @return string representation of this object.
	 */
	@Override
	public String toString()
	{
		
		return this.getClass().getSimpleName() + "[" + toStringContent() + "]";
		
	}

}
