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

import org.nerd4j.lang.BitField;
import org.nerd4j.util.EqualsUtils;
import org.nerd4j.util.HashCoder;

/**
 * Alternative abstract implementation of the {@link BitField
 * BitField&#60;I&#62;} interface that uses a long parameter to store the bits.
 * <p>
 * Can be useful for smaller bit fields up to 64 fields.
 * 
 * @author Nerd4j Team
 * 
 * @param <I> type of the index used to access the field.
 */
public abstract class AbstractTinyBitField<I> implements BitField<I>, Serializable, Cloneable
{

	/** Default Serial Version UID. */
	private static final long serialVersionUID = 1L;
	
	/** Size of the bit field. */
	private final int size;
	
	/** Long parameter to hold the values ​​of the bits. */
	private long data;

	/**
	 * Default constructor.
	 * <p>
	 * Creates a bit filed with the maximum size (64). The
	 * resulting bit field will be able to store <tt>64</tt>
	 * bits.
	 */
	public AbstractTinyBitField()
	{
		
		this( Long.SIZE, 0l );
		
	}
	
	/**
	 * Constructor with parameters.
	 * <p>
	 * Creates a bit filed with the given size. The resulting bit field will be
	 * able to store <tt>size</tt> bits.
	 * 
	 * @param size number of desired bits.
	 * @throws IllegalArgumentException
	 *             if the size is not strict positive or greater than 64.
	 */
	public AbstractTinyBitField( int size )
	{
		
		this( size, 0l );
		
	}
	
	/**
	 * Constructor with parameters.
	 * <p>
	 * Constructs a new bit field by decoding the specified long.
	 * 
	 * @param size number of desired bits.
	 * @param data long containing the initial value of the bits.
	 */
	public AbstractTinyBitField( int size, long data )
	{
		
		super();
		
		if ( size < 1 )
			throw new IllegalArgumentException( "You must provide a size greater than 0." );
		
		if ( size > Long.SIZE )
			throw new IllegalArgumentException( "You must provide a size lesser than 64." );
		
		this.size = size;
		
		/* Bitmasked to exclude spurious unneeded values. */
		this.data = data & ( -1l >>> Long.SIZE - size );
		
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
		
		return ( (data >> index) & 1 ) == 0 ? false : true;
		
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
		
		/* 
		 * Bit-mask needed to identify the bit,
		 * example: if position = 4, mask = 00001000.
		 */
		final long mask = (long) ( 1 << index );
		boolean old = getBitAtIndex( index );
		
		if ( value )
			/* Sets the bit to 1. */
			data |= mask;
		else
			/* Sets the bit to 0. */
			data &= ~mask;
		
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
		
		/* 
		 * Bit-mask needed to identify the bit,
		 * example: if position = 4, mask = 00001000.
		 */
		final long mask = (long) ( 1 << index );
		
		data ^= mask;
		
		return getBitAtIndex( index );
		
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
	 * This method will not throw a {@link CloneNotSupportedException}
	 * because the this class implements {@link Cloneable}.
	 */
	@Override
	public AbstractTinyBitField<I> clone()
	{
		
		try
		{
			
			@SuppressWarnings("unchecked")
			AbstractTinyBitField<I> clone = (AbstractTinyBitField<I>) super.clone();
			
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
	 * 
	 * @return byte array representation of this bit field.
	 */
	public byte[] toByteArray()
	{
		
		byte[] result = new byte[8];
		
		result[0] = (byte) (data & 0xFFl);
		result[1] = (byte) (data >> 8 & 0xFFl);
		result[2] = (byte) (data >> 16 & 0xFFl);
		result[3] = (byte) (data >> 24 & 0xFFl);
		result[4] = (byte) (data >> 32 & 0xFFl);
		result[5] = (byte) (data >> 40 & 0xFFl);
		result[6] = (byte) (data >> 48 & 0xFFl);
		result[7] = (byte) (data >> 56 & 0xFFl);
		
		return result;
		
	}
	
	/**
	 * Returns a byte array representation of this bit field.
	 * 
	 * @return byte array representation of this bit field.
	 */
	public long toLong()
	{
		
		return data;
		
	}

	
	/* ******************** */
	/*  COMPARISON METHODS  */
	/* ******************** */
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		
		return HashCoder.hashCode( 79, data );
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		
		if( this == obj ) return true;		
		
		final AbstractTinyBitField<I> other = EqualsUtils.castIfSameClass( this, obj );
		return other != null && data == other.data;
		
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
		
		int blocks = ((size - 1) / 8) + 1;
		
		if ( blocks > 1 )
			for( int i = 0; i < blocks-1; ++i )
			{			
				printByte( (byte) ((data >> i*8) & 0xFFl), Byte.SIZE, sb );
				sb.append("-");
			}
		
		/*
		 * Checks how many bits remains to print in the last byte.
		 * If size % Byte.SIZE == 0 remains a full byte to print.
		 */
		int lastSize = size % Byte.SIZE;
		lastSize = lastSize == 0 ? Byte.SIZE : lastSize;
		
		printByte( (byte) ((data >> (blocks-1)*8) & 0xFFl), lastSize, sb );
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
