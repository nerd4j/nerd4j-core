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

/**
 * {@link Map} implementation able to remove oldest entries if maximum capacity
 * has been reached.
 * 
 * @author Nerd4j Team
 */
public class SpoolingLinkedHashMap<K,V> extends LinkedHashMap<K,V>
{
	
	/** Default Serial Version UID. */
	private static final long serialVersionUID = 1L;
	
	/** Default maximum capacity. */
	private static final int DEFAULT_MAX_ENTRIES = 128;
	
	/** Configured maximum capacity. */
	private final int maximumCapacity;
	
	
    /**
     * Constructs an empty insertion-ordered <tt>SpoolingLinkedHashMap</tt>
     * instance with the specified maximum capacity, initial capacity and load
     * factor.
     *
	 * @param  maximumCapacity the maximum capacity supported
     * @param  initialCapacity the initial capacity
     * @param  loadFactor      the load factor
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the load factor is nonpositive
     */
    public SpoolingLinkedHashMap(int maximumCapacity, int initialCapacity, float loadFactor)
    {
        super(initialCapacity, loadFactor);
        this.maximumCapacity = initialCapacity < maximumCapacity ? maximumCapacity : initialCapacity;
    }

    /**
	 * Constructs an empty insertion-ordered <tt>SpoolingLinkedHashMap</tt>
	 * instance with the specified maximum capacity and initial capacity and a
	 * default load factor (0.75).
	 * 
	 * @param  maximumCapacity the maximum capacity supported
	 * @param  initialCapacity the initial capacity
	 * @throws IllegalArgumentException if the initial capacity is negative
	 */
    public SpoolingLinkedHashMap(int maximumCapacity, int initialCapacity)
    {
    	super( initialCapacity );
    	this.maximumCapacity = initialCapacity < maximumCapacity ? maximumCapacity : initialCapacity;
    }

    /**
	 * Constructs an empty insertion-ordered <tt>SpoolingLinkedHashMap</tt>
	 * instance with the specified maximum capacity and default initial capacity
	 * (16) and load factor (0.75).
	 * 
	 * @param maximumCapacity the maximum capacity supported
	 */
    public SpoolingLinkedHashMap( int maximumCapacity )
    {
    	super();
    	this.maximumCapacity = maximumCapacity;
    }
    
	/**
	 * Constructs an empty insertion-ordered <tt>SpoolingLinkedHashMap</tt>
	 * instance with the default maximum capacity (100) and default initial
	 * capacity (16) and load factor (0.75).
	 */
    public SpoolingLinkedHashMap()
    {
    	super();
    	this.maximumCapacity = DEFAULT_MAX_ENTRIES;
    }
    

    /**
     * Constructs an insertion-ordered <tt>SpoolingLinkedHashMap</tt> instance
     * with the same mappings as the specified map.
     * The <tt>SpoolingLinkedHashMap</tt> instance is created with the specified
     * maximum capacity (silently defaults to given map.size() if lower than
     * that) and a default load factor (0.75) and an initial capacity sufficient
     * to hold the mappings in the specified map.
     *
	 * @param  maximumCapacity the maximum capacity supported
     * @param  m the map whose mappings are to be placed in this map
     * @throws NullPointerException if the specified map is null
     */
    public SpoolingLinkedHashMap( int maximumCapacity, Map<? extends K, ? extends V> m)
    {
        super(m);
        this.maximumCapacity = m.size() < maximumCapacity ? maximumCapacity : m.size();
    }

    
    /**
     * Constructs an empty <tt>SpoolingLinkedHashMap</tt> instance with the
     * specified maximum capacity, initial capacity, load factor and ordering
     * mode.
     *
	 * @param  maximumCapacity the maximum capacity supported
     * @param  initialCapacity the initial capacity
     * @param  loadFactor      the load factor
     * @param  accessOrder     the ordering mode - <tt>true</tt> for
     *         access-order, <tt>false</tt> for insertion-order
     * @throws IllegalArgumentException if the initial capacity is negative
     *         or the load factor is nonpositive
     */
    public SpoolingLinkedHashMap(int maximumCapacity,
    		                     int initialCapacity,
    		                     float loadFactor,
                                 boolean accessOrder)
    {
        super(initialCapacity,loadFactor,accessOrder);
        this.maximumCapacity = initialCapacity < maximumCapacity ? maximumCapacity : initialCapacity;
    }
	
	

    /**
	 * Returns <tt>true</tt> if this map should remove its eldest entry. This
	 * method is invoked by <tt>put</tt> and <tt>putAll</tt> after inserting
	 * a new entry into the map. It provides the implementor with the
	 * opportunity to remove the eldest entry each time a new one is added. This
	 * is useful if the map represents a cache: it allows the map to reduce
	 * memory consumption by deleting stale entries.
	 * 
	 * <p>
	 * Will return true if:
	 * <ol>
	 * <li> the element has expired
	 * </ol>
	 * </p>
	 * 
	 * @param eldest
	 *            The least recently inserted entry in the map, or if this is an
	 *            access-ordered map, the least recently accessed entry. This is
	 *            the entry that will be removed it this method returns
	 *            <tt>true</tt>. If the map was empty prior to the
	 *            <tt>put</tt> or <tt>putAll</tt> invocation resulting in
	 *            this invocation, this will be the entry that was just
	 *            inserted; in other words, if the map contains a single entry,
	 *            the eldest entry is also the newest.
	 * @return <tt>true</tt> if the eldest entry should be removed from the
	 *         map; <tt>false</tt> if it should be retained.
	 */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest)
    {
    	return size() > maximumCapacity;
    }
	
}