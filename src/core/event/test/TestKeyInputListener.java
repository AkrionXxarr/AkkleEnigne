package core.event.test;

import java.awt.event.KeyEvent;

import core.event.Event;
import core.input.InputEvent;

public class TestKeyInputListener implements Event.Listener
{
	final String prefix;
	
	public TestKeyInputListener(String name, String prefix)
	{
		this.prefix = prefix;
	}

	@Override
	public void handleEvent(Event event)
	{
		KeyEvent e = ((InputEvent.Key.Data)event.getData()).e;
		
		System.out.println(prefix + ": " + Integer.toHexString(e.getKeyCode()));
	}
	
	public String getName()
	{
		return "TestKeyInputListener";
	}
}
