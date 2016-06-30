package game.component.physics;

import core.math.vector.Vector2f;
import game.component.base.GameComponent;
import game.component.physics.base.PhysicsComponent;
import game.engine.RenderContext;

public class PolygonCollider extends PhysicsComponent 
{
	public static final GameComponent.Type type = new GameComponent.Type(PolygonCollider.class.getName());
	
	public Vector2f localOffset;
	
	public Vector2f[] vertices;
	public int numOfVertices = 0;

	public PolygonCollider(String name) 
	{
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(float deltaTime) 
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(RenderContext renderContext) 
	{
		// TODO Auto-generated method stub

	}
}
