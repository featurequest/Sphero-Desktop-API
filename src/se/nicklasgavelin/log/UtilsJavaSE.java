/**
 * BlueCove - Java library for Bluetooth
 * Copyright (C) 2006-2009 Vlad Skarzhevskyy
 * 
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * 
 * @author vlads
 * @version $Id$
 */
package se.nicklasgavelin.log;

import java.util.Collection;

/**
 * 
 * J2ME/J9 compatibility module.
 * 
 * <p>
 * <b><u>Your application should not use this class directly.</u></b>
 */
public class UtilsJavaSE
{
	static final boolean javaSECompiledOut = false;

	static class StackTraceLocation
	{
		public String className;
		public String methodName;
		public String fileName;
		public int lineNumber;
	}

	static interface JavaSE5Features
	{
		public void clearProperty( String propertyName );
	}

	static boolean java13 = false;
	static boolean java14 = false;
	static boolean detectJava5Helper = true;
	static JavaSE5Features java5Helper;
	static final boolean ibmJ9midp = detectJ9midp();
	static final boolean canCallNotLoadedNativeMethod = !ibmJ9midp;

	private UtilsJavaSE()
	{
	}

	private static boolean detectJ9midp()
	{
		String ibmJ9config;
		try
		{
			ibmJ9config = System.getProperty( "com.ibm.oti.configuration" );
		}
		catch( SecurityException webstart )
		{
			return false;
		}
		return ( ibmJ9config != null ) && ( ibmJ9config.indexOf( "midp" ) != -1 );
	}

	static StackTraceLocation getLocation( Collection<?> fqcnSet )
	{
		if( java13 || ibmJ9midp )
		{
			return null;
		}
		if( !javaSECompiledOut )
		{
			if( !java14 )
			{
				try
				{
					Class.forName( "java.lang.StackTraceElement" );
					java14 = true;
				}
				catch( ClassNotFoundException e )
				{
					java13 = true;
					return null;
				}
			}
			try
			{
				return getLocationJava14( fqcnSet );
			}
			catch( Throwable e )
			{
				java13 = true;
			}
		}
		return null;
	}

	private static StackTraceLocation getLocationJava14( Collection<?> fqcnSet )
	{
		if( !UtilsJavaSE.javaSECompiledOut )
		{
			StackTraceElement[] ste = new Throwable().getStackTrace();
			for( int i = 0; i < ste.length - 1; i++ )
			{
				if( fqcnSet.contains( ste[i].getClassName() ) )
				{
					String nextClassName = ste[i + 1].getClassName();
					if( nextClassName.startsWith( "java." ) || nextClassName.startsWith( "sun." ) )
					{
						continue;
					}
					if( !fqcnSet.contains( nextClassName ) )
					{
						StackTraceElement st = ste[i + 1];
						StackTraceLocation loc = new StackTraceLocation();
						loc.className = st.getClassName();
						loc.methodName = st.getMethodName();
						loc.fileName = st.getFileName();
						loc.lineNumber = st.getLineNumber();
						return loc;
					}
				}
			}
		}
		return null;
	}
}