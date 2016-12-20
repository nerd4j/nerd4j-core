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
package org.nerd4j.net;

import java.io.Serializable;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class handles the representation of objects of type IP and can be
 * used in all those cases where is the need to handle IPs without the need
 * of all the features provided by the class {@link InetAddress}.
 * <p>
 * It's possible ho access all the formats through the methods:
 * {@link #intValue()}, {@link #stringValue()} and {@link #rawValue()}.
 * The IP <i>raw</i> format is the same returned by the method  {@link InetAddress#getAddress()}
 * in order to simplify the passage from one class to the other.
 * <p>
 * This class provides immutable instances that can be created using the
 * available factory methods: {@link #valueOf(int)}, {@link #valueOf(String)},
 * {@link #valueOf(byte[])}.
 * <p>
 * The implementation of the {@link Comparable} interface can be misleading
 * because internally the IP value is represented using an {@code int}.
 * The IP {@code 255.255.255.255} is expected to be greater than {@code 0.0.0.0}
 * but the first IP is the {@code int} value {@code -1} while the second
 * is the {@code int} value {@code 0}. Another example is given by the IP
 * {@code 127.255.255.255} and the IP {@code 128.0.0.0} the first is the
 * {@code int} value {@code 2147483647} while the second is the {@code int}
 * value {@code -2147483648}.
 * <p>
 * In other words the method {@link #compareTo(IPv4Address)} compares the
 * IP address as they were two unsigned {@code int} values.
 * 
 * @author Nerd4j Team
 */
public final class IPv4Address implements Serializable, Comparable<IPv4Address>
{
	
	/** Serial Version UID. */
	private static final long serialVersionUID = -9003117636518262197L;
	
	
	/* *************** */
	/*   STATIC DATA   */
	/* *************** */
	

	/** Number of bytes of the IP address. */
	private static final int BYTE_NUMBER = 4;
	
	/** RegExp to recognize the '.' */
	private static final String DOT   = "\\.";
	
	/** RegExp with groupo definition to recongize a byte as a decimal value. */
	private static final String DEC_BYTE = "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})";
	
	/** RegExp with groupo definition to recongize a byte as a hex-decimal value. */
	private static final String HEX_BYTE = "([A-Fa-f0-9]{1,2})";
	
	/** Complete RegExp to recongize an IP in decimal notation. */
	private static final Pattern DEC_IP_PATTERN = Pattern.compile( DEC_BYTE + DOT +
			                                                       DEC_BYTE + DOT +
			                                                       DEC_BYTE + DOT +
			                                                       DEC_BYTE );
	
	/** Complete RegExp to recongize an IP in hex-decimal notation. */
	private static final Pattern HEX_IP_PATTERN = Pattern.compile( HEX_BYTE + DOT +
			                                                       HEX_BYTE + DOT +
			                                                       HEX_BYTE + DOT +
			                                                       HEX_BYTE );
	
	/** Bit mask to isolate the less significant byte from an integer. */
	private static final int LOW_BYTE_MASK = 0xFF;
	
	
	/* *************** */
	/*  INSTANCE DATA  */
	/* *************** */
	
	/** The {@code int} representation if the IP. */
	private int intIp;
	
	/** String representation of the IP. Is transient and populated when needed. */
	private transient String strIp;
	
	/** Byte array representation of the IP. Is transient and populated when needed. */
	private transient byte[] rawIp;
	
	
	/**
	 * Private constructor.
	 * <p>
	 * It's not possible to access the constructor, use the factory
	 * methods instead.
	 * 
	 * @param ip the IP represented as an integer.
	 */
	private IPv4Address( int ip )
	{
		
		super();
		
		this.intIp = ip;
		
	}
	
	
	/* ***************** */
	/*  FACTORY METHODS  */
	/* ***************** */
	
	
	/**
	 * Returns an {@link IPv4Address} using the given integer representation.
	 * 
	 * @param ip the IP represented as an integer.
	 * @return IP address.
	 */
	public static IPv4Address valueOf( int ip )
	{
		
		return new IPv4Address(ip);
		
	}
	
	/**
	 * Returns an {@link IPv4Address} using the given {@link String} representation.
	 * <p> 
	 * The textual representation can be in decimal (ie. {@code 255.255.255.255})
	 * or hex-decimal ({@code FF.FF.FF.FF}) notation.
	 * 
	 * @param ip the IP represented as a {@link String}.
	 * @return IP address.
	 * 
	 * @throws IllegalArgumentException if the {@link String} does not represent a valid IP .
	 */
	public static IPv4Address valueOf( String ip ) throws IllegalArgumentException
	{
		
		int intIp;
		IPv4Address ipAddress;
		
		try
		{
			intIp = computeIntValue(ip, DEC_IP_PATTERN, 10);
			ipAddress = new IPv4Address(intIp);
			
			/* Stores the textual representation for further use. */
			ipAddress.strIp = ip;
			
		} catch ( IllegalArgumentException e )
		{
			intIp = computeIntValue(ip, HEX_IP_PATTERN, 16);
			ipAddress = new IPv4Address(intIp);
			
			/*
			 * In this case the textual representation will not be
			 * stored because this class only produces decimal representations.
			 */
		}
		
		return ipAddress;
	}
	
	/**
	 * Returns an {@link IPv4Address} using the given {@code raw} representation.
	 * <p> 
	 * The raw format is the same provided by {@link InetAddress#getAddress()}.
	 * 
	 * @param ip the IP represented as a byre array.
	 * @return IP address.
	 * 
	 * @throws IllegalArgumentException if the byte array does not represent a valid IP .
	 */
	public static IPv4Address valueOf( byte[] ip ) throws IllegalArgumentException
	{
		
		final int intIp = computeIntValue(ip);
		
		final IPv4Address ipAddress = new IPv4Address(intIp);
		
		/* Stores the raw representation for further use. */
		ipAddress.rawIp = new byte[BYTE_NUMBER];
		System.arraycopy(ip, 0, ipAddress.rawIp, 0, ip.length);
		
		return ipAddress;
	}
	
	/**
	 * Returns an {@link IPv4Address} using the given {@link InetAddress} representation.
	 * <p>
	 * This method supports conversions from {@link Inet6Address} if
	 * {@code ((Inet6Address)inet).isIPv4CompatibleAddress() == true} holds.
	 * 
	 * @param inet the IP represented as a {@link IPv4Address}.
	 * @return IP address.
	 * 
	 * @throws IllegalArgumentException if the {@link Inet6Address} is not convertible.
	 */
	public static IPv4Address valueOf( InetAddress inet ) throws IllegalArgumentException
	{
		
		if ( inet == null )
			throw new IllegalArgumentException( "Null isn't a valid inet address." );
		
		byte[] raw;
		
		if ( inet instanceof Inet6Address )
		{
			final Inet6Address inet6 = (Inet6Address) inet;
			
			if(!inet6.isIPv4CompatibleAddress())
				throw new IllegalArgumentException( "Not a compatible IPv4 IPv6 address: " + inet );
			
			raw = new byte[4];
			System.arraycopy(inet6.getAddress(), 12, raw, 0, 4);
			
		} else
		{
			raw = inet.getAddress();
		}
		
		return valueOf( raw );
		
	}
	
	
	/* ******************** */
	/*  CONVERSION METHODS  */
	/* ******************** */
	
	
	/**
	 * Converts the given textual representation into the internal {@code int}
	 * representation using the given numerical base.
	 * 
	 * @param ip      textual representation of the IP.
	 * @param pattern evaluation pattern. The pattern is expected to produce 4 blocks.
	 * @param base    the numerical base to use in conversion.
	 * 
	 * @return internal {@code int} representation of the IP.
	 * 
	 * @throws IllegalArgumentException if the provided text can't be parsed.
	 */
	private static int computeIntValue( String ip, Pattern pattern, int base ) throws IllegalArgumentException
	{
		
		final Matcher matcher = pattern.matcher(ip);
		
		if( matcher.matches() )
		{
			
			int intIp = 0;
			
			String strBlock;
			int intBlock;
			
			/* If the pattern mathes the blocks are ensured to be four. */
			for( int i = 1; i < BYTE_NUMBER + 1; ++i )
			{
				
				strBlock = matcher.group(i);
				intBlock = Integer.valueOf( strBlock, base );
				intIp = intIp << 8 | intBlock;
				
			}
			
			return intIp;
			
		} else
		{
			throw new IllegalArgumentException("Not a valid IP string: \"" + ip + "\".");
		}
		
	}
	
	/**
	 * Converts the given byte array representation into the internal {@code int}
	 * representation using the given numerical base.
	 * 
	 * @param ip      raw representation of the IP.
	 * @return internal {@code int} representation of the IP.
	 * 
	 * @throws IllegalArgumentException if the provided byte array does not represent a valid IP.
	 */
	private static int computeIntValue( byte[] ip )
	{
		
		if( ip == null || ip.length != BYTE_NUMBER )
			throw new IllegalArgumentException("Not a valid raw IP.");
		
		int intIp = 0;
		
		for( byte byteBlock : ip )
		{
			
			/*
			 * The JVM converts the byte into an integer before putting it
			 * in OR with the ip value. Therefore we need to use the LOW_BYTE_MASK
			 * to avoid two's complement to interfere with the operation.
			 * Indeed values of type 254 (byte value -2) are converted from
			 * byte representation 0xFE to integer representation 0xFFFFFFFE.
			 */
			intIp = intIp << 8 | (byteBlock & LOW_BYTE_MASK) ;
		}
		
		return intIp;
		
	}
	
	/**
	 * Converts the given internal representation into the decimal textual representation.
	 * 
	 * @param ip      internal representation of the IP.
	 * @return decimal textual representation of the IP.
	 */
	private String computeStringValue( int ip )
	{
		StringBuilder sb = new StringBuilder(16);
		
		for( int i = 0; i < BYTE_NUMBER; ++i )
		{
			/* Inizia ad appendere dal blocco di byte più alto. */
			int intBlock = ( ip >> 8 * (BYTE_NUMBER -1 -i) ) & LOW_BYTE_MASK;
			sb.append(intBlock).append('.');
			
		}
		
		final int end = sb.length() - 1;
		
		return sb.substring(0, end);
		
	}
	
	/**
	 * Converts the given internal representation into the raw format representation.
	 * 
	 * @param ip      internal representation of the IP.
	 * @return raw format representation of the IP.
	 */
	private byte[] computeRawValue( int ip )
	{

		byte[] rawIp = new byte[BYTE_NUMBER];

		for( int i = 0; i < BYTE_NUMBER; ++i )
		{
			
			/* Starts reading the most significant byte. */
			rawIp[i] = (byte) ( ip >> 8 * (BYTE_NUMBER -1 -i) );
			
		}
		
		return rawIp;
		
	}

	
	/* ************** */
	/*  DATA METHODS  */
	/* ************** */
	
	
	/**
	 * Returns the internal representation of the IP.
	 * 
	 * @return internal representation of the IP.
	 */
	public int intValue()
	{
		return intIp;
	}
	
	/**
	 * Returns the textual representation in decimal format (ie. {@code 255.255.255.255}).
	 * 
	 * @return textual representation if the IP.
	 */
	public String stringValue()
	{
		if (strIp == null)
			strIp = computeStringValue( intIp );
		
		return strIp;
	}
	
	/**
	 * Returns the raw representation as provided by {@link InetAddress#getAddress()}.
	 * 
	 * @return waw representation if the IP.
	 */
	public byte[] rawValue()
	{
		if (rawIp == null)
			rawIp = computeRawValue( intIp );
		
		return rawIp;
	}
	
	
	/* ***************************** */
	/*  OBJECT & COMPARABLE METHODS  */
	/* ***************************** */
	
	
	/**
	 * Compares this instance whit the given one.
	 * <p>
	 * The ordering is the same obtained by ordering two unsigned integers. 
	 * 
	 * @param other the instance to compare.
	 * 
	 * @return a negative value if {@code this < other}, zero if {@code this == other}
	 *         and a positive value if {@code this > other}.
	 */
	@Override
	public int compareTo( IPv4Address other )
	{
		
		/* The comparison is made depending on the sign of the values. */
		
		if ( (intIp ^ other.intIp) >>> 31 == 1 )
			/* Different sighs. */
			return other.intIp - intIp;
		
		else
			/* Same sign. */
			return intIp - other.intIp;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode()
	{
		return intIp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj)
	{
		
		if (this == obj) return true;
		
		if (obj == null) return false;
		
		if (getClass() != obj.getClass()) return false;
		
		IPv4Address other = (IPv4Address) obj;
		
		if (intIp != other.intIp) return false;
		
		return true;
		
	}

    /**
     * Returns the textual representation of this IP address.
     * 
     * @see #stringValue()
     */
	@Override
	public String toString()
	{
		
		return stringValue();
		
	}
	
}
