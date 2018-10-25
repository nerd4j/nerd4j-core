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
package org.nerd4j.lang;

import java.util.LinkedHashMap;
import java.util.Map;

import org.nerd4j.exception.RequirementFailure;
import org.nerd4j.util.Require;

/**
 * {@link Map} implementation able to remove oldest entries if maximum capacity
 * has been reached.
 * 
 * @param <K> type of the key object.
 * @param <V> type of the value object.
 * 
 * @author Nerd4j Team
 */
public class SpoolingLinkedHashMap<K,V> extends LinkedHashMap<K,V>
{
	
	/** Default Serial Version UID. */
	private static final long serialVersionUID = 1L;
	
	/** Default maximum capacity. */
	private static final int DEFAULT_MAX_ENTRIES = 128;
	
	/** Default initial capacity. */
	private static final int DEFAULT_INIT_ENTRIES = 16;
	
	/** Default load factor. */
	private static final float DEFAULT_LOAD_FACTOR = .75f;
	
	
	/** Configured maximum capacity. */
	private final int maximumCapacity;
	

    /**
	 * Constructs an empty insertion-ordered <tt>SpoolingLinkedHashMap</tt>
	 * instance with the default maximum capacity (128) and default initial
	 * capacity (16) and load factor (0.75).
	 */
    public SpoolingLinkedHashMap(  )
    {
    	
    	this( DEFAULT_MAX_ENTRIES );
    	
    }
    
    /**
	 * Constructs an empty insertion-ordered <tt>SpoolingLinkedHashMap</tt>
	 * instance with the specified maximum capacity and default initial capacity
	 * (16) and load factor (0.75).
	 * 
	 * @param maximumCapacity the maximum capacity supported.
	 * @throws RequirementFailure if the maximum capacity is smaller than
	 *         {@link #DEFAULT_INIT_ENTRIES}.
	 */
    public SpoolingLinkedHashMap( int maximumCapacity )
    {
    	
    	this( maximumCapacity, DEFAULT_INIT_ENTRIES );
    	
    }
    
    /**
	 * Constructs an empty insertion-ordered <tt>SpoolingLinkedHashMap</tt>
	 * instance with the specified maximum capacity and initial capacity and a
	 * default load factor (0.75).
	 * 
	 * @param  maximumCapacity the maximum capacity supported
	 * @param  initialCapacity the initial capacity
	 * @throws IllegalArgumentException if the initial capacity is negative.
	 * @throws RequirementFailure if the maximum capacity is smaller than
     *         the initial capacity.
	 */
    public SpoolingLinkedHashMap( int maximumCapacity, int initialCapacity )
    {
    	
    	this( maximumCapacity, initialCapacity, DEFAULT_LOAD_FACTOR );
    	
    }
    
    /**
     * Constructs an empty insertion-ordered <tt>SpoolingLinkedHashMap</tt>
     * instance with the specified maximum capacity, initial capacity and load
     * factor.
     *
	 * @param  maximumCapacity the maximum capacity supported.
     * @param  initialCapacity the initial capacity.
     * @param  loadFactor      the load factor.
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the load factor is non positive.
     * @throws RequirementFailure if the maximum capacity is smaller than
     *         the initial capacity.
     */
    public SpoolingLinkedHashMap( int maximumCapacity, int initialCapacity, float loadFactor )
    {
    	
        this( maximumCapacity, initialCapacity, loadFactor, false );
        
    }
    
    /**
     * Constructs an empty <tt>SpoolingLinkedHashMap</tt> instance with the
     * specified maximum capacity, initial capacity, load factor and ordering
     * mode.
     *
	 * @param  maximumCapacity the maximum capacity supported.
     * @param  initialCapacity the initial capacity.
     * @param  loadFactor      the load factor.
     * @param  accessOrder     the ordering mode - <tt>true</tt> for
     *         access-order, <tt>false</tt> for insertion-order
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the load factor is non positive.
     * @throws RequirementFailure if the maximum capacity is smaller than
     *         the initial capacity.
     */
    public SpoolingLinkedHashMap( int maximumCapacity, int initialCapacity, float loadFactor, boolean accessOrder )
    {
    	
        super( initialCapacity, loadFactor, accessOrder );
        this.maximumCapacity = Require.trueFor( maximumCapacity, maximumCapacity >= initialCapacity,
        		() -> "The initial capacity " + initialCapacity +
        		      " must be less or equals to the maximum capacity " + maximumCapacity ); 
        
    }
    
    
    /**
     * Constructs an insertion-ordered <tt>SpoolingLinkedHashMap</tt> instance
     * with the same mappings as the specified map.
     * The <tt>SpoolingLinkedHashMap</tt> instance is created with the specified
     * maximum capacity (silently defaults to given map.size() if lower than
     * that) and a default load factor (0.75) and an initial capacity sufficient
     * to hold the mappings in the specified map.
     *
	 * @param  maximumCapacity the maximum capacity supported.
     * @param  source the map whose mappings are to be placed in this map.
     * @throws RequirementFailure if the specified map is null or the map
     *         size is greater than the {@code maximumCapacity}.
     */
    public SpoolingLinkedHashMap( int maximumCapacity, Map<? extends K, ? extends V> source )
    {
        
    	super( Require.nonNull(source, "The source map is mandatory") );
        
        this.maximumCapacity = Require.trueFor( maximumCapacity, maximumCapacity >= source.size(),
        		() -> "The maximum capacity " + maximumCapacity +
  		              " must be greater than the provided map size " + source.size() );  
        
    }
	
    
    /* ***************** */
    /*  EXTENSION HOOKS  */
    /* ***************** */
	

    /**
	 * {@inheritDoc}
	 */
    @Override
    protected boolean removeEldestEntry( Map.Entry<K,V> eldest )
    {
    	
    	return size() > maximumCapacity;
    	
    }
	
}