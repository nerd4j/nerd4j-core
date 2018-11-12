package org.nerd4j.lang;

import java.util.Iterator;

import org.nerd4j.lang.array.*;
import org.nerd4j.util.Require;

/**
 * Factory to convert objects of type {@code array} into {@link Iterator}.
 * 
 * <p>
 * This class handles also arrays of primitive types.  
 * Unlike the java {@code stream API}, this class
 * handles all type of primitives, also {@code byte},
 * {@code short} and {@code float}.
 * 
 * <p>
 * Implementing the {@link Iterator} interface forces primitive types
 * to be boxed into objects of type {@link Number}. This can be very
 * inefficient for big amounts of data.
 * 
 * 
 * @author Nerd4j Team
 */
public class ArrayIterator
{
	
	/**
	 * Creates a new {@link Iterator} of {@link Byte}.
	 * 
	 * @param array source array.
	 */
	public static Iterator<Byte> create( byte[] array )
	{
		
		final int length = Require.nonNull( array, "The source array is mandatory" ).length;
		return new ByteArrayIterator( array, 0, length );
		
	}
	
	/**
	 * Creates a new {@link Iterator} of {@link Byte}.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 */
	public static Iterator<Byte> create( byte[] array, int startInclusive, int endExclusive )
	{
		
		return new ByteArrayIterator( array, startInclusive, endExclusive );
		
	}
	
	
	/**
	 * Creates a new {@link Iterator} of {@link Short}.
	 * 
	 * @param array source array.
	 */
	public static Iterator<Short> create( short[] array )
	{
		
		final int length = Require.nonNull( array, "The source array is mandatory" ).length;
		return new ShortArrayIterator( array, 0, length );
		
	}
	
	/**
	 * Creates a new {@link Iterator} of {@link Short}.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 */
	public static Iterator<Short> create( short[] array, int startInclusive, int endExclusive )
	{
		
		return new ShortArrayIterator( array, startInclusive, endExclusive );
		
	}
	
	
	/**
	 * Creates a new {@link Iterator} of {@link Integer}.
	 * 
	 * @param array source array.
	 */
	public static Iterator<Integer> create( int[] array )
	{
		
		final int length = Require.nonNull( array, "The source array is mandatory" ).length;
		return new IntArrayIterator( array, 0, length );
		
	}
	
	/**
	 * Creates a new {@link Iterator} of {@link Integer}.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 */
	public static Iterator<Integer> create( int[] array, int startInclusive, int endExclusive )
	{
		
		return new IntArrayIterator( array, startInclusive, endExclusive );
		
	}
	
	
	/**
	 * Creates a new {@link Iterator} of {@link Long}.
	 * 
	 * @param array source array.
	 */
	public static Iterator<Long> create( long[] array )
	{
		
		final int length = Require.nonNull( array, "The source array is mandatory" ).length;
		return new LongArrayIterator( array, 0, length );
		
	}
	
	/**
	 * Creates a new {@link Iterator} of {@link Long}.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 */
	public static Iterator<Long> create( long[] array, int startInclusive, int endExclusive )
	{
		
		return new LongArrayIterator( array, startInclusive, endExclusive );
		
	}
	
	
	/**
	 * Creates a new {@link Iterator} of {@link Float}.
	 * 
	 * @param array source array.
	 */
	public static Iterator<Float> create( float[] array )
	{
		
		final int length = Require.nonNull( array, "The source array is mandatory" ).length;
		return new FloatArrayIterator( array, 0, length );
		
	}
	
	/**
	 * Creates a new {@link Iterator} of {@link Float}.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 */
	public static Iterator<Float> create( float[] array, int startInclusive, int endExclusive )
	{
		
		return new FloatArrayIterator( array, startInclusive, endExclusive );
		
	}
	
	
	/**
	 * Creates a new {@link Iterator} of {@link Double}.
	 * 
	 * @param array source array.
	 */
	public static Iterator<Double> create( double[] array )
	{
		
		final int length = Require.nonNull( array, "The source array is mandatory" ).length;
		return new DoubleArrayIterator( array, 0, length );
		
	}
	
	/**
	 * Creates a new {@link Iterator} of {@link Double}.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 */
	public static Iterator<Double> create( double[] array, int startInclusive, int endExclusive )
	{
		
		return new DoubleArrayIterator( array, startInclusive, endExclusive );
		
	}
	
	
	/**
	 * Creates a new {@link Iterator} of objects of the given type.
	 * 
	 * @param <Type> the type of the objects in the array.
	 * @param array source array.
	 */
	public static <Type> Iterator<Type> create( Type[] array )
	{
		
		final int length = Require.nonNull( array, "The source array is mandatory" ).length;
		return new ObjectArrayIterator<Type>( array, 0, length );
		
	}
	
	/**
	 * Creates a new {@link Iterator} of objects of the given type.
	 * 
	 * @param <Type>         the type of the objects in the array.
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 */
	public static <Type> Iterator<Type> create( Type[] array, int startInclusive, int endExclusive )
	{
		
		return new ObjectArrayIterator<Type>( array, startInclusive, endExclusive );
		
	}
	
	
	/**
	 * Creates a new {@link Iterator} from the given array, deducing
	 * the type of elements of the array by reflection.
	 * 
	 * @param array source array.
	 */
	public static Iterator<?> create( Object array )
	{
		
		if( ! Require.nonNull( array, "The source array is mandatory" ).getClass().isArray() )
			throw new IllegalArgumentException( "The given argument need to be of type array but was " + array.getClass() );
		
		if( array instanceof Object[] )
			return create( (Object[]) array );
		
		if( array instanceof int[] )
			return create( (int[]) array );
		
		if( array instanceof double[] )
			return create( (double[]) array );
		
		if( array instanceof long[] )
			return create( (long[]) array );
		
		if( array instanceof float[] )
			return create( (float[]) array );
		
		if( array instanceof byte[] )
			return create( (byte[]) array );
		
		if( array instanceof short[] )
			return create( (short[]) array );
		
		throw new IllegalArgumentException( "Unable to recognize array type " + array.getClass() ); 
		
	}
	
	/**
	 * Creates a new {@link Iterator} from the given array, deducing
	 * the type of elements of the array by reflection.
	 * 
	 * @param array          source array.
	 * @param startInclusive the first index to cover, inclusive.
	 * @param endExclusive   index immediately past the last index to cover. 
	 */
	public static Iterator<?> create( Object array, int startInclusive, int endExclusive )
	{
		
		if( ! Require.nonNull( array, "The source array is mandatory" ).getClass().isArray() )
			throw new IllegalArgumentException( "The given argument need to be of type array but was " + array.getClass() );
		
		if( array instanceof Object[] )
			return create( (Object[]) array, startInclusive, endExclusive );
		
		if( array instanceof int[] )
			return create( (int[]) array, startInclusive, endExclusive );
		
		if( array instanceof double[] )
			return create( (double[]) array, startInclusive, endExclusive );
		
		if( array instanceof long[] )
			return create( (long[]) array, startInclusive, endExclusive );
		
		if( array instanceof float[] )
			return create( (float[]) array, startInclusive, endExclusive );
		
		if( array instanceof byte[] )
			return create( (byte[]) array, startInclusive, endExclusive );
		
		if( array instanceof short[] )
			return create( (short[]) array, startInclusive, endExclusive );
		
		throw new IllegalArgumentException( "Unable to recognize array type " + array.getClass() ); 
		
	}
	
}
