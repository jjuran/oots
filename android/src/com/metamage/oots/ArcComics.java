package com.metamage.oots;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;


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
	
	@Override
	protected void onListItemClick( ListView l, View v, int position, long id )
	{
		Data.goToComicOffsetWithinCurrentArc( position );
		
		startActivity( new Intent( "com.metamage.oots.action.COMIC" ) );
	}
	
}

