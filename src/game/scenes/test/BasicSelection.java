package game.scenes.test;

import core.math.vector.Vector2f;
import game.component.console.Console;
import game.component.utility.BasicSceneSelector;
import game.engine.Game;
import game.engine.RenderContext;
import game.object.base.GameObject;
import game.scenes.Scene;

public class BasicSelection extends Scene
{
	private final GameObject root = new GameObject("root");
	
	public BasicSelection()
	{
		super("BasicSelection");
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
		
		GameObject go = new GameObject("Console");
		BasicSceneSelector selector = new BasicSceneSelector("Basic Scene Selector");
		
		root.addChild(go);
		
		go.addComponent(
				new Console(
						"Console", 
						new Vector2f(0, 0), 
						game.getDisplay().getScreen().getWidth(), 
						game.getDisplay().getScreen().getHeight(), 
						100
					)
				);
		
		go.addComponent(selector);
		
		selector.addScene(new TestScene1());
		selector.addScene(new TestScene2());
		selector.addScene(new TestScene3());
		
		root.start();
	}

	@Override
	public void deactivate(Game game)
	{
		root.stop();
	}

}
