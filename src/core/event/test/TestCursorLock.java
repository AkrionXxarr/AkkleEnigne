package core.event.test;

import java.awt.event.MouseEvent;

import game.engine.Game;
import core.event.Event;
import core.input.InputEvent;

public class TestCursorLock implements Event.Listener
{
	Game game;
	String name;
	
	public TestCursorLock(String name, Game game)
	{
		this.game = game;
		this.name = name;
	}

	@Override
	public void handleEvent(Event event)
	{
		InputEvent.Mouse.Data data = event.getData();
		MouseEvent e = data.e;
		
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			game.getInputEngine().lockMouseCursor(true);
		}
		else if (e.getButton() == MouseEvent.BUTTON3)
		{
			game.getInputEngine().lockMouseCursor(false);
		}
	}

	@Override
	public String getName()
	{
		return name;
	}
}