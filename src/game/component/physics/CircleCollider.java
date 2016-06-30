package game.component.physics;

import core.math.vector.Vector2f;
import game.component.base.GameComponent;
import game.component.physics.base.PhysicsComponent;
import game.engine.RenderContext;

public class CircleCollider extends PhysicsComponent 
{
	public static final GameComponent.Type type = new GameComponent.Type(CircleCollider.class.getName());
			
	public Vector2f localOffset;
	public float radius;

	public CircleCollider(float radius, float xOff, float yOff) 
	{
		super("CircleCollider");
		
		this.radius = radius;
		this.localOffset = new Vector2f(xOff, yOff);
	}

	@Override
	public void update(float deltaTime) 
	{
	}

	@Override
	public void draw(RenderContext renderContext) 
	{
		// TODO Auto-generated method stub
		
		/*
		Graphics g = renderContext.getDisplay().getScreen().getBufferedImage().createGraphics();
		
		int x = (int)parent.world.pos.x;
		int y = (int)parent.world.pos.y;
		int r = (int)radius; // Width and Height for an oval will simply be 2r
		
		// The oval is that which fits within a rectangle, meaning x and y are top left corner and
		// need to be adjusted accordingly.
		//
		// First shift the corner right and down by half of the oval's rectangle, to "center" it.
		x += r;
		y += r;
		
		g.drawLine(x, y, x, y);
		
		// Then shift by the localOffset
		x -= (int)localOffset.x;
		y -= (int)localOffset.y;
		
		g.drawOval(x, y, r * 2, r * 2);
		
		g.dispose();
		*/
	}
}
