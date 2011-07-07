package com.metamage.oots;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;


public final class Main extends Activity
{
	
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
	
}

