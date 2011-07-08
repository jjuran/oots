package com.metamage.oots;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public final class Comic extends Activity implements Prep.Target
{
	
	final Prep.Task itsPrepTasks = new Prep.SerialTasks
	(
		new ComicLoad(),
		new ComicDisplay()
	);
	
	TextView itsCaption;
	
	Bitmap itsDisplayedBitmap;
	
	private void onMessage( String message )
	{
		if ( itsCaption != null )
		{
			itsCaption.setText( message );
		}
	}
	
	public boolean onException( IOException exception )
	{
		if ( exception == null )
		{
			return false;
		}
		
		onMessage( exception.getMessage() );
		
		return true;
	}
	
	public Prep.Task getPrepTasks()
	{
		return itsPrepTasks;
	}
	
	private static String getLocalFilenameForComic( int number ) throws IOException
	{
		// Pad to 4 chars with zeroes
		final String numeral = Integer.toString( 10000 + number ).substring( 1 );
		
		final String filename = "oots" + numeral + ".gif";
		
		return filename;
	}
	
	public final class ComicLoad implements Prep.Task
	{
		public boolean engage() throws IOException
		{
			if ( itsDisplayedBitmap != null )
			{
				return true;
			}
			
			final int number = Data.currentComicNumber();
			
			final String filename = getLocalFilenameForComic( number );
			
			final File cacheFile = Storage.getPicturesDir( filename );
			
			if ( cacheFile.exists() )
			{
				itsDisplayedBitmap = BitmapFactory.decodeStream( new FileInputStream( cacheFile ) );
				
				return true;
			}
			
			return false;
		}
	}
	
	private final class ComicDisplay implements Prep.Task
	{
		public boolean engage()
		{
			if ( itsCaption != null )
			{
				itsCaption = null;
				
				setContentView( R.layout.comic );
				
				ImageView v = (ImageView) findViewById( R.id.comic );
				
				v.setImageBitmap( itsDisplayedBitmap );
			}
			
			return true;
		}
	}
	
	private void setTitleForCurrentComic()
	{
		setTitle( "#" + Data.currentComicNumeral() + ": " + Data.currentComicTitle() );
	}
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		
		setTitleForCurrentComic();
		
		setContentView( R.layout.comic_loading );
		
		itsCaption = (TextView) findViewById( R.id.caption );
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		Prep.target = this;
		
		Prep.engageTasks();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		Prep.target = null;
	}
	
}

