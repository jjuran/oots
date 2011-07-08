package com.metamage.oots;

import android.os.Environment;

import java.io.File;
import java.io.IOException;


public final class Storage
{
	
	static final int STORAGE_NONE   = 0;
	static final int STORAGE_RDONLY = 1;
	static final int STORAGE_RDWR   = 2;
	
	public static int getAccessLevel()
	{
		final String state = Environment.getExternalStorageState();
		
		if ( state.equals( Environment.MEDIA_MOUNTED ) )
		{
			return STORAGE_RDWR;
		}
		
		if ( state.equals( Environment.MEDIA_MOUNTED_READ_ONLY ) )
		{
			return STORAGE_RDONLY;
		}
		
		return STORAGE_NONE;
	}
	
	public static File getPicturesDir( String filename ) throws IOException
	{
		final int accessLevel = getAccessLevel();
		
		if ( accessLevel == STORAGE_NONE )
		{
			throw new IOException( "External storage is not mounted." );
		}
		
		final File pics = App.context.getExternalFilesDir( Environment.DIRECTORY_PICTURES );
		
		final File result = new File( pics, filename );
		
		if ( accessLevel == STORAGE_RDONLY  &&  !result.exists() )
		{
			throw new IOException( "External storage is mounted read-only." );
		}
		
		return result;
	}
	
}

