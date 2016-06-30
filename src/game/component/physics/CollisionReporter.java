package game.component.physics;

import game.engine.Game;
import game.events.physics.Collision;
import core.event.Event;
import core.math.vector.Vector2f;
import core.utility.Converter;

public class CollisionReporter implements Event.Listener
{
	private String name;
	private Game game;
	
	public CollisionReporter(String name, Game game)
	{
		this.name = name;
		this.game = game;
	}

	@Override
	public void handleEvent(Event event)
	{
		Collision.Data data = (Collision.Data)event.getData();
		
		Vector2f v1 = new Vector2f(data.a.getParent().world.pos);
		Vector2f v2 = new Vector2f(data.b.getParent().world.pos);
		
		game.getRenderContext().drawLine(Converter.ARGBToInt(255, 255, 255, 255), v1, v2, Float.MAX_VALUE);
	}

	@Override
	public String getName()
	{
		return name;
	}

}
