package com.metamage.oots;

import android.app.Application;
import android.content.Context;

import java.io.File;


public final class App extends Application
{
	
	static Context context;
	
	static File cacheDir;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		context = this;
		
		cacheDir = getCacheDir();
	}
	
}

