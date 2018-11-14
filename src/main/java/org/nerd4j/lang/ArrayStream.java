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
package org.nerd4j.lang;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Factory to convert objects of type {@code array} into {@link Stream}.
 * 
 * <p>
 * This class handles also arrays of primitive types.  
 * Unlike the java {@code stream API}, this class
 * handles all type of primitives, also {@code byte},
 * {@code short} and {@code float}.
 * 
 * <p>
 * To keep a common interface and allow the method {@link #create(Object, int, int, boolean)}
 * to deduce the right type by reflection all methods are returning object streams
 * also when primitive streams are available.
 * 
 * <p>
 * This approach is inefficient but provides an easy and clean {@code API}
 * to stream elements in arrays.
 * 
 * @author Nerd4j Team
 */
public class ArrayStream
{
	
	/**
	 * Creates a new {@link Stream} of {@link Byte} reading the elements
	 * of the given array.
	 * 
	 * @param array    source array.
	 * @param parallel tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of {@link Byte}.
	 */
	public static Stream<Byte> create( byte[] array, boolean parallel )
	{
		
		final Iterator<Byte> iterator = ArrayIterator.create( array );
		return create( iterator, array.length, parallel );
		
	}
	
	/**
	 * Creates a new {@link Stream} of {@link Byte} reading the elements
	 * of the given array.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 * @param parallel       tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of {@link Byte}.
	 */
	public static Stream<Byte> create( byte[] array, int startInclusive, int endExclusive, boolean parallel )
	{
		
		final Iterator<Byte> iterator = ArrayIterator.create( array, startInclusive, endExclusive );
		return create( iterator, endExclusive - startInclusive, parallel );
		
	}

	
	/**
	 * Creates a new {@link Stream} of {@link Short} reading the elements
	 * of the given array.
	 * 
	 * @param array    source array.
	 * @param parallel tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of {@link Short}.
	 */
	public static Stream<Short> create( short[] array, boolean parallel )
	{
		
		final Iterator<Short> iterator = ArrayIterator.create( array );
		return create( iterator, array.length, parallel );
		
	}
	
	/**
	 * Creates a new {@link Stream} of {@link Short} reading the elements
	 * of the given array.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 * @param parallel       tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of {@link Short}.
	 */
	public static Stream<Short> create( short[] array, int startInclusive, int endExclusive, boolean parallel )
	{
		
		final Iterator<Short> iterator = ArrayIterator.create( array, startInclusive, endExclusive );
		return create( iterator, endExclusive - startInclusive, parallel );
		
	}
	
	
	/**
	 * Creates a new {@link Stream} of {@link Integer} reading the elements
	 * of the given array.
	 * 
	 * @param array    source array.
	 * @param parallel tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of {@link Integer}.
	 */
	public static Stream<Integer> create( int[] array, boolean parallel )
	{
		
		final Iterator<Integer> iterator = ArrayIterator.create( array );
		return create( iterator, array.length, parallel );
		
	}
	
	/**
	 * Creates a new {@link Stream} of {@link Integer} reading the elements
	 * of the given array.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 * @param parallel       tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of {@link Integer}.
	 */
	public static Stream<Integer> create( int[] array, int startInclusive, int endExclusive, boolean parallel )
	{
		
		final Iterator<Integer> iterator = ArrayIterator.create( array, startInclusive, endExclusive );
		return create( iterator, endExclusive - startInclusive, parallel );
		
	}
	
	
	/**
	 * Creates a new {@link Stream} of {@link Long} reading the elements
	 * of the given array.
	 * 
	 * @param array    source array.
	 * @param parallel tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of {@link Long}.
	 */
	public static Stream<Long> create( long[] array, boolean parallel )
	{
		
		final Iterator<Long> iterator = ArrayIterator.create( array );
		return create( iterator, array.length, parallel );
		
	}
	
	/**
	 * Creates a new {@link Stream} of {@link Long} reading the elements
	 * of the given array.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 * @param parallel       tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of {@link Long}.
	 */
	public static Stream<Long> create( long[] array, int startInclusive, int endExclusive, boolean parallel )
	{
		
		final Iterator<Long> iterator = ArrayIterator.create( array, startInclusive, endExclusive );
		return create( iterator, endExclusive - startInclusive, parallel );
		
	}
	
	
	/**
	 * Creates a new {@link Stream} of {@link Float} reading the elements
	 * of the given array.
	 * 
	 * @param array    source array.
	 * @param parallel tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of {@link Float}.
	 */
	public static Stream<Float> create( float[] array, boolean parallel )
	{
		
		final Iterator<Float> iterator = ArrayIterator.create( array );
		return create( iterator, array.length, parallel );
		
	}
	
	/**
	 * Creates a new {@link Stream} of {@link Float} reading the elements
	 * of the given array.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 * @param parallel       tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of {@link Float}.
	 */
	public static Stream<Float> create( float[] array, int startInclusive, int endExclusive, boolean parallel )
	{
		
		final Iterator<Float> iterator = ArrayIterator.create( array, startInclusive, endExclusive );
		return create( iterator, endExclusive - startInclusive, parallel );
		
	}
	
	
	/**
	 * Creates a new {@link Stream} of {@link Double} reading the elements
	 * of the given array.
	 * 
	 * @param array    source array.
	 * @param parallel tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of {@link Double}.
	 */
	public static Stream<Double> create( double[] array, boolean parallel )
	{
		
		final Iterator<Double> iterator = ArrayIterator.create( array );
		return create( iterator, array.length, parallel );
		
	}
	
	/**
	 * Creates a new {@link Stream} of {@link Double} reading the elements
	 * of the given array.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 * @param parallel       tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of {@link Double}.
	 */
	public static Stream<Double> create( double[] array, int startInclusive, int endExclusive, boolean parallel )
	{
		
		final Iterator<Double> iterator = ArrayIterator.create( array, startInclusive, endExclusive );
		return create( iterator, endExclusive - startInclusive, parallel );
		
	}
	
	
	/**
	 * Creates a new {@link Stream} of objects of the given type.
	 * 
	 * @param <Type>   the type of the objects in the array.
	 * @param array    source array.
	 * @param parallel tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of objects of type {@code Type}.
	 */
	public static <Type> Stream<Type> create( Type[] array, boolean parallel )
	{
		
		final Iterator<Type> iterator = ArrayIterator.create( array );
		return create( iterator, array.length, parallel );
		
	}
	
	/**
	 * Creates a new {@link Stream} of objects of the given type.
	 * 
	 * @param <Type>   the type of the objects in the array.
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 * @param parallel       tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of objects of type {@code Type}.
	 */
	public static <Type> Stream<Type> create( Type[] array, int startInclusive, int endExclusive, boolean parallel )
	{
		
		final Iterator<Type> iterator = ArrayIterator.create( array, startInclusive, endExclusive );
		return create( iterator, endExclusive - startInclusive, parallel );
		
	}

	
	/**
	 * Creates a new {@link Stream} from the given array, deducing
	 * the type of elements of the array by reflection.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 * @param parallel       tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a new {@link Stream} of the appropriate type.
	 */
	public static Stream<?> create( Object array, int startInclusive, int endExclusive, boolean parallel )
	{
		
		final Iterator<?> iterator = ArrayIterator.create( array, startInclusive, endExclusive );
		return create( iterator, endExclusive - startInclusive, parallel );
		
	}
	
	
	
	
	
	/* ***************** */
	/*  PRIVATE METHODS  */
	/* ***************** */
	
	
	/**
	 * Creates a {@link Stream} of the given type starting from the given {@link Iterator}.
	 * 
	 * @param iterator the {@link Iterator} to stream.
	 * @param size     number of elements in the {@link Iterator}.
	 * @param parallel tells if the resulting {@link Stream} should be parallel or sequential.
	 * @return a {@link Stream} of the elements returned by the {@link Iterator}.
	 */
	private static <Type> Stream<Type> create( Iterator<Type> iterator, int size, boolean parallel )
	{
		
		final Spliterator<Type> spliterator = Spliterators.spliterator( iterator, size, 0 );
		return StreamSupport.stream( spliterator, parallel );
		
	}
	
}
