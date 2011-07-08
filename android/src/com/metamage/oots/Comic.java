package com.metamage.oots;

import android.app.Activity;
import android.os.Bundle;


public final class Comic extends Activity
{
	
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
	}
	
}

