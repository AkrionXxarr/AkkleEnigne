package game.scenes.test;

import core.math.vector.Vector2f;
import game.component.console.Console;
import game.component.physics.CollisionReporter;
import game.engine.Game;
import game.engine.RenderContext;
import game.object.base.GameObject;
import game.scenes.Scene;

public class TestScene3 extends Scene
{
	private final GameObject root = new GameObject("root");
	
	CollisionReporter collisionReporter;
	
	public TestScene3() 
	{
		super("TestScene3");
	}
	
	@Override
	public void update(float deltaTime) 
	{	
		root.update(deltaTime);
	}

	@Override
	public void render(RenderContext renderContext) 
	{
		root.draw(renderContext);
	}

	@Override
	public void activate(Game game) 
	{			
		root.setGameRef(game);
		
		collisionReporter = new CollisionReporter("collision reporter", game);
		
		GameObject go = new GameObject("Console");
		
		root.addChild(go);
		
		go.addComponent(
				new Console(
						"Console", 
						new Vector2f(0, 0), 
						game.getDisplay().getScreen().getWidth(), 
						game.getDisplay().getScreen().getHeight(), 
						10
					)
				);
			
		root.start();
	}

	@Override
	public void deactivate(Game game) 
	{
		root.stop();
	}
}
