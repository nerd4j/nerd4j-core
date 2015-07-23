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
package org.nerd4j.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A {@link KeyLock} is a multiple lock over keys useful in those
 * cases where it's needed to lock several resources.
 * 
 * <p>
 *  A use case can be the following:
 * 
 * <pre>
 * public abstract class ResourceHandler
 * {
 * 
 * 	private KeyLock&lt;MyResource&gt; klock = new KeyLock&lt;MyResource&gt;();
 * 
 * 	public void doWork()
 * 	{
 * 
 * 		MyResource shared = getSharedResource();
 * 
 * 		klock.lock( shared );
 * 
 * 		try
 * 		{
 * 
 * 			...do some work on shared resource...
 * 
 * 		} finally
 * 		{
 * 
 * 			klock.unlock( shared );
 * MyLock
 * 		}
 * 
 * 	}
 * 
 * 	public abstract MyResource getSharedResource();
 * 
 * }
 * </pre>
 * 
 * </p>
 * 
 * @author Nerd4j Team
 * 
 * @param <K> class representing the keys used to identify locks.
 */
/*
 * FIXME there is a problem of fairness during the removal of a lock from
 * the internal map; it should be better do not remove the locks but in
 * this case the map will grow indefinitely. Find a way to clean the map
 * periodically.
 */
public class KeyLock<K>
{
	
	/** Currently locks. */
	private ConcurrentMap<K,ReentrantLock> locks;
	
	/** Fairness of the generated locks. */
	private final boolean fair;
	
    /**
     * Default constructor.
     * 
     * This is equivalent to using {@code KeyLock(false)}.
     */
	public KeyLock()
	{
		
		this( false );
		
	}
	
    /**
     * Creates an instance of {@code KeyLock} with the
     * given fairness policy.
     *
     * @param fair {@code true} if this lock should use a fair ordering policy
     */
	public KeyLock( boolean fair )
	{
		
		this.fair = fair;
		
		locks = new ConcurrentHashMap<K, ReentrantLock>();
		
	}
	
	/**
	 * Acquires the lock for the given key.
	 * 
	 * @param key key for the related lock.
	 */
	public void lock( K key )
	{
	
		/*
		 * We check if the lock is held by the current thread.
		 * In this case no further operations are needed.
		 */
		ReentrantLock zlock = locks.get(key);
		if ( zlock != null && zlock.isHeldByCurrentThread() )
		{
			zlock.lock();
			return;
		}
		
		/* Creates and acquires a new lock. */
		ReentrantLock mlock = new ReentrantLock( fair );
		mlock.lock();
		
		/*
		 * Adds the new lock in the map only if there are no locks for
		 * the given key. The method returns the lock in the map if
		 * exists or null otherwise.
		 */
		ReentrantLock olock = locks.putIfAbsent( key, mlock );
		
		/* Loops until it's not possible to add the lock. */
		while ( olock != null )
		{
			
			/* Keeps the old lock to be able to release it later. */
			ReentrantLock plock = olock;
			
			/*
			 * Acquires a lock over the existing one.
			 * 
			 * In this point we use the fairness to ensure that the locks
			 * are acquired following the order in which they are requested.
			 */
			plock.lock();
			
			/*
			 * Tries again to add the new lock.
			 *
			 * Having acquired the previous lock two cases are possible:
			 * 
			 * A) successfully adds the new lock (olock becomes null);
			 * 
			 * B) another thread has acquired plock and replaced plock
			 * with it's own (olock becomes not null). In this case we
			 * need to loop again.
			 */
			olock = locks.putIfAbsent( key, mlock );
			
			/* Unlocks the existing lock. */
			plock.unlock();
			
		}
		
	}
	
	/**
	 * Attempts to release the lock at given key.
	 * 
	 * <p>
	 * If the current thread is the key lock holder then the hold count is
	 * decremented. If the hold count is now zero then the lock is released. If
	 * the current thread is not the key lock holder then
	 * {@link IllegalMonitorStateException} is thrown.
	 * 
	 * @throws IllegalMonitorStateException if the current thread does not hold given key lock
	 */
	public void unlock( K key ) 
	{
		
		ReentrantLock clock = locks.get( key );
		
		/* If there are no locks for the given key an exception will be thrown. */
		if ( clock == null )
		{
			throw new IllegalMonitorStateException( "Actually doesn't exists any lock for key " + key );
		}
		
		/*
		 * An IllegalMonitorStateException will be thrown if the current thread
		 * is not the one keeping the current lock.
		 */
		clock.unlock();
		
		/* 
		 * If we reach this point the current thread is the one keeping the lock.
		 * If the hold count is 0 than the lock must be removed from the map.
		 */
		/*
		 * FIXME this operation is in conflict with the fairness.
		 */
		if ( clock.getHoldCount() == 0 ) locks.remove( key ); 
		
	}
	
}
