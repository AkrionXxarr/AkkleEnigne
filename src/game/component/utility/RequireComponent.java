package game.component.utility;

import java.util.ArrayList;

import core.event.Event;
import core.event.EventManager;
import game.component.base.GameComponent;
import game.object.base.GameObject;
import game.object.utility.AddRemoveComponentEvent;

// Ideas:
// - I can have this class register and listen for an event that is thrown
// whenever a component is added or removed from a game object in order
// to respond to a component being removed that was required.
//
// - Will try and use template types for the class type that's required

// Requires that a game component exists on a game object.
//
public class RequireComponent
{
	public static class Listener implements Event.Listener
	{
		private String name;
		private final GameObject go;
		private final GameComponent comp;

		public Listener(String name, GameObject go, GameComponent comp)
		{
			this.go = go;
			this.comp = comp;
		}

		@Override
		public void handleEvent(Event event)
		{
			AddRemoveComponentEvent.Data data = event.getData();
			
			if (!data.added)
			{
				if (go.ident.equals(data.go.ident))
				{
					// We're in the right object
					if (comp.ident.equals(data.comp.ident))
					{
						// Required component is being removed
						throw new RuntimeException("<" + comp.getClass().getName() + "> is a required component being removed from GameObject <" + go.name + ">");
					}
				}
			}
		}

		@Override
		public String getName()
		{
			return name;
		}
	}
	
	private ArrayList<RequireComponent.Listener> listeners = new ArrayList<RequireComponent.Listener>();
	
	// Throws an exception if a game component is not present.
	// If present, a listener is set up to monitor whether something 
	// is attempting to remove the component. An exception is thrown if so.
	//
	public <T> void add(GameObject go, Class<T> c)
	{	
		T comp = go.findComponent(c);
		
		if (comp == null)
			throw new RuntimeException("<" + c.getName() + "> is a required component for GameObject <" + go.name + ">");
		
		RequireComponent.Listener listener = new RequireComponent.Listener("RequireComponent", go, (GameComponent)comp);
		EventManager.register(listener, AddRemoveComponentEvent.eventType);
		listeners.add(listener);
	}
	
	public void release()
	{
		for (int i = 0; i < listeners.size(); i++)
		{
			EventManager.unregister(listeners.get(i), AddRemoveComponentEvent.eventType);
		}
	}
}
