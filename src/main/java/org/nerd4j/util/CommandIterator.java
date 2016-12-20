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
package org.nerd4j.util;




/**
 * The aim of this class is to iterate the same {@link Command} over each
 * element in the given array or {@link Iterable} object.
 * 
 * @author Nerd4j Team
 */
public final class CommandIterator
{
	
	
	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */
	
	
	/**
	 * Applies the given {@link Command} to each value in the given
	 * iterable object. If the provided object is not an array end
	 * is not {@link Iterable} nothing will be done.
	 * 
	 * @param command        the command to apply.
	 * @param iterableObject the object to iterate.
	 */
	public static void apply( Command command, Object iterableObject )
	{
		
		if( command == null || iterableObject == null ) return;
		
    	if( iterableObject instanceof Iterable<?> )
    		applyToIterable( command, (Iterable<?>) iterableObject );
    	
    	else if( iterableObject.getClass().isArray() )
    		applyToArray( command, iterableObject );
		
	}
	
	
	/* ***************** */
	/*  PRIVATE METHODS  */
	/* ***************** */
	
	
	/**
	 * Applies the given command to the given {@link Iterable}.
	 * 
	 * @param command  the command to apply.
	 * @param iterable the object to iterate.
	 */
    private static void applyToIterable( Command command, Iterable<?> iterable )
    {
    	
    	for( Object value : iterable )
    		command.executeOn( value );
    	
    }
	
    
    /**
     * Applies the given command to the given array.
     * 
     * @param command the command to apply.
     * @param array   the array to iterate.
     */
    private static void applyToArray( Command command, Object array )
	{
		
		if ( array instanceof Object[] )
			for( Object value : (Object[]) array )
	    		command.executeOn( value );
		
		else if ( array instanceof int[] )
			for( int value : (int[]) array )
	    		command.executeOn( value );
		
		else if ( array instanceof byte[] )
			for( byte value : (byte[]) array )
	    		command.executeOn( value );
		
		else if ( array instanceof long[] )
			for( long value : (long[]) array )
	    		command.executeOn( value );
		
		else if ( array instanceof short[] )
			for( short value : (short[]) array )
	    		command.executeOn( value );
		
		else if ( array instanceof boolean[] )
			for( boolean value : (boolean[]) array )
	    		command.executeOn( value );
		
		else if ( array instanceof float[] )
			for( float value : (float[]) array )
	    		command.executeOn( value );
		
		else if ( array instanceof double[] )
			for( double value : (double[]) array )
	    		command.executeOn( value );
		
		else if ( array instanceof char[] )
			for( char value : (char[]) array )
	    		command.executeOn( value );
		
	}
	
    
	/* *************** */
	/*  INNER CLASSES  */
	/* *************** */

	
	/**
	 * Simple implementation of the {@code Command Design Pattern} interface
	 * used by the {@link CommandIterator} to apply the requested operation
	 * to each element in the {@link Iterable} object.
	 * 
	 * @author Nerd4j Team
	 */
	public interface Command
	{
		
		/**
		 * Executes the command on the given object.
		 * 
		 * @param value the target of the command.
		 */
		public void executeOn( Object value );
		
	}
	
}
