package game.component.test;

import java.awt.event.KeyEvent;

import core.event.Event;
import core.event.EventManager;
import core.input.InputEvent;
import core.math.vector.Vector2f;
import game.component.base.GameComponent;
import game.engine.RenderContext;

public class BasicKeyMovement extends GameComponent implements Event.Listener
{
	protected boolean up, down, left, right;
	
	Vector2f dir = new Vector2f(0, 0);
	float speed;

	public BasicKeyMovement(float speed)
	{
		super("Basic Key Movement");
		
		this.speed = speed;
	}

	@Override
	public void onAdd()
	{
		EventManager.register(this, InputEvent.Key.pressed);
		EventManager.register(this, InputEvent.Key.released);
	}

	@Override
	public void onRemove()
	{
		EventManager.unregister(this, InputEvent.Key.pressed);
		EventManager.unregister(this, InputEvent.Key.released);
	}

	@Override
	public void start()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float deltaTime)
	{
		dir.x = 0; dir.y = 0;
		
		if (up)
			dir.y -= 1;
		if (left)
			dir.x -= 1;
		if (right)
			dir.x += 1;
		if (down)
			dir.y += 1;
			
		dir = dir.normalized().mul(speed * deltaTime);
		
		parent.local.pos.x += dir.x;
		parent.local.pos.y += dir.y; 
		
		// - = _ +
	}

	@Override
	public void draw(RenderContext renderContext)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleEvent(Event event)
	{
		InputEvent.Key.Data data = event.getData();
		
		if (event.eventType.id == InputEvent.Key.pressed.id)
		{
			switch (data.e.getKeyCode())
			{
			case KeyEvent.VK_W:
				if (!up)
					up = true;
				break;
			case KeyEvent.VK_A:
				if (!left)
					left = true;
				break;
			case KeyEvent.VK_S:
				if (!down)
					down = true;
				break;
			case KeyEvent.VK_D:
				if (!right)
					right = true;
				break;
			}
		}
		else
		{
			switch (data.e.getKeyCode())
			{
			case KeyEvent.VK_W:
				if (up)
					up = false;
				break;
			case KeyEvent.VK_A:
				if (left)
					left = false;
				break;
			case KeyEvent.VK_S:
				if (down)
					down = false;
				break;
			case KeyEvent.VK_D:
				if (right)
				right = false;
				break;
			}
		}
	}

	@Override
	public String getName()
	{
		// TODO Auto-generated method stub
		return null;
	}

}
