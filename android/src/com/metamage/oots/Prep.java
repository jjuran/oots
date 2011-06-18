package com.metamage.oots;

import java.io.IOException;


public final class Prep
{
	
	public interface Task
	{
		public abstract boolean engage() throws IOException;
	}
	
	public interface Target
	{
		public abstract Task getPrepTasks();
		
		public abstract boolean onException( IOException exception );
	}
	
	static public final class SerialTasks implements Task
	{
		private Task[] itsTasks;
		
		public SerialTasks( Task... tasks )
		{
			itsTasks = tasks;
		}
		
		public boolean engage() throws IOException
		{
			for ( int i = 0;  i < itsTasks.length;  ++i )
			{
				Task nextTask = itsTasks[ i ];
				
				final boolean ready = nextTask.engage();
				
				if ( !ready )
				{
					return false;  // tasks are pending
				}
			}
			
			return true;  // all tasks are complete
		}
	}
	
	static public final class ParallelTasks implements Task
	{
		private Task[] itsTasks;
		
		public ParallelTasks( Task... tasks )
		{
			itsTasks = tasks;
		}
		
		public boolean engage() throws IOException
		{
			boolean result = true;
			
			for ( int i = 0;  i < itsTasks.length;  ++i )
			{
				Task nextTask = itsTasks[ i ];
				
				boolean ready = false;
				
				try
				{
					ready = nextTask.engage();
				}
				catch ( IOException e )
				{
					target.onException( e );
				}
				
				result = result && ready;
			}
			
			return result;
		}
	}
	
	
	static Target target;
	
	static public final void engageTasks()
	{
		if ( target != null )
		{
			Task tasks = target.getPrepTasks();
			
			if ( tasks != null )
			{
				try
				{
					tasks.engage();
				}
				catch ( IOException e )
				{
					target.onException( e );
				}
			}
		}
	}
	
	
	static public class TaskCompletion implements Completion
	{
		public void call( IOException exception )
		{
			if ( target != null  &&  !target.onException( exception ) )
			{
				engageTasks();
			}
		}
	}
	
	static Completion taskCompletion = new TaskCompletion();
	
}

