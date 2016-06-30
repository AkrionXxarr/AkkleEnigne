package core.event;

import java.util.ArrayList;
import java.util.HashMap;

import core.utility.Time;

// Handles everything necessary to process and distribute events.
//
// Singleton class, access is done through static methods using the instance variable.
//
// Methods are synchronized in order to handle systems that may run on separate threads.
// An example of such a system is the input system. This engine uses event driven input
// and the events coming in from AWT are not parallel with the main loop.
//
public class EventManager 
{
	private static EventManager instance = null;

	private final Time time;
	private final HashMap<Integer, ArrayList<Event.Listener>> eventListeners;
	private final ArrayList<Event> events;
	
	public EventManager() 
	{
		if (instance == null)
			instance = this;
		else
			System.out.println("Error: EventManager already created");

		time = new Time();
		eventListeners = new HashMap<Integer, ArrayList<Event.Listener>>();
		events = new ArrayList<Event>();
		
		time.deltaTimeMillis();
	}
	
	// Processes the event queue.
	//
	// Events will be processed until queue is empty or
	// the allowed process time (in milliseconds) has elapsed.
	//
	public void process(long maxProcessTimeMillis)
	{
		long processTime = 0;
		
		synchronized(instance)
		{
			while (!events.isEmpty())
			{
				Event event = events.remove(0);
	
				ArrayList<Event.Listener> listeners = eventListeners.get(event.eventType.id);
			
				if (listeners != null)
				{
					for (int i = 0; i < listeners.size(); i++)
					{
						listeners.get(i).handleEvent(event);
					}
				}
				
				processTime += time.deltaTimeMillis();
				
				if (processTime >= maxProcessTimeMillis)
					break;
			}
		}
	}
	
	// Adds an event to the event queue
	//
	public static void addEvent(Event event)
	{
		synchronized(instance)
		{
			instance.events.add(event);
		}
	}
	
	// Fires an event immediately, bypassing the queue
	//
	public static void trigger(Event event)
	{
		synchronized(instance)
		{
			ArrayList<Event.Listener> listeners = instance.eventListeners.get(event.eventType.id);
			
			if (listeners != null)
			{
				for (int i = 0; i < listeners.size(); i++)
				{
					listeners.get(i).handleEvent(event);
				}
			}
		}
	}
	
	// Adds an event listener
	//
	public static void register(Event.Listener listener, Event.Type eventType)
	{
		synchronized(instance)
		{
			if (instance.eventListeners.containsKey(eventType.id))
			{
				instance.eventListeners.get(eventType.id).add(listener);
			}
			else
			{
				ArrayList<Event.Listener> t = new ArrayList<Event.Listener>();
				t.add(listener);
				instance.eventListeners.put(eventType.id, t);
			}
		}
	}
	
	// Removes an event listener
	//
	public static void unregister(Event.Listener listener, Event.Type eventType)
	{
		synchronized(instance)
		{
			if (instance.eventListeners.containsKey(eventType.id))
			{
				instance.eventListeners.get(eventType.id).remove(listener);
			}
			else
			{
				throw new RuntimeException("Unregister error: " + eventType.name + " wasn't registered.");
			}
		}
	}
}
