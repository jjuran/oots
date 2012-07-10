package com.metamage.oots;

import android.os.AsyncTask;

import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;


public final class FileURLDownloadTask extends AsyncTask< Void, Integer, Void >
{
	private static HashMap< Integer, Integer > theDownloadsInProgress = new HashMap< Integer, Integer >();
	
	int itsID;
	
	URL itsUrl;
	
	File itsDestination;
	
	Completion itsCompletion;
	
	IOException itsSavedException;
	
	public FileURLDownloadTask( int id, URL url, File dest, Completion completion )
	{
		if ( theDownloadsInProgress.containsKey( id ) )
		{
			cancel( false );  // no need to interrupt, we never enter doInBackground()
			
			return;
		}
		
		theDownloadsInProgress.put( id, 0 );
		
		itsID          = id;
		itsUrl         = url;
		itsDestination = dest;
		itsCompletion  = completion;
	}
	
	protected Void doInBackground( Void... v )
	{
		try
		{
			HttpURLConnection urlConnection = (HttpURLConnection) itsUrl.openConnection();
			
			urlConnection.setRequestProperty( "Accept-Encoding", "identity" );
			
			try
			{
				InputStream input = urlConnection.getInputStream();
				
				try
				{
					String connectedHost = urlConnection.getURL().getHost();
					
					if ( !itsUrl.getHost().equals( connectedHost ) )
					{
						throw new ProtocolException( "HTTP redirect to " + connectedHost + " not allowed" );
					}
					
					File tempFile = new File( itsDestination.getParentFile(),
					                          itsDestination.getName() + "~" );
					
					FileOutputStream output = new FileOutputStream( tempFile );
					
					tempFile.deleteOnExit();
					
					try
					{
						int contentLength = urlConnection.getContentLength();
						
						if ( contentLength < 0 )
						{
							throw new ProtocolException( "Content-Length header field missing" );
						}
						
						byte[] buffer = new byte[ 4096 ];
						
						int totalBytesRead = 0;
						
						int bytes_read;
						
						while ( (bytes_read = input.read( buffer )) > 0 )
						{
							output.write( buffer, 0, bytes_read );
							
							totalBytesRead += bytes_read;
						}
						
						if ( totalBytesRead != contentLength )
						{
							final String contentRatio = Integer.toString( totalBytesRead )
							                          + " / "
							                          + Integer.toString( contentLength );
							
							if ( totalBytesRead < contentLength )
							{
								throw new EOFException( "Content length underrun: " + contentRatio );
							}
							else  // totalBytesRead > contentLength
							{
								throw new ProtocolException( "Content length overrun: " + contentRatio );
							}
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
		theDownloadsInProgress.remove( itsID );
		
		itsCompletion.call( itsSavedException );
	}
}

