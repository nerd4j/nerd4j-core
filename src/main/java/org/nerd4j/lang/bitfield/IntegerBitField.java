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
package org.nerd4j.lang.bitfield;


/**
 * Simple extension of {@link AbstractBitField}
 * that handles indexes of type {@link Integer}.
 * 
 * @author Nerd4j Team
 */
public class IntegerBitField extends AbstractBitField<Integer>
{

	/** Default Serial Version UID. */
	private static final long serialVersionUID = 1L;
	
	
	/**
	 * Default constructor.
	 * <p>
	 * Is intended to be used by reflection only.
	 *  
	 */
	private IntegerBitField()
	{
		
		super( 1 );
		
	}
	
	
	/**
	 * Constructor with parameters.
	 * 
	 * <p>
	 * Creates a bit filed with the given size.
	 * The resulting bit field will be able to store
	 * {@code size} bits.
	 * 
	 * @param size number of desired bits. 
	 * @throws IllegalArgumentException if the size is not strict positive.
	 */
	public IntegerBitField( int size )
	{
		
		super( size );
		
	}
	

	/**
	 * Constructor with parameters.
	 * <p>
	 * Constructs a new bit field by decoding the specified array of bytes.
     * If the provided byte array is valid and not null it will be copied
     * into the bit field structure.
	 * <p>
	 * The provided copy if the byte array will not be used to prevent
	 * unexpected changes of the internal data.
	 * 
	 * @param data byte array containing the initial value of the bits.
	 * @throws IllegalArgumentException if the provided byte array is null or empty.
	 */
	public IntegerBitField( byte[] data )
	{
		
		super( data );
		
	}

	
	/**
	 * Constructor with parameters.
	 * <p>
	 * Constructs a new bit field, whith the given size,
	 * by decoding the specified array of bytes.
     * The provided byte array must be valid and not null and
     * the given size nust be strict positiveand not exceed 
     * the number of bits representable by the byte array.
	 * <p>
	 *  The provided copy if the byte array will not be used to prevent
	 *  unexpected changes of the internal data.
	 * 
	 * @param size number of desired bits. 
	 * @param data byte array containing the initial value of the bits.
	 * @throws IllegalArgumentException if one of the parameters do not
	 *                                  satisfy the constraints.
	 */
	public IntegerBitField( int size, byte[] data )
	{
		
		super( size, data );
		
	}

	
	/* ***************** */
	/*  EXTENSION HOOKS  */
	/* ***************** */
	
	
	/**
	 * Performs the translation between the provided index
	 * and that used internally.
	 * <p>
	 * This method must return an integer in the range [0,size).
	 * In the other case a {@link IndexOutOfBoundsException} will
	 * be thrown when accessing the byte array.
	 * 
	 * @param index the index to be translated.	 * 
	 * @return the index to be used internally.
	 * 
	 * @throws NullPointerException if the provided index is null.
	 */
	@Override
	protected int evaluateIndex( Integer index ) throws NullPointerException
	{
		
		return index.intValue();
		
	}
	
}
