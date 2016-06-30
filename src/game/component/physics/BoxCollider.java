package game.component.physics;

import core.math.vector.Vector2f;
import game.component.base.GameComponent;
import game.component.physics.base.PhysicsComponent;
import game.engine.RenderContext;

// TODO Change this so that the parent position represents the center 
// of the box rather than the top left corner.
public class BoxCollider extends PhysicsComponent 
{
	public static final GameComponent.Type type = new GameComponent.Type(BoxCollider.class.getName());
	
	public Vector2f localOffset = new Vector2f(0, 0);
	public float width = 0, height = 0;
	public float halfWidth = 0, halfHeight = 0;
	
	public BoxCollider(float width, float height, float xOff, float yOff) 
	{
		super("BoxCollider");
		
		this.width = width;
		this.height = height;
		halfWidth = width / 2.0f;
		halfHeight = height / 2.0f;
		
		localOffset = new Vector2f(xOff, yOff);
	}

	@Override
	public void update(float deltaTime) 
	{
	}

	@Override
	public void draw(RenderContext renderContext) 
	{
	}
}
