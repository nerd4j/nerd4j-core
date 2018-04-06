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
package org.nerd4j.util;

import java.util.Collection;
import java.util.function.Supplier;

import org.nerd4j.exception.RequirementFailure;

/**
 * Assertion utility class that assists in validating arguments.
 * Is inspired by the {@link java.util.Objects} approach.
 * 
 * <p>
 * The methods of this class return the given argument if the required property holds,
 * otherwise throw exceptions depending of the type of property violated.
 * 
 * <p>
 * This class is designed primarily for doing parameter validation in methods
 * and constructors, as demonstrated below:
 * <blockquote><pre>
 * public Foo( int id, String val, Bar bar )
 * {
 *    this.id  = Require.toHold( id &gt; 0 );
 *    this.bar = Require.nonNull( bar, "bar must not be null" );
 *    this.val = Require.nonEmpty( val, "val must not be empty" );
 * }
 * </pre></blockquote>
 * 
 * @author Nerd4j Team
 * @since 1.1.1
 */
public class Require
{
	
	/** Prefix to prepend to all messages. */
	private static final String PREFIX = "[Requirement failed] - ";
	
	
	/* GENERIC ASSERTION CHECK */

	/**
     * Checks that the specified object holds the given assertion.
     * 
     * @param assertion the assertion that has to be {@code true}
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static void toHold( boolean assertion )
    {
    	
        toHold( assertion, "the required assertion must hold" );
        
    }

    /**
     * Checks that the specified object holds the given assertion and
     * throws a customized {@link RequirementFailure} if not.
     *
     * @param assertion the assertion that has to be {@code true}
     * @param message detail message to be used in the event that a {@code
     *                RequirementFailure} is thrown
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static void toHold( boolean assertion, String message )
    {
    	
        if( ! assertion )
            throw new RequirementFailure( PREFIX + message );
                
    }
    
    /**
     * Checks that the specified object holds the given assertion and
     * throws a customized {@link RequirementFailure} if not.
     *
     * <p>Unlike the method {@link #toHold(boolean, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param assertion the assertion that has to be {@code true}
     * @param messageSupplier supplier of the detail message to be
     *        used in the event that a {@code RequirementFailure} is thrown
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static void toHold( boolean assertion, Supplier<String> messageSupplier )
    {
        if( ! assertion )
            throw new RequirementFailure( PREFIX + messageSupplier.get() );
        
    }
    
    
    /* NON NULL CHECK */
    
    /**
     * Checks that the specified object reference is not {@code null}.
     *
     * @param value the object reference to check for nullity
     * @param <T> the type of the reference
     * @return {@code value} if not {@code null}
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <T> T nonNull( T value )
    {
    	
    	return nonNull( value, "this argument must not be null" );
    	
    }
    
    /**
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the object reference to check for nullity
     * @param message detail message to be used in the event that a {@code
     *                RequirementFailure} is thrown
     * @param <T> the type of the reference
     * @return {@code value} if not {@code null}
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <T> T nonNull( T value, String message )
    {
    	
    	if( value == null )
    		throw new RequirementFailure( PREFIX + message );
    	
    	return value;
    	
    }
    
    /**
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonNull(Object, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value   the object reference to check for nullity
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @param <T> the type of the reference
     * @return {@code value} if not {@code null}
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <T> T nonNull( T value, Supplier<String> messageSupplier )
    {
    	if( value == null )
    		throw new RequirementFailure( PREFIX + messageSupplier.get() );
    	
    	return value;
    	
    }
    
    
    /* NON EMPTY STRING CHECK */
    
    /**
     * Checks that the specified {@link String} reference is not {@code null} or empty.
     *
     * @param value the {@link String} reference to check for emptiness
     * @return {@code value} if not {@code null} or empty
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static String nonEmpty( String value )
    {
    	
        return nonEmpty( value, "this argument must not be empty" );
        
    }

    /**
     * Checks that the specified {@link String} reference is not {@code null} or empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the {@link String} reference to check for emptiness
     * @param message detail message to be used in the event that a {@code
     *                RequirementFailure} is thrown
     * @return {@code value} if not {@code null} or empty
     * @throws RequirementFailure if {@code value} is {@code null} or empty
     */
    public static String nonEmpty( String value, String message )
    {
    	
        if( value == null || value.isEmpty() )
            throw new RequirementFailure( PREFIX + message );
        
        return value;
        
    }
    
    /**
     * Checks that the specified {@link String} reference is not {@code null} or empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(String, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value   the {@link String} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} or empty
     * @throws RequirementFailure if {@code value} is {@code null} or empty
     */
    public static String nonEmpty( String value, Supplier<String> messageSupplier )
    {
        if( value == null || value.isEmpty() )
            throw new RequirementFailure( PREFIX + messageSupplier.get() );
        
        return value;
        
    }
    
	
    /* NON EMPTY COLLECTION CHECK */
    
    /**
     * Checks that the specified {@link Collection} reference is not {@code null} or empty.
     *
     * @param value the {@link Collection} reference to check for emptiness
     * @param <T> the type of the elements in the {@link Collection}
     * @return {@code value} if not {@code null} or empty
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <T> Collection<T> nonEmpty( Collection<T> value )
    {
    	
    	return nonEmpty( value, "this argument must not be empty" );
    	
    }
    
    /**
     * Checks that the specified {@link Collection} reference is not {@code null} or empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param value   the {@link Collection} reference to check for emptiness
     * @param message detail message to be used in the event that a {@code
     *                RequirementFailure} is thrown
     * @param <T> the type of the elements in the {@link Collection}
     * @return {@code value} if not {@code null} or empty
     * @throws RequirementFailure if {@code value} is {@code null} or empty
     */
    public static <T> Collection<T> nonEmpty( Collection<T> value, String message )
    {
    	
    	if( value == null || value.isEmpty() )
    		throw new RequirementFailure( PREFIX + message );
    	
    	return value;
    	
    }
    
    /**
     * Checks that the specified {@link Collection} reference is not {@code null} or empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Collection, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param value   the {@link Collection} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @param <T> the type of the elements in the {@link Collection}
     * @return {@code value} if not {@code null} or empty
     * @throws RequirementFailure if {@code value} is {@code null} or empty
     */
    public static <T> Collection<T> nonEmpty( Collection<T> value, Supplier<String> messageSupplier )
    {
    	if( value == null || value.isEmpty() )
    		throw new RequirementFailure( PREFIX + messageSupplier.get() );
    	
    	return value;
    	
    }
    
}
