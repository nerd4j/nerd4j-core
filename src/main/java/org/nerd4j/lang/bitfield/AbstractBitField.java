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

import java.io.Serializable;
import java.util.Arrays;

import org.nerd4j.lang.BitField;

/**
 * Base abstract implementation of the {@link BitField BitField&#60;I&#62;}
 * interface that uses a byte array to store the bits.
 * 
 * <p>
 * The size of the bit array is set to the minimum size needed to contain
 * the desired amount of bits. 
 * 
 * @author Nerd4j Team
 * 
 * @param <I> type of the index used to access the field.
 */
public abstract class AbstractBitField<I> implements BitField<I>, Serializable, Cloneable
{

	/** Default Serial Version UID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Size of the bit field, must be in the range:
	 * {@code (data.length - 1 * 8, data.length * 8]}.
	 */
	private final int size;
	
	/**
	 * Byte array to hold the values ​​of the bits. The data will be organized
	 * as follows: the bit with index x will be in the position
	 * {@code x % 8} of the byte {@code data[x/8]}.
	 * <p>
	 * This field cannot be {@code final} because it is modified by the
	 * method {@link AbstractBitField#clone()}.
	 */
	private byte[] data;

	
	/**
	 * Constructor with parameters.
	 * <p>
	 * Creates a bit filed with the given size.
	 * The resulting bit field will be able to store
	 * <tt>size</tt> bits.
	 * 
	 * @param size number of desired bits. 
	 * @throws IllegalArgumentException if the size is not strict positive.
	 */
	public AbstractBitField( int size )
	{
		
		super();
		
		if ( size < 1 )
			throw new IllegalArgumentException( "You must provide a size greater than 0." );
		
		this.size = size;
		
		/*
		 * Gets the length of the internal byte array
		 * given the desired size of the bit field. 
		 */
		final int length = lengthFromSize(size);		
		data = new byte[ length ];
		
	}
	

	/**
	 * Constructor with parameters.
	 * <p>
	 * Constructs a new bit field by decoding the specified array of bytes.
     * If the provided byte array is valid and not null it will be copied
     * into the bit field structure.
     * 
	 * <p>
	 * The provided copy if the byte array will not be used to prevent
	 * unexpected changes of the internal data.
	 * 
	 * @param data byte array containing the initial value of the bits.
	 * @throws IllegalArgumentException if the provided byte array is null or empty.
	 */
	public AbstractBitField( byte[] data )
	{
		
		super();
		
		if( data == null || data.length < 1 )
			throw new IllegalArgumentException("You must provide a not empty byte array.");
		
		
		/* Gets the size of the bit field given the byte array length. */
		this.size = sizeFromLength( data.length );
		
		/* Creates a copy of the provided byte array. */
		this.data = Arrays.copyOf( data, data.length );
		
	}

	
	/**
	 * Constructor with parameters.
	 * <p>
	 * Constructs a new bit field, with the given size,
	 * by decoding the specified array of bytes.
     * The provided byte array must be valid and not null and
     * the given size must be strict positive and not exceed
     * the number of bits representable by the byte array.
     * 
	 * <p>
	 * The provided copy if the byte array will not be used to prevent
	 * unexpected changes of the internal data.
	 * 
	 * @param size number of desired bits. 
	 * @param data byte array containing the initial value of the bits.
	 * @throws IllegalArgumentException if one of the parameters do not
	 *                                  satisfy the constraints.
	 */
	public AbstractBitField( int size, byte[] data )
	{
		
		super();
		
		if( data == null || data.length < 1 )
			throw new IllegalArgumentException( "You must provide a not empty byte array." );
		
		if( size < 1 )
			throw new IllegalArgumentException( "You must provide a size greater than 0." );

		if( ! checkCapacity(data,size) )
			throw new IllegalArgumentException( "The provided size exceeds the byte array capacity." );
		
		
		this.size = size;
		
		/* Creates a copy of the needed portion of the provided byte array. */
		final int length = lengthFromSize( size );
		this.data = Arrays.copyOf( data, length );
		
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
	protected abstract int evaluateIndex( I index ) throws NullPointerException;
	
	
	/* ***************** */
	/*  PRIVATE METHODS  */
	/* ***************** */
	
	
	/**
	 * Checks if the given byte array is able to contain
	 * the desired number of bits.
	 * 
	 * @param data byte array used to store data.
	 * @param size desired number of bits.
	 * 
	 * @return {@code true} if the byte array capacity fits the desired size.
	 */
	private boolean checkCapacity( byte[] data, int size )
	{
		
		return data.length * Byte.SIZE > size;
		
	}

	
	/**
	 * Evaluates the smallest length that allows the byte array
	 * to store the desired amount of bits.
	 * 
	 * @param size the desired amount of bits. 
	 * @return smallest byte array length.
	 */
	private int lengthFromSize( int size )
	{
		
		return (size - 1)  / Byte.SIZE + 1;
		
	}
	
	
	/**
	 * Evaluates the maximum number of bits that can be stored
	 * into a byte array with the given length.
	 * 
	 * @param length length of the byte array. 
	 * @return number of bits that can be stored.
	 */
	private int sizeFromLength( int length )
	{
		
		return length * Byte.SIZE;
		
	}

	
	/**
	 * Checks that the given index is in the range
	 * represented by this bit field.
	 * 
	 * @param index index to evaluate.
	 * 
	 * @throws IndexOutOfBoundsException if the given index is not in the range [0,size).
	 */
	private void checkIndex( int index )
	{
		
		if ( index < 0 || index >= size )
			throw new IndexOutOfBoundsException( "Index " + index + " out of range [0," + size + ")." );
		
	}
	
	/**
	 * Returns {@code true} if the bit in the given position
	 * is set to 1, {@code false} otherwise.
	 * 
	 * @param data     the byte containing the bit to evaluate.
	 * @param position the position of the bit in range [0,8).
	 *            
	 * @return {@code true} if the bit value is 1;<br />
	 *         {@code false} otherwise.
	 */
	private boolean boolAtPosition( byte data, int position )
	{
		
		/* We assume the bit position to be in range [0,8). */
		return ( (data >> position) & 1 ) == 0 ? false : true;
		
	}
	
	/**
	 * Returns the value of the bit in the given position.
	 * This method works with the internal index that must be
	 * an integer value in the range [0,size).
	 * 
	 * @param index index of the bit to read.
	 * @return the value of the bit.
	 * @throws IndexOutOfBoundsException
	 *             if the index is not in the range [0,size).
	 */
	private boolean getBitAtIndex( int index )
	{
		
		checkIndex( index );
		
		final int block    = index / Byte.SIZE;
		final int position = index % Byte.SIZE;
		
		if( block >= data.length )
			return false;
		
		return boolAtPosition( data[block] , position );
		
	}
	
	/**
	 * Sets the value of the bit in the given position.
	 * This method works with the internal index that must be
	 * an integer value in the range [0,size).
	 * 
	 * @param index index of the bit to write.
	 * @param value the value to set.
	 * @return the previous value of the bit.
	 * @throws IndexOutOfBoundsException
	 *             if the index is not in the range [0,size).
	 */
	private boolean setBitAtIndex( int index, boolean value )
	{
		
		checkIndex( index );
				
		final int block    = index / Byte.SIZE;
		final int position = index % Byte.SIZE;
		
		/* 
		 * Bit-mask needed to identify the bit,
		 * example: if position = 4, mask = 00001000.
		 */
		final byte mask = (byte) (1 << position);
		boolean old = boolAtPosition(data[ block ], position);
		
		if ( value )
			/* Sets the bit to 1. */
			data[ block ] |= mask;
		else
			/* Sets the bit to 0. */
			data[ block ] &= ~mask;
		
		return old;
		
	}
	
	/**
	 * Toggles the value of the bit in the given position.
	 * This method works with the internal index that must be
	 * an integer value in the range [0,size).
	 * 
	 * @param index index of the bit to toggle.
	 * @return the current value of the bit.
	 * @throws IndexOutOfBoundsException
	 *             if the index is not in the range [0,size).
	 */
	private boolean toggleBitAtIndex( int index )
	{
		
		checkIndex( index );
		
		final int block    = index / Byte.SIZE;
		final int position = index % Byte.SIZE;
		
		/* 
		 * Bit-mask needed to identify the bit,
		 * example: if position = 4, mask = 00001000.
		 */
		final byte mask = (byte) (1 << position);
		
		data[ block ] ^= mask;
		
		return boolAtPosition(data[ block ], position);
		
	}
	
	
	/* ******************* */
	/*  INTERFACE METHODS  */
	/* ******************* */
	
	
	/**
	 * Returns the value of the bit in the position identified by the given index.
	 * 
	 * @param index the index of the bit to read.
	 * @return the boolean value representing the bit.
	 * 
	 * @throws NullPointerException if the given index is {@code null}.
	 * @throws IndexOutOfBoundsException 
	 *         if the given index is out of the bit field limits.
	 */
	@Override
	public boolean get(I index)
	{
		
		return getBitAtIndex( evaluateIndex( index ) );
		
	}

	/**
	 * Sets the value of the bit in the position identified by the given index.
	 * 
	 * @param index the index of the bit to set.
	 * @param value value to set the bit with.
	 * 
	 * @return the old value of the bit.
	 * 
	 * @throws NullPointerException if the given index is {@code null}.
	 * @throws IndexOutOfBoundsException 
	 *         if the given index is out of the bit field limits.
	 */
	@Override
	public boolean set(I index, boolean value)
	{
		
		return setBitAtIndex( evaluateIndex( index ), value );
		
	}
	
	/**
	 * Toggles the value of the bit in the position identified by the given index..
	 * 
	 * @param  index the index of the bit to toggle.
	 * @return the current value of the bit.
	 * 
	 * @throws NullPointerException if the given index is {@code null}.
	 * @throws IndexOutOfBoundsException 
	 *         if the given index is out of the bit field limits.
	 */
	@Override
	public boolean toggle( I index )
	{
		
		return toggleBitAtIndex( evaluateIndex( index ) );
		
	}
		
	/**
	 * Returns the size of the bit field.
	 * 
	 * @return the size of the bit field.
	 */
	@Override
	public int size()
	{
		
		return size;
		
	}
		
	/**
	 * This implementation makes a deep copy of the internal
	 * byte array to completely separate the new instance
	 * from its parent.
	 * <p>
	 * This method will not throw a {@link CloneNotSupportedException}
	 * because the this class implements {@link Cloneable}.
	 */
	@Override
	public AbstractBitField<I> clone()
	{
		
		try
		{
			
			@SuppressWarnings("unchecked")
			AbstractBitField<I> clone = (AbstractBitField<I>) super.clone();
			
			/* Makes a deep copy of the internal byte array. */
			clone.data = Arrays.copyOf( data, data.length );
			
			return clone;
			
		} catch ( CloneNotSupportedException e )
		{
			/* This point should never be reached. */
			throw new InternalError( e.toString() );
		}
		
	}
	
	
	/* ***************** */
	/*  PUBBLIC METHODS  */
	/* ***************** */
	
	
	/**
	 * Returns a byte array representation of this bit field.
	 * <p>
	 * Actually this method makes a copy of the byte array
	 * internally used to store the bits.
	 * 
	 * @return byte array representation of this bit field.
	 */
	public byte[] toByteArray()
	{
		
		return Arrays.copyOf( data, data.length );
		
	}

	
	/* ******************** */
	/*  COMPARISON METHODS  */
	/* ******************** */
	
	
	/**
	 * Simple implementation that demands to 
	 * {@link Arrays#hashCode(byte[])}.
	 * 
	 * @return the hash code for this array.
	 */
	@Override
	public int hashCode()
	{
		
		return Arrays.hashCode(data);
		
	}

	/**
	 * Checks the {@link Class} compatibility than demands
	 * to {@link Arrays#equals(byte[], byte[])} to check
	 * the content equality.
	 */
	@Override
	public boolean equals(Object obj)
	{
		
		if (this == obj) return true;		
		if (obj == null) return false;
		
		if( getClass() != obj.getClass() ) return false;
		
		@SuppressWarnings("unchecked")
		final AbstractBitField<I> other = (AbstractBitField<I>) obj;		
		return Arrays.equals(data, other.data);
		
	}
	
	/**
	 * Returns a simple human-readable string representation.
	 * 
	 * @return human-readable string representation.
	 */
	@Override
	public String toString()
	{
				
		final StringBuilder sb = new StringBuilder( size + 30);
		
		sb.append(this.getClass().getSimpleName() )
		  .append( "[size=" ).append( size )
		  .append(",data=" );
		
		if( data.length > 1 )	
			for( int i = 0; i < data.length-1; ++i )
			{			
				printByte( data[i], Byte.SIZE, sb );
				sb.append("-");
			}
		

		printByte( data[data.length-1], size % Byte.SIZE, sb );
		sb.append("]");
		
		return sb.toString();
		
	}
	
	
	/**
	 * Prints a single byte of the internal byte array.
	 * <p>
	 * It inverts the canonical representation of a byte
	 * printing the lowest-order bit in the leftmost
	 * position and the highest-order bit in the rightmost
	 * position.
	 * 
	 * @param b    the bite to print.
	 * @param size the number of bits to print.
	 * @param sb   the string buffer in which to write.
	 */
	private void printByte( byte b, int size, StringBuilder sb )
	{
		
		for( int k = 0; k < size; ++k )
			sb.append( b >>> k & 1 );
		
	}
	
}
