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
package org.nerd4j.util;

import java.util.Arrays;
import java.util.Iterator;

/**
 * Utility class providing the most common operations
 * used inside the method {@link Object#equals(Object)}.
 *
 * @author Nerd4j Team
 */
public class EqualsUtils
{

	
	/**
	 * This method is intended to be used inside a {@link Object#equals(Object)}
	 * to check if the object to equals is not {@code null} and if it has the
	 * same class as {@code this} object.
	 *  
	 * @param otherObj  the object with which to compare.
	 * @param thisClass the class the {@code otherObj} needs to have.
	 * @param <Type> the returned type.
	 * @return the {@code otherObj} casted to the right class if possible, {@code null} otherwise.
	 */
	@SuppressWarnings("unchecked")
	public static <Type> Type castIfSameClass( Object otherObj, Class<Type> thisClass )
	{
		
		if( otherObj != null && otherObj.getClass().equals(thisClass) )
			return (Type) otherObj;
		else
			return null;
		
	}
	
	
	/**
	 * This method is intended to be used inside a {@link Object#equals(Object)}
	 * to check if the object to equals is not {@code null} and if its class
	 * is an instance of {@code thisClass}.
	 *  
	 * @param otherObj  the object with which to compare.
	 * @param thisClass the class the {@code otherObj} needs to have.
	 * @param <Type> the returned type.
	 * @return the {@code otherObj} casted to the right class if possible, {@code null} otherwise.
	 */
	@SuppressWarnings("unchecked")
	public static <Type> Type castIfInstanceOf( Object otherObj, Class<Type> thisClass )
	{
		
		if( otherObj != null && thisClass.isInstance(otherObj) )
			return (Type) otherObj;
		else
			return null;
		
	}
	
    
    /**
     * This method is intended to be used inside a {@link Object#equals(Object)}
     * and tells if the field of {@code thisObject} equals to the related field
     * of the {@code otherObject}.
     * <p>
     * <b>Note</b>
     * This method checks equality by invoking
     * the {@link #equals(Object)} method on the
     * given objects, so it can't be used to check
     * {@code thisObject} and the {@code otherObject}
     * itself.
     * 
     * @param thisField  field of  {@code thisObject}.
     * @param otherField related field of the {@code otherObject}.
     * @param <Field> the type of the fields to check.
     * @return {@code true} if the two fields are equal.
     */
    public static <Field> boolean equalsFields( Field thisField, Field otherField )
    {
    	
    	/* This check works also for thisField == otherField == null. */
    	if( thisField == otherField ) return true;
    	
    	/* Checks if (thisField == null && otherField != null) || (thisField != null && otherField == null). */
    	if( thisField == null ^ otherField == null ) return false;
    	
    	/*
    	 * If we are at this point then the given
    	 * objects are not the same instance and
    	 * both not null.
    	 */
    	
    	if( thisField.getClass().isArray() )
    		return equalsArrays( thisField, otherField );
    	
    	if( thisField instanceof Iterable )
    		return equalsIterables( (Iterable<?>) thisField, (Iterable<?>) otherField );
    	
    	return thisField.equals( otherField );
    	
    }
    
    
    /**
     *  This method is intended to be used inside a {@link Object#equals(Object)}
     * and tells if the list of related fields are equals.
	 * <p>
	 * The list of arguments must be in the form:
	 * {@code thisFieldA, otherFieldA, thisFieldB, otherFieldB, ...}
	 * otherwise the method will not be able to perform the check properly.
	 * 
	 * @param thisField  field of  {@code thisObject}.
     * @param otherField related field of the {@code otherObject}.
     * @param others     the other pairs {@code <thisField,otherField>}.
     * @param <Field> the type of the fields to check.
	 * @return {@code true} if the given objects are two by two equal.
	 * @throws IllegalArgumentException If the number of arguments is inconsistent.
	 */
    public static <Field> boolean equalsFields( Field thisField, Field otherField, Field... others )
    {

   		if( others != null && others.length % 2 != 0 )
    		throw new IllegalArgumentException( "Number of arguments must be even but was " + (others.length + 2) );
    	
    	if( ! equalsFields(thisField, otherField) ) return false;
    	
    	if( others != null && others.length > 0 )
    		for( int i = 0; i < others.length; ++i )
    			if( ! equalsFields(others[i], others[++i]) )
    				return false;

    	return true;

    }

    
    /**
     * This method is intended to be used inside a {@link Object#equals(Object)}
     * and tells if the field of {@code thisObject} equals to the related field
     * of the {@code otherObject}.
     * <p>
     * If the given objects are {@link Iterable} or
     * they are arrays this method iterates the
     * process to each contained element. 
     * <p>
     * <b>Note</b>
     * This method checks equality by invoking
     * the {@link #equals(Object)} method on the
     * given objects, so it can't be used to check
     * {@code thisObject} and the {@code otherObject}
     * itself.
     * 
     * @param thisField  field of  {@code thisObject}.
     * @param otherField related field of the {@code otherObject}.
     * @param <Field> the type of the fields to check.
     * @return {@code true} if the two fields are equal.
     */
    public static <Field> boolean deepEqualsFields( Field thisField, Field otherField )
    {
    	
    	/* This check works also for thisField == otherField == null. */
    	if( thisField == otherField ) return true;
    	
    	/* Checks if (thisField == null && otherField != null) || (thisField != null && otherField == null). */
    	if( thisField == null ^ otherField == null ) return false;
    	
    	/*
    	 * If we are at this point then the given
    	 * objects are not the same instance and
    	 * both not null.
    	 */
    	
    	if( thisField.getClass().isArray() )
    		return deepEqualsArrays( thisField, otherField );
    	
    	if( thisField instanceof Iterable )
    		return deepEqualsIterables( (Iterable<?>) thisField, (Iterable<?>) otherField );
    	
    	return thisField.equals( otherField );
    	
    }

    
    /**
     *  This method is intended to be used inside a {@link Object#equals(Object)}
     * and tells if the list of related fields are equals.
	 * <p>
	 * The list of arguments must be in the form:
	 * {@code thisFieldA, otherFieldA, thisFieldB, otherFieldB, ...}
	 * otherwise the method will not be able to perform the check properly.
	 * <p>
     * If some of the given fields are {@link Iterable} or
     * arrays this method iterates the process to each
     * contained element. 
	 * 
	 * @param thisField  field of  {@code thisObject}.
     * @param otherField related field of the {@code otherObject}.
     * @param others     the other pairs {@code <thisField,otherField>}.
     * @param <Field> the type of the fields to check.
	 * @return {@code true} if the given objects are two by two equal.
	 * @throws IllegalArgumentException If the number of arguments is inconsistent.
	 */
    public static <Field> boolean deepEqualsFields( Field thisField, Field otherField, Field... others )
    {
    	
    	if( others != null && others.length % 2 == 1 )
    		throw new IllegalArgumentException( "Number of arguments must be even but was " + (others.length + 2) );
    	
    	if( ! deepEqualsFields(thisField, otherField) ) return false;
    	
    	if( others != null && others.length > 0 )
    		for( int i = 0; i < others.length; ++i )
    			if( ! deepEqualsFields(others[i], others[++i]) )
    				return false;
    	
    	return true;
    	
    }
    
    
    /* ***************** */
    /*  PRIVATE METHODS  */
    /* ***************** */
    
    
    /**
	 * Tells if the two {@link Iterable} are equal.
	 * 
	 * @param fst first {@link Iterable} to check.
	 * @param snd second {@link Iterable} to check.
	 * @return {@code true} if the two {@link Iterable} are equal.
	 */
    private static boolean equalsIterables( Iterable<?> fst, Iterable<?> snd )
    {
    	
    	final Iterator<?> fstIter = fst.iterator();
    	final Iterator<?> sndIter = snd.iterator();
    	
    	Object fstValue, sndValue;
    	while( fstIter.hasNext() )
    	{
    		if( ! sndIter.hasNext() )
    			return false;
    		
    		fstValue = fstIter.next();
    		sndValue = sndIter.next();
    		
    		if( ! equalsFields(fstValue, sndValue) )
    			return false;
    		
    	}
    	
    	return true;
    	
    }
    
    
    /**
	 * Tells if the two arrays are equal.
	 * 
	 * @param fst first array to check.
	 * @param snd second array to check.
	 * @return {@code true} if the two arrays are equal.
	 */
	private static boolean equalsArrays( Object fst, Object snd )
	{
		
		if ( fst instanceof Object[] )
			return Arrays.equals( (Object[])fst, (Object[])snd );
		
		else if ( fst instanceof int[] )
			return Arrays.equals( (int[])fst, (int[])snd );
		
		else if ( fst instanceof byte[] )
			return Arrays.equals( (byte[])fst, (byte[])snd );
		
		else if ( fst instanceof long[] )
			return Arrays.equals( (long[])fst, (long[])snd );
		
		else if ( fst instanceof short[] )
			return Arrays.equals( (short[])fst, (short[])snd );
		
		else if ( fst instanceof boolean[] )
			return Arrays.equals( (boolean[])fst, (boolean[])snd );
		
		else if ( fst instanceof float[] )
			return Arrays.equals( (float[])fst, (float[])snd );
		
		else if ( fst instanceof double[] )
			return Arrays.equals( (double[])fst, (double[])snd );
		
		else if ( fst instanceof char[] )
			return Arrays.equals( (char[])fst, (char[])snd );	
		
		return false;
		
	}
	
	
	/**
	 * Tells if the two {@link Iterable} are equal
	 * and iterates the process for each value.
	 * 
	 * @param fst first {@link Iterable} to check.
	 * @param snd second {@link Iterable} to check.
	 * @return {@code true} if the two {@link Iterable} are equal.
	 */
	private static boolean deepEqualsIterables( Iterable<?> fst, Iterable<?> snd )
	{
		
		final Iterator<?> fstIter = fst.iterator();
		final Iterator<?> sndIter = snd.iterator();
		
		Object fstValue, sndValue;
		while( fstIter.hasNext() )
		{
			if( ! sndIter.hasNext() )
				return false;
			
			fstValue = fstIter.next();
			sndValue = sndIter.next();
			
			if( ! deepEqualsFields(fstValue, sndValue) )
				return false;
			
		}
		
		return true;
		
	}
	
	
	/**
	 * Tells if the two arrays are equal
	 * and iterates the process for each
	 * couple of values in the arrays.
	 * 
	 * @param fst first array to deep check.
	 * @param snd second array to deep check.
	 * @return {@code true} if the two arrays are deeply equal.
	 */
	private static boolean deepEqualsArrays( Object fst, Object snd )
	{
		
		if ( fst instanceof Object[] )
		{
		
			final Object[] fstArray = (Object[]) fst;
			final Object[] sndArray = (Object[]) snd;
			
			if( fstArray.length != sndArray.length ) return false;
			
			for( int i = 0; i < fstArray.length; ++i )
				if( ! deepEqualsFields(fstArray[i], sndArray[i]) )
					return false;
			
			return true;
			
		}
		
		else if ( fst instanceof int[] )
			return Arrays.equals( (int[])fst, (int[])snd );
		
		else if ( fst instanceof byte[] )
			return Arrays.equals( (byte[])fst, (byte[])snd );
		
		else if ( fst instanceof long[] )
			return Arrays.equals( (long[])fst, (long[])snd );
		
		else if ( fst instanceof short[] )
			return Arrays.equals( (short[])fst, (short[])snd );
		
		else if ( fst instanceof boolean[] )
			return Arrays.equals( (boolean[])fst, (boolean[])snd );
		
		else if ( fst instanceof float[] )
			return Arrays.equals( (float[])fst, (float[])snd );
		
		else if ( fst instanceof double[] )
			return Arrays.equals( (double[])fst, (double[])snd );
		
		else if ( fst instanceof char[] )
			return Arrays.equals( (char[])fst, (char[])snd );	
		
		return false;
		
	}
    
}
