package com.metamage.oots;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;


public final class Arcs extends ListActivity
{
	
	static final int[] starts;
	
	static final String[] names;
	
	static
	{
		final int ARC_START = 0;
		final int ARC_TITLE = 1;
		
		final String[] arc_lines = new String[]
		{
			"0001	Dungeon",
			"0121	Paladin",
			"0301	War",
			"0485	Separation",
			"0673	Western Continent"
		};
		
		final int n_arcs = arc_lines.length;
		
		starts = new int   [ n_arcs ];
		names  = new String[ n_arcs ];
		
		for ( int i = 0;  i < n_arcs;  ++i )
		{
			String[] fields = arc_lines[i].split( "\t" );
			
			starts[i] = Integer.parseInt( fields[ ARC_START ] ) - 1;
			names [i] =                   fields[ ARC_TITLE ];
		}
	}
	
	@Override
	public void onCreate( Bundle savedInstanceState )
	{
		super.onCreate( savedInstanceState );
		
		setListAdapter( new ArrayAdapter< String >( this,
		                                            android.R.layout.simple_list_item_1,
		                                            names ) );
	}
	
}

