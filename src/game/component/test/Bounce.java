package game.component.test;

import core.math.vector.Vector2f;
import core.resource.audio.AudioClip;
import core.resource.audio.AudioManager;
import game.component.base.GameComponent;
import game.component.physics.base.CollisionData;
import game.component.physics.base.PhysicsComponent;
import game.component.utility.RequireComponent;
import game.engine.RenderContext;

public class Bounce extends GameComponent
{
	public static final GameComponent.Type type = new GameComponent.Type(Bounce.class.getName());
	
	Vector2f direction = new Vector2f(0, 0);
	
	float speed;
	
	RequireComponent reqComp;
	PhysicsComponent collider;
	
	AudioClip sfx;
	
	public Bounce(String name, float xDir, float yDir, float speed) 
	{
		super(name);
		
		direction = new Vector2f(xDir, yDir).normalized();
		this.speed = speed;
		
		sfx = new AudioClip("./res/sfx/", "hit2.wav");
	}

	@Override
	public void onAdd()
	{
		reqComp = new RequireComponent();
		
		reqComp.add(parent, PhysicsComponent.class);
		
		collider = parent.findComponent(PhysicsComponent.class);
	}

	@Override
	public void onRemove()
	{
		reqComp.release();
		
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
		if (parent.local.pos.x < 0)
			if (direction.x < 0)
				direction.x = -direction.x;
		if (parent.local.pos.x + 32 >= game.getDisplay().getScreen().getWidth())
			if (direction.x > 0)
				direction.x = -direction.x;
		if (parent.local.pos.y < 0)
			if (direction.y < 0)
				direction.y = -direction.y;
		if (parent.local.pos.y + 32 >= game.getDisplay().getScreen().getHeight())
			if (direction.y > 0)
				direction.y = -direction.y;

		for (int i = 0; i < collider.collisions.size(); i++)
		{
			CollisionData data = collider.collisions.get(0);
			
			if (data.justCollided)
			{
				AudioManager.play(sfx);
				
				Vector2f dir = new Vector2f(direction.x, direction.y);
				
				float f = dir.dot(data.normal);
				
				if (f > 0)
				{
					Vector2f v1 = data.normal.mul(f * 2.0f);
					dir = dir.sub(v1).normalized();
				}
				else
				{
					Vector2f v1 = data.normal.mul(f * -2.0f);
					dir = dir.sub(v1).normalized();
				}
				
				direction.x = dir.x;
				direction.y = dir.y;
			}
		}
		
		Vector2f t = direction.mul(speed * deltaTime);
		
		parent.local.pos.x += t.x;
		parent.local.pos.y += t.y;
	}

	@Override
	public void draw(RenderContext renderContext) 
	{
		
	}
}
