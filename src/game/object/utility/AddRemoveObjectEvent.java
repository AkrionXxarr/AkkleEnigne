package game.object.utility;

import game.object.base.GameObject;
import core.event.Event;

public class AddRemoveObjectEvent extends Event
{
	public static class Data implements Event.Data
	{
		public final GameObject go;
		public final boolean added;
		
		public Data(GameObject go, boolean added)
		{
			this.go = go;
			this.added = added;
		}
	}
	
	public static final Event.Type eventType = new Event.Type("add_remove_game_object");
	
	public AddRemoveObjectEvent(GameObject go, boolean added)
	{
		super(eventType, new AddRemoveObjectEvent.Data(go, added));
	}
	
	public AddRemoveObjectEvent(AddRemoveObjectEvent.Data data)
	{
		super(eventType, data);
	}
}
