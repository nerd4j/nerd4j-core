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
package org.nerd4j.util;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents a counter able to count the number of elements in a set
 * and the number of elements in each subset.
 * 
 * <p>
 * This class assumes that the relation between the elements of the
 * set induces a partition such that the sum of the elements in each
 * subset equals to the total number of elements in the set.
 * 
 * <p>
 * All operations in this class are {@code thread-safe}.
 * 
 * <p>
 * <b>NOTE</b>: Currently synchronization is very coarse should be refined.
 * 
 * @param <B> class representing each block in the partition.
 * 
 * @author Nerd4j Team
 */
public class PartitionCount<B>
{

    /** The total number of elements. */
    private AtomicInteger totalCount;
    
    /** Map to relate each block to the related count. */
    private Map<B, AtomicInteger> partitionCount;
	    
    
    /**
     * Default constructor.
     * 
     */
    public PartitionCount()
    {
    	
    	super();
    	
    	totalCount = new AtomicInteger( 0 );
    	
    	/*
    	 * It's necessary to instantiate a ConcurrentHashMap
    	 * due to the method getPartition() that allows to
    	 * iterate over the map entries that can be concurrently
    	 * modified.
    	 */
    	partitionCount = new ConcurrentHashMap<B, AtomicInteger>();
    	
    }
	
    
    /* **************** */
    /*  PUBLIC METHODS  */
    /* **************** */
    
    
    /**
     * Returns the total number of elements.
     * 
     * @return the total number of elements.
     */
    public int getTotalCount()
    {
    	
    	return totalCount.get();
    	
    }
    
    /**
     * Returns the number of elements in the given block.
     * 
     * @param  block the block to be evaluated.
     * @return the number of elements in the given block.
     */
    public int getCount( B block )
    {
    	
    	final AtomicInteger count = partitionCount.get( block );
    	return count != null ? count.get() : 0;
    	
    }
    
    /**
     * Adds an element in the count related to the given block.
     * 
     * @param  block the block to be updated.
     * @return new number of elements in the block.
     */
    public synchronized int addElement( B block )
    {
    	
    	return unsafeAddElement( block );
    	
    }

    /**
     * Removes an element from the count related to the given block.
     * 
     * @param  block the block to be updated.
     * @return new number of elements in the block.
     * @throws NoSuchElementException if the block is already empty.
     */
    public synchronized int remElement( B block )
    {

    	return unsafeRemElement( block );
    	    	
    }
    
    /**
     * Initializes the given block with the given value.
     * 
     * @param block the block to initialize.
     * @param count initial count.
     */
    public synchronized void init( B block, int count )
    {
    	
    	if( count < 0 )
    		throw new IllegalArgumentException( "The initial count can't be negative." );
    	
    	if( partitionCount.containsKey(block) )
    		throw new IllegalArgumentException( "The block " + block + " was already initialized." );
    	
		partitionCount.put( block, new AtomicInteger(count) );
		totalCount.addAndGet( count );
    	
    }
    
    /**
     * Returns the set of blocks representing the partition.
     * 
     * @return the set of blocks representing the partition.
     */
    public Set<B> getPartition()
    {
    	
    	return partitionCount.keySet();
    	
    }
    
    
    /* ***************** */
    /*  PRIVATE METHODS  */
    /* ***************** */
    
    
    /**
     * Adds a new element to the count related to the given block.
     * <p>
     * This method is not {@code thread-safe}.
     * 
     * @param  block the block to update.
     * @return new number of elements.
     */
    protected int unsafeAddElement( B block )
    {
    	
    	/*
    	 * The blocks are supposed to be unbounded in size,
    	 * so adding elements is a safe operation.
    	 * The only annoying case is when the block does
    	 * not exist and must be created.
    	 */    	
    	AtomicInteger count = partitionCount.get( block );
    	if( count == null )
    	{
    		
    		count = new AtomicInteger( 0 );
    		partitionCount.put( block, count );
    		
    	}
    	    	
    	totalCount.incrementAndGet();
    	return count.incrementAndGet();
    	
    }

    /**
     * Removes an element from the count related to the given block.
     * <p>
     * This method is not {@code thread-safe}.
     * 
     * @param  block the block to update.
     * @return new number of elements.
     * @throws NoSuchElementException if the block is already empty.
     */
    protected int unsafeRemElement( B block )
    {

    	/* If the block is empty this call is inconsistent. */
    	final AtomicInteger blockCount = partitionCount.get( block );
    	if( blockCount == null )
    		throw new NoSuchElementException( "The block " + block + " is empty, no more elements can be removed");
    		
    	/* First we reduce the number of elements in the block. */
    	final int currentCount = blockCount.decrementAndGet();
    	
    	/* If the block is empty we remove it from the partition. */
    	if( currentCount < 1 ) partitionCount.remove( block );
    		
    	/*
    	 * We reduce also the total count. In the case of a failure
    	 * in one of the above operations, the total count will not
    	 * be modified.
    	 */
    	totalCount.decrementAndGet();
    	return currentCount;
    	    	
    }

}
