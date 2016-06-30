package core.engine;

import core.utility.Time;
import core.window.Display;

// Provides a template for the core game engine. 
//
public abstract class BaseEngine
{
	protected final Time time;
	
	protected final Display display;
	
	public BaseEngine(String title, int width, int height, int scale)
	{
		display = new Display(title, width, height, scale);
		display.requestFocus();
		
		time = new Time();
		time.deltaTimeMillis();
	}
	
	public abstract void initialize();
	
	public abstract void start();
	public abstract void stop();
	public abstract void run();
	
	public abstract void update(float deltaTime);
	public abstract void render();
	
	public final Display getDisplay()
	{
		return display;
	}
}
