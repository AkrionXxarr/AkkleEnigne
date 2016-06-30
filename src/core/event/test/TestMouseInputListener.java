package core.event.test;

import java.awt.event.MouseEvent;

import core.event.Event;
import core.input.InputEvent;

public class TestMouseInputListener implements Event.Listener
{
	final String prefix;
	
	public TestMouseInputListener(String prefix)
	{
		this.prefix = prefix;
	}

	@Override
	public void handleEvent(Event event)
	{
		MouseEvent e = ((InputEvent.Mouse.Data)event.getData()).e;
		
		System.out.println(prefix + ": (" + e.getX() + ", " + e.getY() + ")");
	}

	@Override
	public String getName()
	{
		return "TestMouseInputListener";
	}
}