package core.input;

import game.engine.Game;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import core.event.EventManager;

// Handles the input events coming from AWT and sends it out
// as an event to the event manager.
//
// AWT input events are not parallel with the main loop.
//
public class InputEngine
{
	/////////////////////////////////
	// Keyboard input handler
	//
	public static class Keyboard implements KeyListener
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			EventManager.addEvent(new InputEvent.Key(InputEvent.Key.pressed, e));
		}

		@Override
		public void keyReleased(KeyEvent e)
		{
			EventManager.addEvent(new InputEvent.Key(InputEvent.Key.released, e));
		}

		@Override
		public void keyTyped(KeyEvent e)
		{
			EventManager.addEvent(new InputEvent.Key(InputEvent.Key.typed, e));
		}
	}
	
	/////////////////////////////////
	// Mouse input handler
	//
	public static class Mouse implements MouseListener, MouseMotionListener
	{
		@Override
		public void mouseDragged(MouseEvent e)
		{
			EventManager.addEvent(new InputEvent.Mouse(InputEvent.Mouse.dragged, e));
		}

		@Override
		public void mouseMoved(MouseEvent e)
		{
			EventManager.addEvent(new InputEvent.Mouse(InputEvent.Mouse.moved, e));
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			EventManager.addEvent(new InputEvent.Mouse(InputEvent.Mouse.clicked, e));
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			EventManager.addEvent(new InputEvent.Mouse(InputEvent.Mouse.entered, e));
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			EventManager.addEvent(new InputEvent.Mouse(InputEvent.Mouse.exited, e));
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			EventManager.addEvent(new InputEvent.Mouse(InputEvent.Mouse.pressed, e));
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			EventManager.addEvent(new InputEvent.Mouse(InputEvent.Mouse.released, e));
		}
		
	}
	
	// Given the nature of gaining and losing focus, this event is triggered immediately
	// rather than waiting for the event manager to process it.
	//
	public static class Focus implements FocusListener
	{
		@Override
		public void focusGained(FocusEvent arg0)
		{	
			EventManager.trigger(new InputEvent.Focus(InputEvent.Focus.gained, true));
		}

		@Override
		public void focusLost(FocusEvent arg0)
		{
			EventManager.trigger(new InputEvent.Focus(InputEvent.Focus.lost, false));
		}
	}
	
	
	public final InputEngine.Keyboard keyboard;
	public final InputEngine.Mouse mouse;
	public final InputEngine.Focus focus;
	
	public final Game game;
	
	private Robot robot;
	boolean lockCursor = false;

	public InputEngine(Game game)
	{
		keyboard = new Keyboard();
		mouse = new Mouse();
		focus = new Focus();
		
		this.game = game;
		
		try
		{
			robot = new Robot();
		} catch (AWTException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		game.getDisplay().addKeyListener(keyboard);
		game.getDisplay().addMouseListener(mouse);
		game.getDisplay().addMouseMotionListener(mouse);
		game.getDisplay().addFocusListener(focus);
	}
	
	public void update()
	{
		if (lockCursor)
		{
			Point p = game.getDisplay().getLocationOnScreen();
			int width = game.getDisplay().getWidth();
			int height = game.getDisplay().getHeight();
			
			robot.mouseMove(p.x + width / 2, p.y + height / 2);
		}
	}
	
	public void lockMouseCursor(boolean lock)
	{
		lockCursor = lock;
	}
}
