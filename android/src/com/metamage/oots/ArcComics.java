package com.metamage.oots;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;


public final class ArcComics extends ListActivity
{
	ArrayAdapter< String > itsAdapter;
	
	private boolean setListArray( String[] strings )
	{
		if ( strings == null )
		{
			return false;
		}
		
		ArrayAdapter< String > adapter;
		
		adapter = new ArrayAdapter< String >( this,
		                                      android.R.layout.simple_list_item_1,
		                                      strings );
		
		setListAdapter( adapter );
		
		return true;
	}
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		
		setTitle( Data.currentArcName() );
		
		if ( setListArray( Data.getTitlesForCurrentArc() ) )
		{
			return;
		}
		
		itsAdapter = new ArrayAdapter< String >( this, android.R.layout.simple_list_item_1 );
		
		setListAdapter( itsAdapter );
		
		itsAdapter.add( "Loading titles..." );
	}
	
}

