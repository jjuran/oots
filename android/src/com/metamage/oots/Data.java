package com.metamage.oots;


public final class Data
{
	
	static String[] titles;
	
	static private int currentArcOffset = 0;
	
	static private int currentOffset = 0;
	
	static int countOfComicsInCompletedArcs()
	{
		return Arcs.starts[ Arcs.starts.length - 1 ];
	}
	
	static int countOfAllComics()
	{
		return titles.length;
	}
	
	static String currentArcName()
	{
		return Arcs.names[ currentArcOffset ];
	}
	
	static String[] getTitlesForCurrentArc()
	{
		if ( currentArcOffset == Arcs.starts.length - 1 )
		{
			return null;
		}
		
		final int begin = countOfComicsBeforeCurrentArc();
		final int end   = countOfComicsBeforeNextArc   ();
		
		final int n = end - begin;
		
		String[] result = new String[ n ];
		
		for ( int i = 0;  i < n;  ++i )
		{
			result[ i ] = titles[ begin + i ];
		}
		
		return result;
	}
	
	static int countOfComicsBeforeCurrentArc()
	{
		final int begin = Arcs.starts[ currentArcOffset ];
		
		return begin;
	}
	
	static int countOfComicsBeforeNextArc()
	{
		final int nextArcOffset = currentArcOffset + 1;
		
		if ( nextArcOffset >= Arcs.starts.length )
		{
			return countOfAllComics();
		}
		
		final int end = Arcs.starts[ nextArcOffset ];
		
		return end;
	}
	
	static void goToArcOffset( int offset )
	{
		currentArcOffset = offset;
		
		currentOffset = Arcs.starts[ offset ];
	}
	
}

