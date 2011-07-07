package com.metamage.oots;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public final class FileUtils
{
	
	static String[] ReadNLinesFromInputStream( int n, InputStream inputStream ) throws java.io.IOException
	{
		String[] result = new String[ n ];
		
		BufferedReader in = new BufferedReader( new InputStreamReader( inputStream ) );
		
		for ( int i = 0;  i < n;  ++i )
		{
			result[i] = in.readLine();
		}
		
		return result;
	}
	
}

