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
package org.nerd4j.format;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * All fields or getters of a class marked as <tt> &#64;Formatted</tt> will be
 * used by {@link FormattedClassHandler} to generate the textual representation
 * provided by the method {@link Object#toString()}.
 * <p>
 * This annotation can be placed on fields or getters. Where is found rules how
 * instances to print will be accessed.
 * </p>
 * 
 * @author Nerd4j Team
 */
@Documented
@Target( { ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Formatted
{

	/**
	 * (Optional) The value of the key to be printed instead of the property
	 * name.
	 * <p>
	 * By default the property path will be used (see {@link #value()}). If the
	 * property path is not valued the inferred property name from annotated
	 * field or method will be used instead. This behavior can be overridden by
	 * exploiting this field.
	 * </p>
	 */
	String key() default "";

	/**
	 * (Optional) Value to be printed.
	 * 
	 * <p>
	 *  The default value matches the whole property (i.e.: will be used all
	 *  the identified bean for printing the output); of such bean can be
	 *  used only one property or a chain of properties.
	 * </p>
	 * 
	 * <p>
	 * <pre>
	 *    Example 1: the following annotations are equal
	 * 
	 *    &#064;Formatted
	 *    private String myString;
	 *    
	 *    &#064;Formatted(value="")
	 *    private String myString;
	 * 
	 *    Example 2: the printing of a single property of myObject
	 *    
	 *    &#064;Formatted(value="aProperty")
	 *    private MyObject myObject;
	 *    
	 *    Example 3: the printing of a chain of properties of myObject
	 *    
	 *    &#064;Formatted(value="aProperty.anotherProperty")
	 *    private MyObject myObject;
	 * </pre>
	 * </p>
	 * 
	 */
	String value() default "";

	/**
	 * (Optional) Token separator.
	 */
	String tokenSeparator() default ", ";

	/**
	 * (Optional) key - value separator.
	 */
	String valueSeparator() default "=";

}
