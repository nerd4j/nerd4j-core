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

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class to perform simple reflection operations.
 * 
 * @author Nerd4j Team
 */
public class ReflectionUtil
{

	/** Class static logger. */
	private static Logger logger = LoggerFactory.getLogger( ReflectionUtil.class );

    /**
	 * Search, in the provided class, for the desired field
	 * with the given name and type.
	 * <p>
	 *  If the {@link Field} is not in the provided {@link Class}
	 *  it will be searched in the super-class. The process proceeds
	 *  until the class {@link Object} is reached.
	 * </p>
	 * <p>
	 *  If the searched field occurs in more than one class in the class
	 *  hierarchy, the first occurrence found will be returned.
	 * </p>
	 * 
	 * @param fieldType type of the searched field.
	 * @param fieldName name of the searched field.
	 * @param clazz     class into which to search.
	 * 
	 * @return the {@link Field} id found, {@link NullPointerException} otherwise.
	 * 
	 * @throws SecurityException see {@link Class#getDeclaredClasses()}
	 * 
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 */
	public static Field findField( Class<?> fieldType, String fieldName, Class<?> clazz )
	throws SecurityException, NullPointerException
    {
    	
    	if ( clazz == null || fieldName == null || fieldType == null )
    	{
    		throw new NullPointerException( "Neither class or fieldName or fieldType can be null." );
    	}
    	
    	Class<?> currClazz = clazz;
    	
    	/*
		 * "currClazz" will be null when the top of the class hierarchy
		 * will be reached.
		 */
    	while( currClazz != null )
    	{
    		
    		try {
    			
    			if ( logger.isDebugEnabled() )
    				logger.debug( "Looking for field " + currClazz.getCanonicalName() + "." + fieldName + "." );
    			
    			/*
    			 * We search for the desired Field, if not found
    			 * a NoSuchFieldException will be thrown.
    			 */
    			Field field = currClazz.getDeclaredField( fieldName );
    			
    			Class<?> currFieldType = field.getType();
    			
    			/*
    			 * We check that the retrieved field type matches
    			 * the desired field type. If it is the case than
    			 * the we found the right field.
    			 */
    			if ( currFieldType.isAssignableFrom( fieldType ) )
    			{
    				if ( logger.isDebugEnabled() )
    					logger.debug( "Field " + fieldName + " found in " + currClazz.getSimpleName() + "." );
        			
    				return field;
    			}
    			
    			/* 
    			 * If this point is reached it means that a field with the right name
    			 * but the wrong type has been found, so we log it for debug purpose.
    			 */
    			if ( logger.isDebugEnabled() )
    				logger.debug( "Field " + fieldName + " found in " + currClazz.getSimpleName()
    						+ " but with an incompatible type " + currFieldType.getCanonicalName()
    						+ ", requested " + fieldType.getCanonicalName() + "." );
				
			} catch ( NoSuchFieldException e )
			{
			
				/*
				 * We avoid to manage the exception at this point.
				 * The management is in fact identical to the case:
				 * field found but not compatible, this management
				 * is performed proceeding to the next class.
				 */
				
			}
			
    		/*
    		 * We log for debug purpose that the field was not found
    		 * in the current class.
    		 */
			if ( logger.isDebugEnabled() )
				logger.debug( "The field " + currClazz.getCanonicalName() + "." + fieldName
						+ " doesn't exists; looking for it in superclass." );
			
			/* We proceed to inspect the super-class. */
			currClazz = currClazz.getSuperclass();
    		
    	}
    	
    	if ( logger.isErrorEnabled() )
    		logger.error( "Field " + fieldName + " cannot be found in class "
    				+ clazz.getCanonicalName() + " or any superclass." );
    	
    	return null;
    	
    }

	
	/**
	 * Search, in the provided class, for all the fields annotated
	 * with the provided {@link Annotation}.
	 * 
	 * @param <A> the {@link Annotation} type.
	 *  
	 * @param annotationClass type of the annotation to search.
	 * @param clazz     class into which to search.
	 * 
	 * @return all the found fields and their respective annotations.
	 * 
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 */
	public static <A extends Annotation> Map<Field,A> findAnnotatedFields( Class<A> annotationClass, Class<?> clazz )
	throws NullPointerException
	{

		if ( annotationClass == null || clazz == null )
		{
			throw new NullPointerException( "Annotation and class cannot be null." );
		}

		if ( logger.isTraceEnabled() )
			logger.trace( "Looking for anntation " + annotationClass.getCanonicalName()
				+ " in class " + clazz.getCanonicalName() + "." );

		
		Map<Field,A> result = new HashMap<Field,A>();
		Class<?> currClazz = clazz;

		/*
		 * "currClazz" will be null when the top of the class hierarchy
		 * will be reached.
		 */
		while( currClazz != null )
		{

			/* Retrieves all the fields in the current class. */
			Field[] fields = currClazz.getDeclaredFields();

			/*
			 * For each field checks if it has the desired
			 * annotation, in this case the field and the
			 * related annotation will be added to the result.
			 */
			for( Field field : fields )
			{

				A annotation = field.getAnnotation( annotationClass );
				if ( annotation != null )
				{
					if ( logger.isTraceEnabled() )
						logger.trace( "Found anntation " + annotationClass.getCanonicalName()
							+ " on field " + field + "." );

					result.put( field, annotation );
				}

			}

			/* We proceed to inspect the super-class. */
			currClazz = currClazz.getSuperclass();

		}

		if ( logger.isDebugEnabled() )
			logger.debug( "Found " + result.size() + " " + annotationClass.getCanonicalName()
				+ " annoted field in class " + clazz.getCanonicalName() + " and its superclasses." );

		return result;

	}
	
	/**
	 * Search, in the provided class, for all the methods annotated
	 * with the provided {@link Annotation}.
	 * 
	 * @param <A> the {@link Annotation} type.
	 *  
	 * @param annotationClass type of the annotation to search.
	 * @param clazz     class into which to search.
	 * 
	 * @return all the found methods and their respective annotations.
	 * 
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 */
	public static <A extends Annotation> Map<Method,A> findAnnotatedMethods( Class<A> annotationClass, Class<?> clazz )
	throws NullPointerException
	{

		if ( annotationClass == null || clazz == null )
		{
			throw new NullPointerException( "Annotation and class cannot be null." );
		}

		if ( logger.isTraceEnabled() )
			logger.trace( "Looking for anntation " + annotationClass.getCanonicalName()
				+ " in class " + clazz.getCanonicalName() + "." );

		
		Map<Method,A> result = new HashMap<Method,A>();
		Class<?> currClazz = clazz;

		/*
		 * "currClazz" will be null when the top of the class hierarchy
		 * will be reached.
		 */
		while( currClazz != null )
		{

			/* Retrieves all the methods in the current class. */
			Method[] methods = currClazz.getDeclaredMethods();
			

			/*
			 * For each method checks if it has the desired
			 * annotation, in this case the field and the
			 * related annotation will be added to the result.
			 */
			for( Method method : methods )
			{
				
				/* 
				 * We need to check if the method is flagged as "synthetic"
		         * or "bridge" because also those methods are returned by
		         * class.getDeclaredMethods() but we don't want to return them.
		         */
				if( method.isSynthetic() || method.isBridge() )
					continue;
				
				A annotation = method.getAnnotation( annotationClass );
				if ( annotation != null )
				{
					if ( logger.isTraceEnabled() )
						logger.trace( "Found anntation " + annotationClass.getCanonicalName()
							+ " on method " + method + "." );

					result.put( method, annotation );
				}

			}

			/* We proceed to inspect the super-class. */
			currClazz = currClazz.getSuperclass();

		}

		if ( logger.isDebugEnabled() )
			logger.debug( "Found " + result.size() + " " + annotationClass.getCanonicalName()
				+ " annoted method in class " + clazz.getCanonicalName() + " and its superclasses." );

		return result;

	}
	
	
	
	/**
	 * Search, in the provided class, for the <tt>public</tt>
	 * parameterless {@link Method} with the given name.
	 *
	 * @param methodName name of the {@link Method} to find.
	 * @param clazz      class into which to search.
	 * 
	 * @return the public {@link Method} if found, <tt>null</tt> otherwise.
	 * 
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 */
	public static Method findPublicMethod( String methodName, Class<?> clazz )
	throws NullPointerException
	{
		
		if ( methodName == null || methodName.isEmpty() || clazz == null )
		{
			if ( logger.isErrorEnabled() )
				logger.error( "Method name and class cannot be null." );

			throw new NullPointerException( "Method name and class cannot be null." );
		}
		
		if ( logger.isTraceEnabled() )
			logger.trace( "Looking for public method " + methodName + " in class " + clazz.getCanonicalName() + "." );
		
		try {
			
			/* 
			 * We search for the public parameterless method, the called
			 * method already performs the search in all the hierarchy.
			 * If the method is not found a NoSuchMethodException will
			 * be thrown
			 */
			Method method = clazz.getMethod( methodName, (Class<?>[]) null );
			
			/* 
			 * The method class.getDeclaredMethods() returns also
			 * methods flagged as "synthetic" and "bridge", probably
			 * it's not the case for class.getDeclaredMethod(String,Class<?>...)
			 * but we perform the check for safety.
	         */
			if( method.isSynthetic() || method.isBridge() )
				throw new NoSuchMethodException();
			
			/* We log for debug purpose that the method has been found. */
			if ( logger.isDebugEnabled() )
				logger.debug( "Public method " + methodName + " found in class " + clazz.getCanonicalName() + "." );
			
			return method;
			
		}catch( NoSuchMethodException ex )
		{
			
			/*
			 * We log that the method cannot be found but
			 * we don't handle the NoSuchMethodException.
			 */
			if ( logger.isWarnEnabled() )
				logger.warn( "Public method " + methodName + " cannot be found!" );
			
			return null;
			
		}
		
	}


	/**
	 * Search, in the provided class, for the <tt>public</tt>,
	 * <tt>protected</tt> or <tt>private</tt> parameterless
	 * {@link Method} with the given name.
	 *
	 * @param name  name of the {@link Method} to find.
	 * @param clazz class into which to search.
	 * 
	 * @return the public {@link Method} if found, <tt>null</tt> otherwise.
	 * 
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 * @throws SecurityException see {@link Class#getDeclaredMethod(String, Class...)}.
	 */
	public static Method findMethod( String name, Class<?> clazz )
	throws SecurityException, NullPointerException
	{

		return findMethod( name, (Class<?>[]) null, clazz );

	}
	

	/**
	 * Search, in the provided class, for the <tt>public</tt>,
	 * <tt>protected</tt> or <tt>private</tt> {@link Method}
	 * with the given name and parameters.
	 *
	 * @param name           name of the {@link Method} to find.
	 * @param parameterTypes the type of each parameter in the method signature.
	 * @param clazz          class into which to search.
	 * 
	 * @return the public {@link Method} if found, <tt>null</tt> otherwise.
	 * 
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 * @throws SecurityException see {@link Class#getDeclaredMethod(String, Class...)}.
	 */
	public static Method findMethod( String name, Class<?>[] parameterTypes, Class<?> clazz )
	throws SecurityException, NullPointerException
	{
		
		if ( name == null || name.isEmpty() || clazz == null )
		{
			if ( logger.isErrorEnabled() )
				logger.error( "Method name and class cannot be null." );

			throw new NullPointerException( "Method name and class cannot be null." );
		}
		
		if ( logger.isTraceEnabled() )
			logger.trace( "Looking for method {} with parameter types {} in class {} hierarchy.",
					new Object[] { name, parameterTypes, clazz.getSimpleName() } );
		
		Method method = null;
		
		/*
		 * "currClazz" will be null when the top of the class hierarchy
		 * will be reached.
		 */
		Class<?> currClazz = clazz;
		
		do {
			
			try{
				
				method = currClazz.getDeclaredMethod( name, parameterTypes );
				
				/* 
				 * The method class.getDeclaredMethods() returns also
				 * methods flagged as "synthetic" and "bridge", probably
				 * it's not the case for class.getDeclaredMethod(String,Class<?>...)
				 * but we perform the check for safety.
		         */
				if( method.isSynthetic() || method.isBridge() )
					continue;
				
				/* 
    			 * If this point is reached it means that a matching method
    			 * has been found, elsewhere a NoSuchMethodException has been
    			 * thrown.
    			 */
				logger.debug( "Found matching method {} in class {} hierarchy.", method, clazz );
				
				return method;
			
			} catch( NoSuchMethodException ex )
			{
				/*
				 * We avoid to manage the exception at this point.
				 * The management is in fact identical to the case:
				 * method found but not compatible, this management
				 * is performed proceeding to the next class.
				 */
			}
			
		} while( (currClazz = currClazz.getSuperclass()) != null );
		
		if ( logger.isWarnEnabled() )
			logger.warn( "Hierarchy root reached; method {} with parameter types {} cannot be found in class {} hierarchy!",
					new Object[] { name, parameterTypes, clazz.getSimpleName() } );
		
	    return null;
		
	}
	

	/**
	 * Search, in the provided class, for an access method (so called <code>getter</code>)
	 * related to given property name.
	 * <p>
	 *  This method works properly if the provided {@link Class}
	 *  is a proper class and not an interface.
	 *  For interfaces if the method is not found on the current interface
     *  the research will NOT be extended to super interfaces.
	 *  Use {@link #findPublicGetter(String, Class)} instead.
	 * </p>
	 *
	 * @param property name of the property to find.
	 * @param clazz    class into which to search.
	 * 
	 * @return the public {@link Method} if found, <tt>null</tt> otherwise.
	 * 
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 * @throws SecurityException see {@link Class#getDeclaredMethod(String, Class...)}.
	 */
	public static Method findGetter( String property, Class<?> clazz )
	throws SecurityException, NullPointerException
	{
	
		if ( property == null || property.isEmpty() || clazz == null )
		{
			logger.error( "Property name and class cannot be null." );

			throw new NullPointerException( "Property name and class cannot be null." );
			
		}
		
		if ( logger.isTraceEnabled() )
			logger.trace( "Looking for getter for property {} in class {} hierarchy.", property, clazz );
		
		
		Method method = null;
		
		/* Capitalizes the property name. */
		final String name = Character.toUpperCase( property.charAt( 0 ) ) + property.substring( 1 );
		
		/* Names or the methods to search. */
		final String[] methodNames = { "get" + name, "is"  + name };
		
		/*
		 * "currClazz" will be null when the top of the class hierarchy
		 * will be reached.
		 */
		Class<?> currClazz = clazz;
		
		/* Getter do not have any parameters... */
		final Class<?>[] none = (Class<?>[]) null;
		
		do{
			
			
			for( String methodName : methodNames )
			{
				
				try{
					
					
					method = currClazz.getDeclaredMethod( methodName, none );
					
					/* 
					 * The method class.getDeclaredMethods() returns also
					 * methods flagged as "synthetic" and "bridge", probably
					 * it's not the case for class.getDeclaredMethod(String,Class<?>...)
					 * but we perform the check for safety.
			         */
					if( method.isSynthetic() || method.isBridge() )
						continue;
					
					/* 
	    			 * If this point is reached it means that a matching method
	    			 * has been found, elsewhere a NoSuchMethodException has been
	    			 * thrown.
	    			 */
					if ( logger.isDebugEnabled() )
						logger.debug( "Found matching method {} in class {} hierarchy.", method, clazz );
					
					return method;
				
				} catch( NoSuchMethodException ex )
				{
					
					/*
					 * We avoid to manage the exception at this point.
					 * The management is in fact identical to the case:
					 * method found but not compatible, this management
					 * is performed proceeding to the next class.
					 */
					
				}
				
			}
			
			
		} while( (currClazz = currClazz.getSuperclass()) != null );
		
		if ( logger.isWarnEnabled() )
			logger.warn( "Hierarchy root reached; a getter for property {} cannot be found!", property );
		
	    return null;
	
	}
	

	/**
	 * Search, in the provided class, for a public access method
	 * (so called <code>setter</code>) related to the given property name.
	 * <p>
	 *  This method works properly for both classes and interfaces.
	 * </p>
	 *
	 * @param property name of the property to find.
	 * @param clazz    class into which to search.
	 * 
	 * @return the public {@link Method} if found, <tt>null</tt> otherwise.
	 * 
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 * @throws SecurityException see {@link Class#getDeclaredMethod(String, Class...)}.
	 */
	public static Method findPublicSetter( String property, Class<?> clazz )
	throws SecurityException, NullPointerException
	{
	    
	    if ( property == null || property.isEmpty() || clazz == null )
	    {
	        logger.error( "Property name and class cannot be null." );
	        
	        throw new NullPointerException( "Property name and class cannot be null." );
	        
	    }
	    
	    if ( logger.isTraceEnabled() )
	        logger.trace( "Looking for public setter for property {} in class {} hierarchy.", property, clazz );
	    
	    
	    /* Name of the method to search. */
	    final String setterName = "set" + Character.toUpperCase( property.charAt(0) ) + property.substring( 1 );
	    
        final List<Method> setters = new LinkedList<Method>();
        final Method[] allPublicMethods = clazz.getMethods();
	   
        /*
         * If we find a method with the given name and only one parameter
         * we add it to the list of possible setters.
         * We need to check if the method is flagged as "synthetic"
         * or "bridge" because also those methods are returned by
		 * class.getDeclaredMethods() but we don't want to return them.
         */
        for( Method method : allPublicMethods )
            if( ! method.isSynthetic() &&
            	! method.isBridge() &&
            	method.getParameterTypes().length == 1 &&
            	method.getName().equals(setterName) )
            {
                setters.add( method );
                
                /* 
                 * If this point is reached it means that a matching method
                 * has been found, elsewhere a NoSuchMethodException has been
                 * thrown.
                 */
                if( logger.isDebugEnabled() )
                    logger.debug( "Found matching method {} in class {} hierarchy.", method, clazz );
            }
        
        /* If we have only one matching method we return the setter. */
        if( setters.size() == 1 )
            return setters.get(0);
	    
        /* If the have no matching methods the return null. */
        if( setters.isEmpty() )
        {
            if( logger.isWarnEnabled() )
                logger.warn( "A public setter for property {} cannot be found!", property );
            return null;
        }
	    
        /*
         * If we have more than one possible setter we have to  claim it
         * checking any matching getter or field.
         */
	    final Method getter = findPublicGetter( property, clazz );
	    if( getter != null )
	        for( Method setter : setters )
	            if( getter.getReturnType().equals(setter.getParameterTypes()[0]) )
	                return setter;
	    
	    Field field = null;
	    Class<?> currentClass = clazz;
	        
	    do{
	        
	        try{
	        
	            field = currentClass.getDeclaredField( property );
	        
	        }catch( NoSuchFieldException ex ) {}
	        
	        currentClass = currentClass.getSuperclass();
	    
	    }while( field == null && currentClass != null );
	        
	    if( field != null )	        
	        for( Method setter : setters )
	            if( field.getType().equals(setter.getParameterTypes()[0]) )
	                return setter;
        
	    return null;
	    
	}
	
	
	/**
	 * Search, in the provided class, for a public access method
	 * (so called <code>getter</code>) related to the given property name.
	 * <p>
	 *  This method works properly for both classes and interfaces.
	 * </p>
	 *
	 * @param property name of the property to find.
	 * @param clazz    class into which to search.
	 * 
	 * @return the public {@link Method} if found, <tt>null</tt> otherwise.
	 * 
	 * @throws NullPointerException if any of the parameters is <code>null</code>.
	 * @throws SecurityException see {@link Class#getDeclaredMethod(String, Class...)}.
	 */
	public static Method findPublicGetter( String property, Class<?> clazz )
	throws SecurityException, NullPointerException
	{
	
		if ( property == null || property.isEmpty() || clazz == null )
		{
			logger.error( "Property name and class cannot be null." );
	
			throw new NullPointerException( "Property name and class cannot be null." );
			
		}
		
		if ( logger.isTraceEnabled() )
			logger.trace( "Looking for public getter for property {} in class {} hierarchy.", property, clazz );
		
		
		Method method = null;
		
		/* Capitalizes the property name. */
		final String name = Character.toUpperCase( property.charAt( 0 ) ) + property.substring( 1 );
		
		/* Names of the methods to search. */
		final String[] methodNames = { "get" + name, "is"  + name, "has" + name };
			
		/* Getter do not have any parameters... */
		final Class<?>[] none = (Class<?>[]) null;
		

		for( String methodName : methodNames )
		{
			
			try{
				
				method = clazz.getMethod( methodName, none );
				
				/* 
				 * The method class.getDeclaredMethods() returns also
				 * methods flagged as "synthetic" and "bridge", probably
				 * it's not the case for class.getDeclaredMethod(String,Class<?>...)
				 * but we perform the check for safety.
		         */
				if( method.isSynthetic() || method.isBridge() )
					continue;
				
				/* 
    			 * If this point is reached it means that a matching method
    			 * has been found, elsewhere a NoSuchMethodException has been
    			 * thrown.
    			 */
				if ( logger.isDebugEnabled() )
					logger.debug( "Found matching method {} in class {} hierarchy.", method, clazz );
				
				return method;
			
			} catch( NoSuchMethodException ex )
			{
				/*
				 * We avoid to manage the exception at this point.
				 * The management is in fact identical to the case:
				 * method found but not compatible, this management
				 * is performed proceeding to the next class.
				 */
			}
			
		}
		
		if ( logger.isWarnEnabled() )
			logger.warn( "A public getter for property {} cannot be found!", property );
		
	    return null;
	
	}
	
	/**
	 * Checks if the provided class represents a boolean.
	 * This method returns <code>true</code> if it represents
	 * both a native  type <code>boolean</code> or a type {@link Boolean}.

	 * @param clazz the class to be evaluated
	 * @return <tt>true</tt> if it represents a boolean. 
	 */
	public static boolean isBoolean( Class<?> clazz )
	{
		
		if ( clazz.equals( Boolean.class ) ||  clazz.equals( boolean.class ) )
			return true;
		
		return false;
		
	}
	
	/** Getter pattern to validate methods name and extract his related property name. */
	private static final Pattern getterPattern = Pattern.compile("^(?:get|is)([A-Z]{1})(.*)$");
	
	public static boolean isGetter( Method method )
	{
		
		/* Parameters check. */
		if( hasParameters(method) )
		{
			logger.debug( "Method {} has parameters.", method );
			return false;
		}
		
		/* Return type check. */
		if( ! hasReturn(method) )
		{
			logger.debug( "Method {} returns void.", method );
			return false;
		}
		
		/* Name pattern check. */
		if( ! (getterPattern.matcher(method.getName())).matches() )
		{
			logger.debug( "Method {} doesn't match '{}'.", method, getterPattern.pattern() );
			return false;
		}
		
		return true;
		
	}
	
	/**
	 * Extract a property name from a getter method.
	 * 
	 * <p>
	 * Given method must be a getter:
	 * <ul>
	 * <li>his name must be <tt>(get|is)&#60;property_name&#62;</tt> and
	 * property name must start with an uppercase letter (i.e.: match pattern:
	 * <tt>^(?:get|is)([A-Z]{1})(.*)$</tt>);</li>
	 * <li>return type must not be <tt>void</tt>;</li>
	 * <li>no parameters.</li>
	 * </ul>
	 * </p>
	 * 
	 * @param method getter method.
	 * @return getter propery name or <code>null</code> if isn't a getter.
	 */
	public static String propertyFromGetter( Method method ) throws IllegalArgumentException
	{
		
		/* Parameters check. */
		if( hasParameters(method) )
		{
			logger.debug( "Method {} has parameters.", method );
			return null;
		}
		
		/* Return type check. */
		if( ! hasReturn(method) )
		{
			logger.debug( "Method {} returns void.", method );
			return null;
		}
		
		/* Name pattern check. */
		final Matcher matcher;
		if( ! (matcher = getterPattern.matcher(method.getName())).matches() )
		{
			logger.debug( "Method {} doesn't match '{}'.", method, getterPattern.pattern() );
			return null;
		}
		
		return matcher.group(1).toLowerCase() + matcher.group(2);
		
	}
	
	/**
	 * Evaluate if given method has parameters or not.
	 * 
	 * @param method method to evaluate.
	 * @return {@code true} if given method has at least a parameter.
	 */
	public static boolean hasParameters( Method method )
	{
		
		return method.getParameterTypes().length != 0;
		
	}
	
	/**
	 * Evaluate if given method has a return type or is void.
	 * 
	 * @param method method to evaluate.
	 * @return {@code true} if given method has a return type.
	 */
	public static boolean hasReturn( Method method )
	{
		
		/* Return type check. */
		final Class<?> returnType = method.getReturnType();
		
		return ! ( returnType.equals( Void.TYPE ) || returnType.equals( Void.class ) );
		
	}
	
	/**
	 * Search, in the provided class, for the <tt>public</tt>
	 * {@link Constructor} matching given argument values.
	 * 
	 * <p>
	 * Argument values can be {@code null}, this facility will search for a no
	 * ambiguous constructor for the data given.
	 * </p>
	 * 
	 * @param clazz  class into which to search.
	 * @param values constructor arguments
	 * 
	 * @return found constructor or {@code null}
	 * 
	 * @throws IllegalArgumentException
	 *             if given parameters sesult in an ambiguous constructor
	 *             definition given parameters
	 * @throws SecurityException
	 *             see {@link Class#getConstructors()}
	 * 
	 */
	public static <X> Constructor<X> findPublicConstructor( Class<X> clazz, Object[] values )
			throws IllegalArgumentException, SecurityException
	{
		
		/* Extract types from values if possible (consider null values) */
		Class<?>[] types = null;		
		if ( values != null )
		{
			
			types = new Class<?>[ values.length ];
			
			Object value;
			for( int i = 0 ; i < values.length; ++i )
			{
				value = values[i];
				types[i] = value == null ? null : value.getClass();
			}
			
		}
		
		return findPublicConstructor( clazz, types );
		
	}
	
	/**
	 * Search, in the provided class, for the <tt>public</tt>
	 * {@link Constructor} matching given argument types.
	 * 
	 * <p>
	 * Argument types can be {@code null}, this facility will search for a no
	 * ambiguous constructor for the data given.
	 * </p>
	 * 
	 * @param clazz class into which to search.
	 * @param types constructor arguments types
	 * 
	 * @return found constructor or {@code null}
	 * 
	 * @throws IllegalArgumentException
	 *             if given parameters sesult in an ambiguous constructor
	 *             definition given parameter types
	 * @throws SecurityException
	 *             see {@link Class#getConstructors()}
	 * 
	 */
	public static <X> Constructor<X> findPublicConstructor( Class<X> clazz, Class<?>[] types )
			throws IllegalArgumentException, SecurityException
	{
		
		
		Constructor<X> constructor = null;
		
		if ( types == null || types.length == 0 )
		{
			
			try
			{
				
				constructor = clazz.getConstructor( (Class<?>[]) null );
				
				logger.debug( "Found default constructor {} in class {}", constructor, clazz );
				
			} catch ( NoSuchMethodException e )
			{
				
				logger.warn( "A default constructor in class {} cannot be found!", clazz );
				
			}
			
		} else
		{
			
			@SuppressWarnings("unchecked")
			final Constructor<X>[] constructors = (Constructor<X>[]) clazz.getConstructors();
			
			/* Constructors found counts */
			int matches = 0; 
			CONSTRUCTOR_LOOP : for ( Constructor<X> checking : constructors )
			{
				
				/*
				 * Only one constructor can have perfect compatibility, many can
				 * have it partial. If many partial compatible constructor
				 * exists and no perfect one the request is ambiguous and cannot
				 * be satisfied.
				 */
				Compatibility compatibility = checkCompatibility( types, checking );
				switch( compatibility )
				{
				
					case PERFECT:
						/* Reset match count, it's a perfect match */
						matches = 1;
						constructor = checking;
						logger.debug( "Found perfect matching constructor {}", constructor );
						break CONSTRUCTOR_LOOP;
						
					case SOME:
						++matches;
						constructor = checking;
						logger.debug( "Found compatible constructor {}, looking for a better match", constructor, clazz );
						
					case NONE:
						break;
						
					default:
						throw new IllegalStateException( "Unexpected compatibility state " + compatibility );
				
				}
				
			}
			
			/* Validate found count */
			switch ( matches )
			{
				case 0:
					logger.warn( "A constructor in class {} for parameters {} cannot be found!", clazz, (Object) types );
					break;
					
				case 1:
					logger.debug( "Found matching constructor {} in class {}.", constructor, clazz );
					break;
					
				default:
					throw new IllegalArgumentException( "Ambiguous constructor with type parameters " +
							Arrays.toString(types) + ". Fount " + matches );
			}
			
		} 
		
		return constructor;
		
	}
	
	/**
	 * Check compatility between types and costructor arguments
	 * 
	 * @param types       data types to evaluate
	 * @param constructor constructor instance to check
	 * 
	 * @return compatibility level
	 */
	private static Compatibility checkCompatibility( Class<?>[] types, Constructor<?> constructor )
	{
		
		final Class<?>[] ctypes = constructor.getParameterTypes();
		
		if ( ctypes.length != types.length )
			return Compatibility.NONE;
		
		Compatibility compatibility = Compatibility.PERFECT;
		Class<?> type, ctype;
		for( int i = 0; i < types.length; ++i )
		{
			
			type = types[i];
			ctype = ctypes[i];
			
			/* Ricorda che types può contenere null e autobox */
			if ( type == null )
			{
				
				compatibility = Compatibility.SOME;
				
			} else
			{
				
				if( type.equals( ctype ) ) 
				{
					
					switch ( compatibility )
					{
						case PERFECT:
							compatibility = Compatibility.PERFECT;
							break;
							
						case SOME:
							break;
							
						case NONE:							
						default:
							throw new IllegalStateException( "Unexpected compatibility state " + compatibility );
					}
					
				} else if ( ctype.isAssignableFrom( type ) )
				{
					
					switch ( compatibility )
					{
						case PERFECT:
							compatibility = Compatibility.SOME;
							break;
							
						case SOME:
							break;
							
						case NONE:							
						default:
							throw new IllegalStateException( "Unexpected compatibility state " + compatibility );
					}
					
				} else
				{
					
					boolean boxed = false;
					if ( type.isPrimitive() )
					{
						type = PRIMITIVES.get(type);
						boxed = true;
					}
					
					if ( ctype.isPrimitive() )
					{
						ctype = PRIMITIVES.get(ctype);
						boxed = true;
					}
					
					
					if ( boxed && ctype.isAssignableFrom( type ) )
					{
						
						switch ( compatibility )
						{
							case PERFECT:
							case SOME:
								compatibility = Compatibility.SOME;
								break;
								
							case NONE:							
							default:
								throw new IllegalStateException( "Unexpected compatibility state " + compatibility );
						}
						
					} else
					{
						return Compatibility.NONE;
					}
					
				}
				
			}
			
		}
		
		return compatibility;
		
	}
	
	
	/** Mapping primitive to object types */
	private static final Map<Class<?>,Class<?>> PRIMITIVES = new HashMap<Class<?>,Class<?>>();
	static
	{
		
		PRIMITIVES.put(Boolean.TYPE, Boolean.class);
		PRIMITIVES.put(Character.TYPE, Character.class);
		PRIMITIVES.put(Byte.TYPE, Byte.class);
		PRIMITIVES.put(Short.TYPE, Short.class);
		PRIMITIVES.put(Integer.TYPE, Integer.class);
		PRIMITIVES.put(Long.TYPE, Long.class);
		PRIMITIVES.put(Float.TYPE, Float.class);
		PRIMITIVES.put(Double.TYPE, Double.class);
		PRIMITIVES.put(Void.TYPE, Void.class);
		
	}
	
	/** Enumerate possible method/constructor compatibilities */
	private static enum Compatibility
	{
		/** Complete compatibility, exact match */
		PERFECT,
		
		/**
		 * Possible compatibility through supertypes, autoboxing, autounboxing
		 * and null values
		 */
		SOME, /* Autobox, supertype, null type */
		
		/** No compatibility */
		NONE;
	}
	
	
	
	
	
	
	

}
