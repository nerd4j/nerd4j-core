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
package org.nerd4j.format;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.nerd4j.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class for the representation of bean annotated with {@link Formatted
 * &#64;Formatted}.
 * 
 * <p>
 * This handler takes care of creating a textual representation of the instance
 * passed as argument to {@link #toStringContent(Object)} and
 * {@link #toStringContent(Object, boolean)}. Each field or getter of the class
 * hierarchy annotated with {@link Formatted &#64;Formatted} will be used to
 * create the textual representation.
 * 
 * @author Nerd4j Team
 */
public class FormattedClassHandler
{

	/** Static logger for this class. */
	private static final Logger logger = LoggerFactory.getLogger( FormattedClassHandler.class );
	
	
	/* ***************** */
	/*     SINGLETON     */
	/* ***************** */
	
	
	/** Singleton instance of this handler. */
	private static final FormattedClassHandler instance = new FormattedClassHandler();
	
	
	/**
	 * Returns the singleton instance of this handler.
	 * 
	 * @return the singleton instance of this handler.
	 */
	public static FormattedClassHandler getInstance()
	{
		
		return instance;
		
	}
	
	
	/**
	 * Default constructor.
	 * <p>
	 * It is private because this class can have only
	 * one instance provided by {@link FormattedClassHandler#getInstance()}.
	 */
	private FormattedClassHandler()
	{
		
		super();
		
	}
	
	
	/* **************** */
	/*  IMPLEMENTATION  */
	/* **************** */
	
	
	/** String representation of {@code null} instances. */
	private static final String NULL_OBJECT_STRING = "<null>";
	
	/** String representation of unreachable instances because a null element in theirs path. */
	private static final String UNREACH_OBJECT_STRING = "<undef>";
	
	/** String representation of already seen bean instances to avoid infinite loops. */
	private static final String SEEN_OBJECT_STRING = "<seen>";
	
	/**
	 * Configurations cache.
	 * Since the retrieval of configuration is particularly burdensome
	 * we prefer to save this configuration for future reference.
	 * The use of a {@link ConcurrentHashMap} makes all the operations
	 * on the cache thread-safe.
	 */
	private final Map<Class<?>,List<FormattedConfiguration>> configurationCache =
		new ConcurrentHashMap<Class<?>,List<FormattedConfiguration>>();
	
	/** Already printed instances into current stack. */
	private static final ThreadLocal<ExecutionMemory> executions =
		new ThreadLocal<ExecutionMemory>();
	
	
	/* ****************** */
	/*  PUBLIC INTERFACE  */
	/* ****************** */
	

	/**
	 * Using reflection generates a string with the fields annotated
     * with {@link Formatted &#64;Formatted} present on the class if the
     * provided object and super-classes.
	 * <p>
	 * {@code null} values will always be printed.
	 * 
	 * @param object instance to be formatted.
	 * @return formatted string representation.
	 */
	public String toStringContent( Object object )
	{
		
		return toStringContent( object, true );
		
	}
	
	/**
	 * Using reflection generates a string with the fields annotated
     * with {@link Formatted &#64;Formatted} present on the class if the
     * provided object and super-classes.
     * <p>
     * {@code null} values will printed according to the
     * {@code printNull} flag.
	 * 
	 * @param object    instance to be formatted.
	 * @param printNull tells if {@code null} values should be printed.
	 * @return formatted string representation.
	 */
	public final String toStringContent( Object object, boolean printNull )
	{
		
		/*
		 * Retrieve current execution memory and create it if doesn't exists.
		 * 
		 * Execution memory will be used to handle cyclic to string (A -> B -> A).
		 * Already seen instances will not be printed.
		 */
		ExecutionMemory execution = executions.get();
		if ( execution == null )
		{
			
			execution = new ExecutionMemory( object );
			executions.set(execution);
			
		}
		
		/* If current instance has been already seen print a substitution. */
		if ( ! execution.seen.add( object ) )
			return SEEN_OBJECT_STRING;
		
		
		StringBuilder builder = new StringBuilder();
		try
		{
		
			/* Class of the object to be printed, i.e. the class to be inspected. */
			final Class<?> objectClass = object.getClass();
			
			/*
			 * If the configuration is found in the cache
			 * will be used, otherwise a new configuration
			 * will be created and stored.
			 */
			List<FormattedConfiguration> configurations = configurationCache.get( objectClass );
			
			if ( configurations == null )
			{
				
				configurations = createFormattedConfigurations( objectClass );
				configurationCache.put( objectClass, configurations );
				
			} else
			{
				logger.debug( "Found a cached configuration for class {}.", objectClass.getSimpleName() );
			}
			
			
			for( FormattedConfiguration configuration : configurations )
			{
				
				/* Instance on which the method toString() will be invoked. */
				Object objectToPrint = null;
				
				try
				{
					
					/* Try to get the right instance. */
					objectToPrint = getInstanceToPrint( object, configuration );
					
				} catch ( NullPointerException ex )
				{
					
					/*
					 * If the right instance to print isn't reachable, verifies if
					 * undefined elements elements should be printed and in case
					 * performs the replacement (same flag as nulls).
					 */
					objectToPrint = ( objectToPrint == null && printNull ) ? UNREACH_OBJECT_STRING : objectToPrint;
					
				}
		
				/*
				 * If no object to be printed was found, verifies if null
				 * elements should be printed and in case performs the
				 * replacement.
				 */
				objectToPrint = ( objectToPrint == null && printNull ) ? NULL_OBJECT_STRING : objectToPrint;
				
				/* If there is still non objects to print skips to the next configuration. */
				if ( objectToPrint == null )
					continue;
				
				
				/*
				 * If the current token is not the first,
				 * a token separator will be added.
				 */
				if ( builder.length() != 0 )
					builder.append( configuration.tokenSeparator );
		
				
				/* The key is added. */
				builder.append( configuration.key );
		
				/* The key-value separator is added. */
				builder.append( configuration.valueSeparator );
		
				/* The value is added. */
				builder.append( objectToPrint );
		
			}
			
			
		
		} catch ( Exception e )
		{
			
			logger.error( "Unexpected Exception while evaluating toStringContent for class {}.", object.getClass(), e );
		
		} finally
		{
			
			/*
			 * If current level is the top one clean thread local data.
			 */
			if ( execution.root == object )
			{
				execution.seen.clear();
				executions.remove();
			}
		
		}
		
		return builder.toString();
		
	}
	
	
	/* ***************** */
	/*  PRIVATE SECTION  */
	/* ***************** */
	
	/**
	 * Create and populate a {@link ReflectionObject} list with reflection data
	 * inferred from given properties.
	 * 
	 * @param properties       nested properties to be found.
	 * @param reflectionObject reflection object on which look for properties
	 * @param clazz            class which hosted &#64Formatted annotation.
	 * @param annotation       ruling annotation.
	 * @return new list of chained {@link ReflectionObject}.
	 */
	private List<ReflectionObject> createInvocationList( String[] properties, ReflectionObject reflectionObject, Class<?> clazz, Formatted annotation )
	{
		
		
		List<ReflectionObject> invokes = new ArrayList<ReflectionObject>( properties.length + 1 );
		invokes.add( reflectionObject );
		
		/* Class to inspect to find the a getter. */
		Class<?> lookupClazz = reflectionObject.getType();
		
		for ( String property : properties )
		{

			/*
			 * Tries to retrieve the getter...
			 * If the inspected Class instance (lookupClazz) is an interface
			 * must be called  the method findPublicGetter which handles
			 * the interface inheritance. Otherwise, if it is a class,
			 * the method findGetter should be called.
			 */
			Method method = null;
			
			try
			{
				method = lookupClazz.isInterface() ?
						ReflectionUtil.findPublicGetter( property, lookupClazz ) :
						ReflectionUtil.findGetter( property, lookupClazz );
						
			} catch ( Exception e )
			{ /* SOAK */ }
			
			/* Method not found handling. */
			if ( method == null )
			{
				
				if (logger.isErrorEnabled())
					logger.error( "Cannot found a getter method for property '{}' on {} {}. Annotation {} from class {} will be skipped.",
							new Object[] {
								property,
								lookupClazz.isInterface() ? "interface " : "class ",
								lookupClazz.getSimpleName(),
								annotation.toString(),
								clazz.getCanonicalName()
								} );

				/* Cleans the methods find so far and terminates the loop. */
				invokes = Collections.emptyList();
				break;
				
			} else
			{
				
				/*
				 * Check accessibility, nested path should access through public
				 * getters; only root class implementation should be known.
				 */
				int modifier = method.getModifiers();
				
				if ( !Modifier.isPublic( modifier ) )
					logger.warn( "Accessing a non public method; consider to change method " +
							"access or remove @Formatted {} from class {}.", 
							annotation.toString(), clazz.getCanonicalName()  );
				
			}
			
			/* Adds the retrieved method to the method chain. */
			invokes.add( new ReflectionMethod( method ) );
			
			/* Uses the return type as new class to inspect. */
			lookupClazz = method.getReturnType();
			
		}
		
		return invokes;
		
	}
	
	
	/**
	 * Creates the formatting configuration for a single method.
	 * 
	 * @param clazz      class for which to create the configuration.
	 * @param method     annotated method.
	 * @param annotation the corresponding annotation.
	 * @return the generated configuration.
	 */
	private FormattedConfiguration createFormattedConfiguration( Class<?> clazz, Method method, Formatted annotation )
	{
		
		logger.trace( "Generating a new Formatted configuration for method {}.", method.getName() );
		
		if ( ReflectionUtil.isGetter( method ) )
			return createFormattedConfiguration( clazz, new ReflectionMethod(method), annotation );
		
		
		logger.warn( "Annotated method {} isn't a getter. Annotation {} from class {} will be skipped.",
				new Object[] { method.getName(), annotation, clazz } );
		
		return null;
		
	}
	
	/**
	 * Creates the formatting configuration for a single field.
	 * 
	 * @param clazz      class for which to create the configuration.
	 * @param field      annotated field.
	 * @param annotation the corresponding annotation.
	 * @return the generated configuration.
	 */
	private FormattedConfiguration createFormattedConfiguration( Class<?> clazz, Field field, Formatted annotation )
	{
		
		logger.trace( "Generating a new Formatted configuration for field {}.", field.getName() );
		
		return createFormattedConfiguration( clazz, new ReflectionField(field), annotation );
		
	}
	
	
	/**
	 * Creates the formatting configuration for a single {@link ReflectionObject}.
	 * 
	 * @param clazz      class for which to create the configuration.
	 * @param reflective annotated <tt>ReflectionObject</tt>.
	 * @param annotation the corresponding annotation.
	 * @return the generated configuration.
	 */
	private FormattedConfiguration createFormattedConfiguration( Class<?> clazz, ReflectionObject reflective, Formatted annotation )
	{
		
		final String propertyPath = annotation.value();
		
		String[] propertyTokens; 
		
		/* Split given path by dots if there is data in property path. */
		if ( propertyPath.isEmpty() )
			propertyTokens = new String[0];
		else
			propertyTokens = propertyPath.split( Pattern.quote(".") );
		
		/*
		 * Prepares the list of methods/fields to be invoked in chain. The count
		 * of methods to invoke matches the count of tokens in the path plus one.
		 */
		List<ReflectionObject> invokes = createInvocationList( propertyTokens, reflective, clazz, annotation );
		
		
		/* If no errors occur and there is a chain of methods... */
		if ( !invokes.isEmpty() )
		{
			/* A configuration will be created. */
			FormattedConfiguration configuration = new FormattedConfiguration();
			
			/* If the key parameter is empty uses the property path as default. */
			configuration.key   = annotation.key().isEmpty() ? reflective.getPropertyName() : annotation.key();
			
			configuration.tokenSeparator = annotation.tokenSeparator();
			configuration.valueSeparator = annotation.valueSeparator();
			
			configuration.invokes = invokes;
			
			logger.debug( "Generated a new Formatted configuration for annotation {} from class {}.",
					annotation, clazz.getCanonicalName());
			
			logger.trace( "Generated Formatted configuration: {}.", configuration );

			return configuration;
			
		}
		
		return null;
		
	}
	
	
	/**
	 * Creates a list of formatting configurations for the given class.
	 *
	 * @param clazz class to inspect.
	 * @return list of configurations.
	 */
	private List<FormattedConfiguration> createFormattedConfigurations( Class<?> clazz )
	{

		logger.trace( "Generating a new Formatted configuration for class {}.", clazz.getSimpleName() );

		List<FormattedConfiguration> result = new ArrayList<FormattedConfiguration>();

		
		/* Retrieves annotated fields and related annotations. */
		Map<Field,Formatted> fields = ReflectionUtil.findAnnotatedFields( Formatted.class, clazz );
		
		for( Map.Entry<Field,Formatted> field : fields.entrySet() )
		{
			
			FormattedConfiguration configuration = createFormattedConfiguration(clazz, field.getKey(), field.getValue());
			
			/* If a valid configuration has been created will be added to the result. */
			if ( configuration != null )
				result.add( configuration );
			
		}
		
		
		/* Retrieves annotated methods and related annotations. */
		Map<Method,Formatted> methods = ReflectionUtil.findAnnotatedMethods( Formatted.class, clazz );

		for( Map.Entry<Method,Formatted> method : methods.entrySet() )
		{
			
			FormattedConfiguration configuration = createFormattedConfiguration(clazz, method.getKey(), method.getValue());
			
			/* If a valid configuration has been created will be added to the result. */
			if ( configuration != null )
				result.add( configuration );
			
		}
		
		logger.debug( "Generated a new Formatted configuration for class {}; configuration size: {}.",
				clazz.getSimpleName(), result.size() );

		return result;
		
	}
	
	
	/**
	 * Returns the instance of the object to print given the instance on which
	 * to apply the configuration.
	 * 
	 * @param object        instance on which to apply the configuration.
	 * @param configuration configuration to apply.
	 * @return the object to print, or {@code null}.
	 * 
	 * @throws NullPointerException
	 *             if the right instance to print isn't reachable because a null
	 *             instance in her path.
	 */
	private Object getInstanceToPrint( Object object, FormattedConfiguration configuration )
	{
		
		/* Instance on which the method toString() will be invoked. */
		Object objectToPrint = object;
		
		
		for( ReflectionObject reflective : configuration.invokes )
		{
			
			/*
			 * Verifies the method accessibility. We define a method/field
			 * "accessible" if it is public and his declaring class is public or
			 * if is status has been modified by reflection.
			 * 
			 * If the method/field status needs to be modified, we do not
			 * restore the previous status to prevent the need to do the same
			 * operation again. This approach is safe because the change of the
			 * status is limited to the method instance and such instance is
			 * never used outside this class.
			 */
			final boolean isPublic = Modifier.isPublic( reflective.getModifiers() & reflective.getDeclaringClass().getModifiers());
			
			final boolean notAccessible = !( isPublic || reflective.isAccessible() );
			
			try {
				
				if ( notAccessible )
				{
					logger.warn( "Backing {} wasn't accessible; changing accessibility status.", reflective );
					
					reflective.setAccessible( true );
				}
				
				/*
				 * Invokes the method and updates the object to print.
				 * 
				 * If property path isn't reachable cause of a null object in
				 * the hierarchy a NullPointerException will be thrown.
				 * 
				 * Example: a.b.c path with b equals to null. The element c
				 * cannot be reached because b nullity.
				 */
				objectToPrint = reflective.getValue( objectToPrint );
			
			} catch ( IllegalAccessException ex ) {
				
				/* In case of failure in calling the access Method/Field. */
				logger.error( "Cannot access {}.", reflective, ex );
				return null;
				
			} catch ( IllegalArgumentException ex )
			{
				/* This exception should never me thrown. */
				logger.error( "Unexpected exception.", ex );
				return null;

			} catch ( InvocationTargetException ex )
			{
				/* This exception should never me thrown. */
				logger.error( "Unexpected exception.", ex );
				return null;

			} 
			
		}
		
		return objectToPrint;
		
	}
	
	
	
	/* *************************** */
	/*  ANCILLARY IMPLEMENTATIONS  */
	/* *************************** */
	
	
	/**
	 * This inner class represent the memory supporting toString print cycle
	 * detections.
	 * <p>
	 * Once completely generated all toStringContent on the stack it will be
	 * cleaned.
	 * 
	 * @author Nerd4j Team
	 */
	private static final class ExecutionMemory
	{
		
		public Object root;
		public Set<Object> seen;
		
		public ExecutionMemory( Object root )
		{
			
			this.root = root;
			this.seen = new HashSet<Object>(1);
			
		}
		
	}
	
	
	/**
	 * This inner class represents the configuration
	 * element related to an annotation {@link Formatted @Formatted}.
	 * Each configuration will be cached so that it will be computed
	 * only once.
	 * 
	 * @author Nerd4j Team
	 */
	private static class FormattedConfiguration
	{
		
		/** Output key to be displayed. */
		private String key;
		
		/** Token separator. */
		private String tokenSeparator;
		
		/** Key - value separator. */
		private String valueSeparator;

		/** List of methods/fields to be chained. */
		private List<ReflectionObject> invokes;
		
		
		/**
		 * Returns a string representation of this object.
		 *
		 * @return string representation of this object.
		 */
		@Override
		public String toString()
		{
			/*
			 * For obvious reasons this class cannot use
			 * the annotation @Formatted.
			 */
			return FormattedConfiguration.class.getSimpleName() +
				"[key=" + key +
				", tokenSeparator=" + tokenSeparator +
				", valueSeparator=" + valueSeparator +
				", invokes=" + invokes + "]"; 
		}
		
	}
	
	
	/**
	 * Wrapper interface for {@link Field} and {@link Method}.
	 * 
	 * @author Nerd4j Team
	 */
	private interface ReflectionObject
	{
		
		/**
		 * Returns the Java language modifiers as an integer. The
		 * {@code Modifier} class should be used to decode the modifiers.
		 * 
		 * @return Java language modifiers.
		 * @see Modifier
		 */
		public int getModifiers();
		
		/**
		 * Get the value of the <tt>accessible</tt> flag for this object.
		 * 
		 * @return the value of the object's <tt>accessible</tt> flag
		 * 
		 * @see AccessibleObject#isAccessible()
		 */
		public boolean isAccessible();
		
		/**
		 * Set the <tt>accessible</tt> flag for this object to the indicated
		 * boolean value.
		 * 
		 * @param flag the new value for the <tt>accessible</tt> flag
		 * @throws SecurityException if the request is denied.
		 * 
		 * @see AccessibleObject#setAccessible(boolean)
		 */
		public void setAccessible( boolean flag ) throws SecurityException;
		
		/**
		 * Returns the property name identified by this instance.
		 * 
		 * @return the property name.
		 */
		public String getPropertyName();
		
		/**
		 * Returns the corresponding object type, i.e.: the type of
		 * {@link #getValue(Object)}.
		 * 
		 * @return corresponding type.
		 */
		public Class<?> getType();
		
		/**
		 * Find the corresponding object value using reflective calls on given instance.
		 * 
		 * @param instance
		 * @return object value.
		 * 
		 * @throws IllegalArgumentException
		 * @throws IllegalAccessException
		 * @throws InvocationTargetException
		 * 
		 * @see Method#invoke(Object, Object...)
		 * @see Field#get(Object)
		 */
		public Object getValue( Object instance ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException;
		
		/**
		 * Returns the {@code Class} object representing the class or
		 * interface that declares the object represented by this
		 * {@code ReflectionObject}.
		 * 
		 * @return class represented by this object.
		 */
		public Class<?> getDeclaringClass();
		
	}
	
	/**
	 * ReflectionObject implementation for Method.
	 * 
	 * @author Nerd4j Team
	 */
	private class ReflectionMethod implements ReflectionObject
	{
		
		/** Wrapped instance. */
		private final Method method;

		public ReflectionMethod( Method method )
		{
			
			this.method = method;
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getModifiers()
		{
			
			return method.getModifiers();
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAccessible()
		{
			
			return method.isAccessible();
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getPropertyName()
		{
			
			return ReflectionUtil.propertyFromGetter( method );
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setAccessible(boolean flag)
		{
			
			method.setAccessible(flag);
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Class<?> getType()
		{
			
			return method.getReturnType();
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getValue( Object instance ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException
		{
			
			return method.invoke( instance, (Object[]) null );
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Class<?> getDeclaringClass()
		{
			
			return method.getDeclaringClass();
			
		}
		
		/**
		 * Returns a string representation of this object.
		 *
		 * @return string representation of this object.
		 */
		@Override
		public String toString()
		{
			
			/*
			 * For obvious reasons this class cannot use
			 * the annotation @Formatted.
			 */
			return ReflectionMethod.class.getSimpleName() +
					"[method=" + method + "]";
			
		}
		
	}
	
	/**
	 * ReflectionObject implementation for Field.
	 * 
	 * @author Nerd4j Team
	 */
	private class ReflectionField implements ReflectionObject
	{
		
		/** Wrapped instance. */
		private final Field field;

		public ReflectionField(Field field)
		{
			
			this.field = field;
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public int getModifiers()
		{
			
			return field.getModifiers();
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean isAccessible()
		{
			
			return field.isAccessible();
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public void setAccessible(boolean flag)
		{
			
			field.setAccessible(flag);
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public String getPropertyName()
		{
			
			return field.getName();
			
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Class<?> getType()
		{
			
			return field.getType();
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getValue( Object instance ) throws IllegalArgumentException, IllegalAccessException
		{
			
			return field.get( instance );
			
		}
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Class<?> getDeclaringClass()
		{
			
			return field.getDeclaringClass();
			
		}

		/**
		 * Returns a string representation of this object.
		 *
		 * @return string representation of this object.
		 */
		@Override
		public String toString()
		{
			
			/*
			 * For obvious reasons this class cannot use
			 * the annotation @Formatted.
			 */
			return ReflectionField.class.getSimpleName() +
					"[field=" + field + "]";
			
		}
		
	}
}
