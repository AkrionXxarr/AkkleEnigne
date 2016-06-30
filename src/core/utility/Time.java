package core.utility;

// Tracks the elapsed time to provide a deltatime on request.
//
public class Time
{	
	private long curTime, lastTime;
	
	public Time()
	{
		curTime = 0;
		lastTime = 0;
	}
	
	public float deltaTime()
	{
		lastTime = curTime;
		curTime = System.currentTimeMillis();
		
		return (curTime - lastTime) / 1000.0f;
	}
	
	public long deltaTimeMillis()
	{
		lastTime = curTime;
		curTime = System.currentTimeMillis();
		
		return (curTime - lastTime);
	}
}
