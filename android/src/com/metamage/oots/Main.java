package com.metamage.oots;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;


public final class Main extends Activity implements Prep.Target
{
	Prep.Task itsPrepTasks = new Prep.SerialTasks
	(
		new FrontCoverDownload(),
		new FrontCoverDisplay()
	);
	
	Bitmap itsFrontCover;
	
	private void onMessage( String message )
	{
		setTitle( message );
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
	
	private final class FrontCoverDownload implements Prep.Task
	{
		public boolean engage() throws IOException
		{
			if ( itsFrontCover != null )
			{
				return true;
			}
			
			final File cacheFile = new File( App.cacheDir, Files.FRONTCOVER_FILENAME );
			
			if ( cacheFile.exists() )
			{
				itsFrontCover = BitmapFactory.decodeStream( new FileInputStream( cacheFile ) );
				
				return true;
			}
			
			new FileURLDownloadTask( Files.FRONTCOVER_ID,
			                         new URL( Files.FRONTCOVER_IMAGE_URL ),
			                         cacheFile,
			                         Prep.taskCompletion ).execute();
			
			return false;
		}
	}
	
	private final class FrontCoverDisplay implements Prep.Task
	{
		public boolean engage() throws IOException
		{
			ImageView v = (ImageView) findViewById( R.id.frontcover );
			
			v.setImageBitmap( itsFrontCover );
			
			return true;
		}
	}
	
	public void onClickWebSite( View v )
	{
		startActivity( new Intent( Intent.ACTION_VIEW, Uri.parse( Web.GITP ) ) );
	}
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		
		setContentView( R.layout.main );
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

