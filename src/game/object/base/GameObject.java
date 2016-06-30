package game.object.base;

import java.util.ArrayList;

import core.event.EventManager;
import core.utility.IdentityManager;
import game.component.base.GameComponent;
import game.engine.Game;
import game.engine.RenderContext;
import game.object.utility.AddRemoveComponentEvent;
import game.object.utility.AddRemoveObjectEvent;
import game.object.utility.Transform;

// Represents pretty much everything in the game.
//
// GameObject forms the scene graph.
// GameComponent forms the behavior.
//
public final class GameObject 
{
	protected Game game = null;
	
	public final String name;
	public final IdentityManager.Identity ident;
	
	private GameObject root = null;
	private GameObject parent = null;
	
	public Transform world = new Transform();
	public Transform local = new Transform();
	
	private ArrayList<GameObject> children;
	private ArrayList<GameComponent> components;
	
	public GameObject(String name)
	{
		this.name = name;
		ident = IdentityManager.requestIdentity(name);
		
		root = this;
		parent = null;
		
		children = new ArrayList<GameObject>();
		components = new ArrayList<GameComponent>();
	}
	
	public void start()
	{
		if (parent == null)
		{
			// TODO: Maybe a better way?
			world.pos = local.pos;
		}
		else
		{
			world.pos = parent.world.pos.add(local.pos);
		}
		
		for (int i = 0; i < children.size(); i++)
		{
			children.get(i).start();
		}
		
		for (int i = 0; i < components.size(); i++)
		{
			components.get(i).start();
		}
	}
	
	public void stop()
	{
		for (int i = 0; i < children.size(); i++)
		{
			children.get(i).stop();
		}
		
		for (int i = 0; i < components.size(); i++)
		{
			components.get(i).onRemove();
			components.get(i).stop();
		}
	}
	
	public void update(float deltaTime)
	{
		if (parent == null)
		{
			world.pos = local.pos;
		}
		else
		{
			world.pos = parent.world.pos.add(local.pos);
		}
		
		for (int i = 0; i < children.size(); i++)
		{
			children.get(i).update(deltaTime);
		}
		
		for (int i = 0; i < components.size(); i++)
		{
			components.get(i).update(deltaTime);
		}
	}
	
	public void draw(RenderContext renderContext)
	{
		for (int i = 0; i < children.size(); i++)
		{
			children.get(i).draw(renderContext);
		}
		
		for (int i = 0; i < components.size(); i++)
		{
			components.get(i).draw(renderContext);
		}
	}
	
	public void addChild(GameObject child)
	{
		children.add(child);
		
		child.setRoot(root);
		child.setParent(this);
		child.setGameRef(game);
		
		child.start();
		
		EventManager.addEvent(new AddRemoveObjectEvent(child, true));
	}
	
	public void removeChild(GameObject child)
	{
		child.stop();
		
		children.remove(child);
		child.setParent(null);
		
		EventManager.addEvent(new AddRemoveObjectEvent(child, false));
	}
	
	public void addComponent(GameComponent component)
	{
		components.add(component);

		component.setParent(this);
		component.setGameRef(game);
		component.onAdd();
		
		EventManager.addEvent(new AddRemoveComponentEvent(this, component, true));
	}
	
	public void removeComponent(GameComponent component)
	{
		components.remove(component);
		component.setParent(null);
		
		component.onRemove();
		
		EventManager.addEvent(new AddRemoveComponentEvent(this, component, false));
	}
	
	public GameObject findGameObject(String name)
	{		
		return findGameObject(name.hashCode());
	}
	
	private GameObject findGameObject(int id)
	{
		GameObject go = null;
		
		if (ident.hash == id)
		{
			go = this;
		}
		else
		{
			for (int i = 0; i < children.size(); i++)
			{
				go = children.get(i).findGameObject(id);
				if (go != null)
					break;
			}
		}
		
		return go;
	}
	
	public ArrayList<GameObject> findGameObjects(String name)
	{
		return findGameObjects(name.hashCode());
	}
	
	private ArrayList<GameObject> findGameObjects(int id)
	{
		ArrayList<GameObject> list = new ArrayList<GameObject>();
		
		if (ident.hash == id)
		{
			list.add(this);
		}
		
		for (int i = 0; i < children.size(); i++)
		{
			list.addAll(children.get(i).findGameObjects(id));
		}
		
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T findComponent(Class<T> c)
	{
		T ret = null;
		
		for (int i = 0; i < components.size(); i++)
		{
			Object obj = components.get(i);
			
			if (c.isInstance(obj))
			{
				ret = (T)obj;
			}
		}
		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> findComponents(Class<T> c)
	{
		ArrayList<T> list = new ArrayList<T>();
		
		for (int i = 0; i < components.size(); i++)
		{
			Object obj = components.get(i);
			
			if (c.isInstance(obj))
			{
				list.add((T)obj);
			}
		}
		
		return list;
	}
	
	public ArrayList<GameObject> getChildren()
	{
		return children;
	}
	
	public ArrayList<GameComponent> getComponents()
	{
		return components;
	}
	
	private void setRoot(GameObject root)
	{
		this.root = root;
	}
	
	public GameObject getRoot()
	{
		return root;
	}
	
	private void setParent(GameObject parent)
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
