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
package org.nerd4j.time;

import java.util.Date;

import org.nerd4j.lang.ComparableInterval;

/**
 * Represent a time interval into <i>Unix Time</i> range definition.
 * 
 * <p>
 * Unix dates are defined between <tt>01/01/1970 00:00:00 UTC</tt> and
 * <tt>19/01/2038 03:14:07 UTC</tt>.
 * </p>
 * 
 * <p>
 * Every period must have starting and ending dates; if missing (as in case of
 * non ending periods) the following can be used:
 * <ul>
 * <li>no start: {@link #BEGIN_OF_UNIXTIME}</li>
 * <li>no end: {@link #END_OF_UNIXTIME}</li>
 * </ul>
 * </p>
 * 
 * @author Nerd4j Team
 */
public class UnixTimeInterval extends ComparableInterval<Date>
{

	/** Default Serial Version UID. */
	private static final long serialVersionUID = 1L;
	
	/** <i>Unix Time</i> (32bit) begin date: <tt>01/01/1970 00:00:00 UTC</tt>. */
	public static final Date BEGIN_OF_UNIXTIME = new Date( 0L );
	
	/** <i>Unix Time</i> (32bit) end date: <tt>19/01/2038 03:14:07 UTC</tt>. */
	public static final Date END_OF_UNIXTIME = new Date( Integer.MAX_VALUE * 1000L );
	
	/** Complete standard <i>Unix Time</i> interval. */
	private static final UnixTimeInterval UNIX_TIME_INTERVAL = new UnixTimeInterval();
	
	
	/**
	 * Creates a new {@link UnixTimeInterval} without begin and end dates.
	 * <p>
	 * Begin and end dates will be set to {@link #BEGIN_OF_UNIXTIME} and
	 * {@link #END_OF_UNIXTIME} accordingly.
	 * </p>
	 */
    protected UnixTimeInterval()
    {
    	
    	super( BEGIN_OF_UNIXTIME, END_OF_UNIXTIME );
    	
    }
    
	/**
	 * Creates a new {@link UnixTimeInterval} virtually without end.
	 * 
	 * <p>
	 * End date will be set to {@link #END_OF_UNIXTIME}.
	 * </p>
	 * 
	 * @param begin interval begin date.
	 * 
	 * @throws NullPointerException
	 *             if <tt>begin</tt> parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <tt>begin</tt> is before {@link #BEGIN_OF_UNIXTIME}
	 * @throws IllegalArgumentException
	 *             if <tt>begin</tt> isn't before {@link #END_OF_UNIXTIME}
	 */
    public UnixTimeInterval( Date begin )
    {
    	
    	this( begin, END_OF_UNIXTIME );
    	
    }
    
	/**
	 * Creates a completely defined new {@link UnixTimeInterval}.
	 * 
	 * @param begin interval begin date.
	 * @param end   interval end date.
	 * 
	 * @throws NullPointerException
	 *             if al leas one parameter is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <tt>begin</tt> isn't before <tt>end</tt>.
	 * @throws IllegalArgumentException
	 *             if <tt>begin</tt> is before {@link #BEGIN_OF_UNIXTIME}
	 * @throws IllegalArgumentException
	 *             if <tt>begin</tt> is after {@link #END_OF_UNIXTIME}
	 */
    public UnixTimeInterval( Date begin, Date end )
    {
    	
    	super( begin, end );
    	
    	if ( begin.before( BEGIN_OF_UNIXTIME ) )
    		throw new IllegalArgumentException( "The begin date is not in the unix time range" );
    	
    	if ( end.after( END_OF_UNIXTIME ) )
    		throw new IllegalArgumentException( "The end date is not in the unix time range" );
    	
    }
    
    /**
     * Returns the complete standard <i>Unix Time</i> interval.
     * 
     * @return <i>Unix Time</i> definition interval
     */
	public static UnixTimeInterval getUnixTimeInterval()
	{
		
		return UNIX_TIME_INTERVAL;
		
	}
	
}
