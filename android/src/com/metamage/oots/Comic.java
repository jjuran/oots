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
import java.net.URL;


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
	
	private static String getImageUrlForComic( int number )
	{
		final int i = number - 1;
		
		if ( i >= Data.filenames.length )
		{
			return null;
		}
		
		final String filename = Data.filenames[ i ];
		
		return Files.IMAGES_DIR_URL + filename;
	}
	
	private static String getLocalFilenameForComic( int number ) throws IOException
	{
		// Pad to 4 chars with zeroes
		final String numeral = Integer.toString( 10000 + number ).substring( 1 );
		
		final String extension = number <= 934 ? ".gif" : ".png";
		
		final String filename = "oots" + numeral + extension;
		
		return filename;
	}
	
	private final Bitmap scaledBitmap( Bitmap bitmap )
	{
		final float density = getResources().getDisplayMetrics().density;
		
		if ( density != 1.0 )
		{
			bitmap = Bitmap.createScaledBitmap( bitmap,
			                                    (int) (bitmap.getWidth()  * density),
			                                    (int) (bitmap.getHeight() * density),
			                                    true );
		}
		
		return bitmap;
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
				itsDisplayedBitmap = scaledBitmap( BitmapFactory.decodeStream( new FileInputStream( cacheFile ) ) );
				
				return true;
			}
			
			new FileURLDownloadTask( number,
			                         new URL( getImageUrlForComic( number ) ),
			                         cacheFile,
			                         Prep.taskCompletion ).execute();
			
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

