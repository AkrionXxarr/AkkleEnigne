package game.object.utility;

import game.component.base.GameComponent;
import game.object.base.GameObject;
import core.event.Event;

public class AddRemoveComponentEvent extends Event
{
	public static class Data implements Event.Data
	{
		public final GameObject go;
		public final GameComponent comp;
		public final boolean added;
		
		public Data(GameObject go, GameComponent comp, boolean added)
		{
			this.go = go;
			this.comp = comp;
			this.added = added;
		}
	}
	
	public static final Event.Type eventType = new Event.Type("add_remove_game_component");
	
	public AddRemoveComponentEvent(GameObject go, GameComponent comp, boolean added)
	{
		super(eventType, new AddRemoveComponentEvent.Data(go, comp, added));
	}
	
	public AddRemoveComponentEvent(AddRemoveComponentEvent.Data data)
	{
		super(eventType, data);
	}
}
