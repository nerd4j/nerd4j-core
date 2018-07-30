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
import java.util.Map;
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
     * Checks the given assertion to be {@code true}.
     * 
     * @param assertion the assertion that has to be {@code true}
     * @throws RequirementFailure if the {@code assertion} is {@code false}
     */
    public static void toHold( boolean assertion )
    {
    	
        Require.toHold( assertion, "the required assertion must hold" );
        
    }

    /**
     * Checks the given assertion to be {@code true} and
     * throws a customized {@link RequirementFailure} if not.
     *
     * @param assertion the assertion that has to be {@code true}
     * @param message detail message to be used in the event that a {@code
     *                RequirementFailure} is thrown
     * @throws RequirementFailure if the {@code assertion} is {@code false}
     */
    public static void toHold( boolean assertion, String message )
    {
    	
        if( ! assertion )
            throw new RequirementFailure( PREFIX + message );
                
    }
    
    /**
     * Checks the given assertion to be {@code true} and
     * throws a customized {@link RequirementFailure} if not.
     *
     * <p>
     * Unlike the method {@link #toHold(boolean, String)},
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
     * @throws RequirementFailure if the {@code assertion} is {@code false}
     */
    public static void toHold( boolean assertion, Supplier<String> messageSupplier )
    {
        if( ! assertion )
            throw new RequirementFailure( PREFIX + messageSupplier.get() );
        
    }
    
    
	/* ASSERTION FOR A GIVEN VALUE CHECK */

	/**
     * Checks the given assertion to hold for the specified value.
     * 
     * <p>
     * The assertion passed to this method should check some property
     * of the given value. If the assertion holds the related value
     * will be returned.
     * 
     * @param <V> the type of the value.
     * @param value the value to be checked.
     * @param assertion the assertion relating to the given value that has to be {@code true}
     * @return {@code value} if the related {@code assertion} holds.
     * @throws RequirementFailure if the {@code assertion} is {@code false},
     *         the given {@code value} otherwise.
     */
    public static <V> V trueFor( V value, boolean assertion )
    {
    	
    	return Require.trueFor( value, assertion, () -> "the required assertion must hold for " + value );
        
    }

    /**
     * Checks the given assertion to hold for the specified value
     * and throws a customized {@link RequirementFailure} if not.
     * 
     * <p>
     * The assertion passed to this method should check some property
     * of the given value. If the assertion holds the related value
     * will be returned 
     *
     * @param <V> the type of the value.
     * @param value the value to be checked.
     * @param assertion the assertion that has to be {@code true}
     * @param message detail message to be used in the event that a {@code
     *                RequirementFailure} is thrown
     *  @return {@code value} if the related {@code assertion} holds.
     * @throws RequirementFailure if the {@code assertion} is {@code false},
     *         the given {@code value} otherwise.
     */
    public static<V> V trueFor( V value, boolean assertion, String message )
    {
    	
    	Require.toHold( assertion, message );
        return value;
                
    }
    
    /**
     * Checks the given assertion to hold for the specified value.
     * and throws a customized {@link RequirementFailure} if not.
     * 
     * <p>
     * The assertion passed to this method should check some property
     * of the given value. If the assertion holds the related value
     * will be returned.
     *
     * <p>Unlike the method {@link #trueFor(Object,boolean,String)},
     * this method allows creation of the message to be deferred until
     * after the assertion check is made. While this may confer a
     * performance advantage in the good case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param <V> the type of the value.
     * @param value the value to be checked.
     * @param assertion the assertion that has to be {@code true}
     * @param messageSupplier supplier of the detail message to be
     *        used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if the related {@code assertion} holds.
     * @throws RequirementFailure if the {@code assertion} is {@code false},
     *         the given {@code value} otherwise.
     */
    public static <V> V trueFor( V value, boolean assertion, Supplier<String> messageSupplier )
    {
        
    	Require.toHold( assertion, messageSupplier );
    	return value;
        
    }
    
    
    /* NON NULL CHECK */
    
    /**
     * Checks that the specified object reference is not {@code null}.
     *
     * @param <V> the type of the reference
     * @param value the object reference to check for nullity
     * @return {@code value} if not {@code null}
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <V> V nonNull( V value )
    {
    	
    	return Require.nonNull( value, "this argument must not be null" );
    	
    }
    
    /**
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link RequirementFailure} if it is.
     *
     * @param <V> the type of the reference
     * @param value   the object reference to check for nullity
     * @param message detail message to be used in the event that a {@code
     *                RequirementFailure} is thrown
     * @return {@code value} if not {@code null}
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <V> V nonNull( V value, String message )
    {
    	
    	return Require.trueFor( value, value != null, message );
    	
    }
    
    /**
     * Checks that the specified object reference is not {@code null} and
     * throws a customized {@link RequirementFailure} if it is.
     *
     * <p>
     * Unlike the method {@link #nonNull(Object, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param <V> the type of the reference
     * @param value   the object reference to check for nullity
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null}
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <V> V nonNull( V value, Supplier<String> messageSupplier )
    {
    	
    	return Require.trueFor( value, value != null, messageSupplier );
    	
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
    	
        return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );
        
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
    	
        return Require.trueFor( value, value != null && ! value.isEmpty(), message );
        
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
        
    	return Require.trueFor( value, value != null && ! value.isEmpty(), messageSupplier );
        
    }
    
	
    /* NON EMPTY COLLECTION CHECK */
    
    /**
     * Checks that the specified {@link Collection} reference is not {@code null} or empty.
     *
     * @param <V> the type of the elements in the {@link Collection}
     * @param <C> the actual implementation of the {@link Collection}
     * @param value the {@link Collection} reference to check for emptiness
     * @return {@code value} if not {@code null} or empty
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <V,C extends Collection<V>> C nonEmpty( C value )
    {
    	
    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );
    	
    }
    
    /**
     * Checks that the specified {@link Collection} reference is not {@code null} or empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param <V> the type of the elements in the {@link Collection}
     * @param <C> the actual implementation of the {@link Collection}
     * @param value   the {@link Collection} reference to check for emptiness
     * @param message detail message to be used in the event that a {@code
     *                RequirementFailure} is thrown
     * @return {@code value} if not {@code null} or empty
     * @throws RequirementFailure if {@code value} is {@code null} or empty
     */
    public static <V,C extends Collection<V>> C nonEmpty( C value, String message )
    {
    	
    	return Require.trueFor( value, value != null && ! value.isEmpty(), message );
    	
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
     * @param <V> the type of the elements in the {@link Collection}
     * @param <C> the actual implementation of the {@link Collection}
     * @param value   the {@link Collection} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} or empty
     * @throws RequirementFailure if {@code value} is {@code null} or empty
     */
    public static <V,C extends Collection<V>> C nonEmpty( C value, Supplier<String> messageSupplier )
    {
    	
    	return Require.trueFor( value, value != null && ! value.isEmpty(), messageSupplier );
    	
    }
    
    
    /* NON EMPTY MAP CHECK */
    
    /**
     * Checks that the specified {@link Map} reference is not {@code null} or empty.
     *
     * @param <K> the type of the key elements in the {@link Map}
     * @param <V> the type of the value elements in the {@link Map}
     * @param <M> the actual implementation of the {@link Map}
     * @param value the {@link Map} reference to check for emptiness
     * @return {@code value} if not {@code null} or empty
     * @throws RequirementFailure if {@code value} is {@code null}
     */
    public static <K,V,M extends Map<K,V>> M nonEmpty( M value )
    {
    	
    	return Require.nonEmpty( value, () -> "this argument must not be empty but was " + value );
    	
    }
    
    /**
     * Checks that the specified {@link Map} reference is not {@code null} or empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * @param <K> the type of the key elements in the {@link Map}
     * @param <V> the type of the value elements in the {@link Map}
     * @param <M> the actual implementation of the {@link Map}
     * @param value   the {@link Map} reference to check for emptiness
     * @param message detail message to be used in the event that a {@code
     *                RequirementFailure} is thrown
     * @return {@code value} if not {@code null} or empty
     * @throws RequirementFailure if {@code value} is {@code null} or empty
     */
    public static <K,V,M extends Map<K,V>> M nonEmpty( M value, String message )
    {
    	
    	return Require.trueFor( value, value != null && ! value.isEmpty(), message );
    	
    }
    
    /**
     * Checks that the specified {@link Map} reference is not {@code null} or empty
     * and throws a customized {@link RequirementFailure} if it is.
     *
     * <p>Unlike the method {@link #nonEmpty(Map, String)},
     * this method allows creation of the message to be deferred until
     * after the null check is made. While this may confer a
     * performance advantage in the non-null case, when deciding to
     * call this method care should be taken that the costs of
     * creating the message supplier are less than the cost of just
     * creating the string message directly.
     *
     * @param <K> the type of the key elements in the {@link Map}
     * @param <V> the type of the value elements in the {@link Map}
     * @param <M> the actual implementation of the {@link Map}
     * @param value   the {@link Map} reference to check for emptiness
     * @param messageSupplier supplier of the detail message to be
     * used in the event that a {@code RequirementFailure} is thrown
     * @return {@code value} if not {@code null} or empty
     * @throws RequirementFailure if {@code value} is {@code null} or empty
     */
    public static <K,V,M extends Map<K,V>> M nonEmpty( M value, Supplier<String> messageSupplier )
    {
    	
    	return Require.trueFor( value, value != null && ! value.isEmpty(), messageSupplier );
    	
    }
    
    
}
