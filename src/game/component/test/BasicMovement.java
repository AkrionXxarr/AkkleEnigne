package game.component.test;

import core.math.vector.Vector3f;
import game.component.base.GameComponent;
import game.engine.RenderContext;

public class BasicMovement extends GameComponent 
{
	public static final GameComponent.Type type = new GameComponent.Type(BasicMovement.class.getName());
	
	private Vector3f dir = new Vector3f(0, 0, 0);
	
	private float xFactor, yFactor, zFactor, scale;
	
	float timer = 0;
	
	public BasicMovement(String name, float xFactor, float yFactor, float zFactor, float scale) 
	{
		super(name);
	
		this.xFactor = xFactor;
		this.yFactor = yFactor;
		this.zFactor = zFactor;
		this.scale = scale;
	}
	
	@Override
	public void onAdd()
	{
	}

	@Override
	public void onRemove()
	{
	}

	@Override
	public void start() 
	{
	}
	
	@Override
	public void stop()
	{
	}

	@Override
	public void update(float deltaTime) 
	{
		timer += deltaTime;
		
		float sin = (float)Math.sin(timer);
		float cos = (float)Math.cos(timer);
	
		dir.x = sin * xFactor;
		dir.y = cos * yFactor;
		dir.z = sin * zFactor;
		
		dir = dir.mul(scale);
		dir = dir.mul(deltaTime);
		
		parent.local.pos.x += dir.x;
		parent.local.pos.y += dir.y;
		parent.local.pos.z += dir.z;
	}

	@Override
	public void draw(RenderContext renderContext) {
		// TODO Auto-generated method stub

	}
}
