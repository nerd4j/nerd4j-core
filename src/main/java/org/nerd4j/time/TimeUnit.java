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

import java.util.Calendar;

/**
 * Enumerates known metrical system time units.
 * 
 * @author Nerd4j Team
 */
public enum TimeUnit
{
	
	/** AD or BC in the Julian calendar. */
	ERA( Calendar.ERA ),
	
	/** Calendar specific year number. */
    YEAR( Calendar.YEAR ),
    
    /** Month number in year. */
    MONTH( Calendar.MONTH ),
    
    /** Week number in year. */
    WEEK( Calendar.WEEK_OF_YEAR ),
    
    /** Day number in year. */
    DAY( Calendar.DAY_OF_YEAR ),
    
    /** Hour number in day. */
    HOUR( Calendar.HOUR_OF_DAY ),
    
    /** Minute number in hour. */
    MINUTE( Calendar.MINUTE ),
    
    /** Second number in minute. */
    SECOND( Calendar.SECOND ),
    
    /** Milli-second number in second. */
    MILLISECOND( Calendar.MILLISECOND );
    
    /** Codice corrispondente al dato concetto nella classe {@code java.util.Calendar}. */
    private final int calendarCode;
    
    
	/**
	 * Creates a {@link TimeUnit} binding it with it's {@link Calendar} mathing
	 * constant.
	 * 
	 * @param calendarCode matching code of {@link Calendar}.
	 */
    private TimeUnit( int calendarCode )
    {
    	
    	this.calendarCode = calendarCode;
    	
    }
    
    /**
     * Returns corresponding numeric code known by {@link Calendar}.
     * 
     * @return {@link Calendar} matching code.
     */
    public int getCalendarCode()
    {
    	
    	return calendarCode;
    	
    }

}
