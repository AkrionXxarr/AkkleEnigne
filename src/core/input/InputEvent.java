package core.input;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import core.event.Event;

// Container class for all the event classes that pertain to input events.
//
public class InputEvent
{
	//////////////////
	// Keyboard
	//
	public static class Key extends Event
	{
		public static class Data implements Event.Data
		{
			public final KeyEvent e;
			
			public Data(KeyEvent e)
			{
				this.e = e;
			}
		}
		
		public static final Event.Type pressed = new Event.Type("key_pressed");
		public static final Event.Type released = new Event.Type("key_released");
		public static final Event.Type typed = new Event.Type("key_typed");
		
		public Key(Type eventType, KeyEvent e)
		{
			super(eventType, new Data(e));
		}
	}
	
	/////////////
	// Mouse
	//
	public static class Mouse extends Event
	{
		public static class Data implements Event.Data
		{
			public final MouseEvent e;
			
			public Data(MouseEvent e)
			{
				this.e = e;
			}
		}
		
		public static final Event.Type dragged = new Event.Type("mouse_dragged");
		public static final Event.Type moved = new Event.Type("mouse_moved");
		public static final Event.Type entered = new Event.Type("mouse_entered");
		public static final Event.Type exited = new Event.Type("mouse_exited");
		public static final Event.Type clicked = new Event.Type("mouse_clicked");
		public static final Event.Type pressed = new Event.Type("mouse_pressed");
		public static final Event.Type released = new Event.Type("mouse_released");
		
		public Mouse(Type eventType, MouseEvent e)
		{
			super(eventType, new Data(e));
		}
	}
	
	//////////////
	// Focus
	//
	public static class Focus extends Event
	{
		public static class Data implements Event.Data
		{
			public final boolean hasFocus;
			
			public Data(boolean hasFocus)
			{
				this.hasFocus = hasFocus;
			}
		}
		
		public static final Event.Type gained = new Event.Type("focus_gained");
		public static final Event.Type lost = new Event.Type("focus_lost");
		
		public Focus(Type eventType, boolean hasFocus)
		{
			super(eventType, new Data(hasFocus));
		}
	}
}
