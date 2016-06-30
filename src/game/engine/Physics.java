package game.engine;

import java.util.ArrayList;

import game.component.physics.base.PhysicsComponent;

// Rudimentary physics engine.
//
public class Physics 
{
	private final ArrayList<PhysicsComponent> components;
	
	public Physics() 
	{
		components = new ArrayList<PhysicsComponent>();
	}
	
	public void update(Game game)
	{	
		for (int itr1 = 0; itr1 < components.size(); itr1++)
		{
			for (int itr2 = itr1 + 1; itr2 < components.size(); itr2++)
			{
				PhysicsComponent.checkCollision(components.get(itr1), components.get(itr2));
			}
		}
	}
	
	public void register(PhysicsComponent component)
	{
		if (components.contains(component))
			return;
		
		components.add(component);
	}
	
	public void unregister(PhysicsComponent component)
	{
		if (!components.contains(component))
			return;
		
		components.remove(component);
	}
}
