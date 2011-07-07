package com.metamage.oots;

import android.app.Application;
import android.content.Context;


public final class App extends Application
{
	
	static Context context;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		context = this;
	}
	
}

