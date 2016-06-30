package game.events.physics;

import game.component.physics.base.PhysicsComponent;
import core.event.Event;

public class Collision extends Event
{
	public static class Data implements Event.Data
	{	
		public final PhysicsComponent a, b;
		
		public Data(PhysicsComponent a, PhysicsComponent b)
		{
			this.a = a;
			this.b = b;
		}
	}
	
	public static final Event.Type eventType = new Event.Type("game_physics_collision");
	
	public Collision(PhysicsComponent a, PhysicsComponent b)
	{
		super(eventType, new Collision.Data(a, b));
	}

	public Collision(Event.Data data)
	{
		super(eventType, data);
	}

}
