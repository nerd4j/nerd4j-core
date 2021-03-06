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
package org.nerd4j.lang.bitfield;



/**
 * Extension of {@link AbstractTinyBitField}
 * that handles indexes of type {@link Enum Enum&#60;E&#62;}.
 * 
 * <p>
 * Two EnumTinyBitField containing the same internal representation
 * but created for two different {@link Enum Enum&#60;E&#62;}
 * types are not comparable.
 * 
 * @author Nerd4j Team
 * 
 * @param <E> type of the {@link Enum Enum&#60;E&#62;} handled by this class.
 */
public class EnumTinyBitField<E extends Enum<E>> extends AbstractTinyBitField<E>
{

	/** Default Serial Version UID. */
	private static final long serialVersionUID = 1L;
	
	/** The type of the {@link Enum Enum&#60;E&#62;} to handle. */
	private final Class<E> enumClass;
	
	
	/**
	 * Default constructor.
	 * <p>
	 * Is intended to be used by reflection only.
	 *  
	 */
	private EnumTinyBitField()
	{
		
		super();
		
		enumClass = null;
		
	}
	
	
	/**
	 * Creates an {@link EnumTinyBitField} that handles
	 * {@link Enum Enum&#60;E&#62;} of the provided
	 * type.
	 * 
	 * @param enumType type of the {@link Enum Enum&#60;E&#62;} to be handled.
	 * 
	 * @throws IllegalArgumentException
	 *         if the provided class represents an empty {@link Enum Enum&#60;E&#62;}
	 *         enum constants are more than 64.
	 */
	public EnumTinyBitField( Class<E> enumType )
	{
		
		this( enumType, 0L );
		
	}
	
	
	/**
	 * Creates an {@link EnumTinyBitField} that handles {@link Enum Enum&#60;E&#62;}
	 * of the provided type, and fills the internal byte array with the given data.
	 * 
	 * @param clazz type of the {@link Enum Enum&#60;E&#62;} to be handled.
	 * @param data  initial data
	 * 
	 * @throws IllegalArgumentException
	 *         if the provided class represents an empty {@link Enum Enum&#60;E&#62;}
	 *         or enum constants are more than 64.
	 */
	public EnumTinyBitField( Class<E> clazz, long data )
	{
		
		super( clazz.getEnumConstants().length, data );
		
		enumClass = clazz;
		
	}
	
	
	/* ***************** */
	/*  EXTENSION HOOKS  */
	/* ***************** */
	

	/**
	 * Performs the translation between the provided index
	 * and that used internally.
	 * 
	 * <p>
	 * This method must return an integer in the range [0,size).
	 * In the other case a {@link IndexOutOfBoundsException} will
	 * be thrown when accessing the byte array.
	 * 
	 * @param index the index to be translated.
	 * @return the index to be used internally.
	 * 
	 * @throws NullPointerException if the provided index is null.
	 * @see EnumTinyBitField#check(Object)
	 */
	@Override
	protected int evaluateIndex( E index ) throws NullPointerException
	{
		
		check( index );
		return index.ordinal();
		
	}
	
	
	/* ***************** */
	/*  PRIVATE METHODS  */
	/* ***************** */
	
	
	/**
	 * Verify that the given instance is compatible with the enum type
	 * for which has been instantiated this object. The check is
     * necessary because otherwise it would be possible to perform
     * the following steps:
	 * 
	 * <pre>
	 * enum A { A1,A2,A3; }
	 * 
	 * enum B { B1,B2,B3; }
	 * 
	 * EnumBitField&#60;A&#62; bitFieldA = new EnumBitField&#60;A&#62;( A.class );
	 * bitFieldA.setBit(A.A1, true);
	 * 
	 * EnumBitField&#60;B&#62; bitFieldB = (EnumBitField&#60;B&#62;) (EnumBitField&#60;?&#62;) bitFieldA;
	 * bitFieldB.getBit(B.B1);
	 * </pre>
	 * 
	 * This method is called to ensure that no inconsistent class cast
	 * will be made, in case a {@link ClassCastException} will be thrown.
	 * 
	 * @param index index instance to evaluate.
	 * @throws ClassCastException if the provded index class is inconsistent.
	 */
	private void check( Object index )
	{
		
		if ( enumClass != index.getClass() )
			throw new ClassCastException( "Trying to cast " + index.getClass().getSimpleName() +
						                  " to " + enumClass.getSimpleName() + "." );
		
	}
	
	
	/* ******************** */
	/*  COMPARISON METHODS  */
	/* ******************** */
	

	/**
	 * Checks the given object is an  {@link EnumTinyBitField}
	 * whith the same internal data and at least check if
	 * both {@link EnumTinyBitField}s handle the same
	 * {@link Enum Enum&#60;E&#62;} type.
	 * 
	 * @param obj the object to evaluate.
	 */
	@Override
	public boolean equals( Object obj )
	{
		
		if( ! super.equals(obj) ) return false;
		
		final EnumTinyBitField<?> other = (EnumTinyBitField<?>) obj;		
		return this.enumClass.equals( other.enumClass );
		
	}
	
}
