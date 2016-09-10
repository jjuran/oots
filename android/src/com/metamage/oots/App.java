package com.metamage.oots;

import android.app.Application;
import android.content.Context;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;


public final class App extends Application
{
	
	static Context context;
	
	static File cacheDir;
	
	public String[] LoadNLinesFromRawFile( int n, int id ) throws IOException
	{
		InputStream input = getResources().openRawResource( id );
		
		String[] lines = FileUtils.ReadNLinesFromInputStream( n, input );
		
		input.close();
		
		return lines;
	}
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		context = this;
		
		cacheDir = getCacheDir();
		
		try
		{
			// Load titles/hashes
			// ------------------
			
			final int n_comics = Data.countOfComicsInCompletedArcs();
			
			Data.titles    = LoadNLinesFromRawFile( n_comics, R.raw.titles    );
			Data.filenames = LoadNLinesFromRawFile( n_comics, R.raw.filenames );
		}
		catch ( IOException e )
		{
			// Shouldn't happen
		}
	}
	
}

