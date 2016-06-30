package game.component.base;

import core.utility.IdentityManager;
import game.engine.Game;
import game.engine.RenderContext;
import game.object.base.GameObject;

// Provide functionality to a game object.
//
// GameComponent.Type is the specific type of game component, 
// such it is to be defined by the inherited class.
//
// The name variable of the GameComponent.Type is to be the name of the relevant inherited class.
//
// The name variable of the GameComponent is to be the unique name for that specific instance of component.
//
// The ids in both cases are simply their hashed names.
//
public abstract class GameComponent 
{
	public static class Type
	{
		public final int id;
		public final String name;
		
		public Type(String name)
		{
			this.name = name;
			id = this.name.hashCode();
		}
	}
	
	protected Game game = null;
	
	public final GameComponent.Type instanceType;

	public final String name;
	public final IdentityManager.Identity ident;
	
	protected GameObject parent;
	
	public GameComponent(String name) 
	{ 
		this.instanceType = new GameComponent.Type(this.getClass().getName());
	
		this.name = name;
		ident = IdentityManager.requestIdentity(name);
	}
	
	public abstract void onAdd();
	public abstract void onRemove();
	
	public abstract void start();
	public abstract void stop();
	public abstract void update(float deltaTime);
	public abstract void draw(RenderContext renderContext);
	
	public void setParent(GameObject parent)
	{
		this.parent = parent;
	}
	
	public GameObject getParent()
	{
		return parent;
	}
	
	public void setGameRef(Game game)
	{
		this.game = game;
	}
	
	public Game getGameRef()
	{
		return game;
	}
}
