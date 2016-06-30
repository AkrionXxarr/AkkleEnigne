package core.event.test;

import core.event.Event;
import core.input.InputEvent;

public class TestFocusListener implements Event.Listener
{
	@Override
	public void handleEvent(Event event)
	{
		InputEvent.Focus.Data b = event.getData();
		
		if (b.hasFocus)
			System.out.println("Gained focus");
		else
			System.out.println("Lost focus");
	}

	@Override
	public String getName()
	{
		return "Focus Listener";
	}
}
