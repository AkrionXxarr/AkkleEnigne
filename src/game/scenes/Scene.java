package game.scenes;

import game.engine.Game;
import game.engine.RenderContext;

// Defines a collection of game objects and components for the current scene.
//
public abstract class Scene 
{
	public final String name;
	
	public Scene(String name)
	{
		this.name = name;
	}
	
	public abstract void update(float deltaTime);
	public abstract void render(RenderContext renderContext);
	
	public abstract void activate(Game game);
	public abstract void deactivate(Game game);
}
