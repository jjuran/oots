package com.metamage.oots;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public final class FileURLDownloadTask extends AsyncTask< Void, Integer, Void >
{
	URL itsUrl;
	
	File itsDestination;
	
	Completion itsCompletion;
	
	IOException itsSavedException;
	
	public FileURLDownloadTask( URL url, File dest, Completion completion )
	{
		itsUrl         = url;
		itsDestination = dest;
		itsCompletion  = completion;
	}
	
	protected Void doInBackground( Void... v )
	{
		try
		{
			HttpURLConnection urlConnection = (HttpURLConnection) itsUrl.openConnection();
			
			try
			{
				InputStream input = urlConnection.getInputStream();
				
				try
				{
					File tempFile = new File( itsDestination.getParentFile(),
					                          itsDestination.getName() + "~" );
					
					FileOutputStream output = new FileOutputStream( tempFile );
					
					tempFile.deleteOnExit();
					
					try
					{
						int contentLength = urlConnection.getContentLength();
						
						byte[] buffer = new byte[ 4096 ];
						
						int totalBytesRead = 0;
						
						int bytes_read;
						
						while ( (bytes_read = input.read( buffer )) > 0 )
						{
							output.write( buffer, 0, bytes_read );
							
							totalBytesRead += bytes_read;
						}
						
						tempFile.renameTo( itsDestination );
					}
					finally
					{
						output.close();
					}
				}
				finally
				{
					input.close();
				}
			}
			finally
			{
				urlConnection.disconnect();
			}
		}
		catch ( IOException e )
		{
			itsSavedException = e;
			
			itsDestination.delete();
		}
		
		return (Void) null;
	}
	
	protected void onProgressUpdate( Integer... params )
	{
	}
	
	protected void onPostExecute( Void v )
	{
		itsCompletion.call( itsSavedException );
	}
}

