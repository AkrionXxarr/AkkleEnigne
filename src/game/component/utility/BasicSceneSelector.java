package game.component.utility;

import java.util.ArrayList;

import core.event.Event;
import core.event.EventManager;
import core.utility.console.events.ConsoleMessage;
import game.component.base.GameComponent;
import game.component.console.Console;
import game.engine.RenderContext;
import game.scenes.Scene;

public class BasicSceneSelector extends GameComponent implements Event.Listener
{	
	private RequireComponent reqComp;
	private Console console;
	
	private static final String select = "sel";
	private static final String availableScenes = "scenes";
	
	public final ArrayList<Scene> scenes;
	
	public BasicSceneSelector(String name)
	{
		super(name);
		
		scenes = new ArrayList<Scene>();
	}

	@Override
	public void onAdd()
	{
		reqComp = new RequireComponent();
		reqComp.add(parent, Console.class);
		
		console = parent.findComponent(Console.class);
		
		console.registerCommand(select, "Selects a scene");
		console.registerCommand(availableScenes, "Displays available scenes");
		
		EventManager.register(this, Console.Input.command);
	}

	@Override
	public void onRemove()
	{
		reqComp.release();
		
		EventManager.unregister(this, Console.Input.command);
	}

	@Override
	public void start()
	{
		String str = "Type \"scenes\" to see a list of available scenes.\n"
				   + "Type \"sel\" and the scene name to load it";
		
		ConsoleMessage.log(str);
	}

	@Override
	public void stop()
	{
	}

	@Override
	public void update(float deltaTime)
	{
	}

	@Override
	public void draw(RenderContext renderContext)
	{
	}
	
	public void addScene(Scene scene)
	{
		scenes.add(scene);
	}

	public String getListOfScenes()
	{
		String str = "Scenes:\n";
		
		for (int i = 0; i < scenes.size(); i++)
		{
			str += scenes.get(i).name;
			
			if (i + 1 < scenes.size())
				str += '\n';
		}
		
		return str;
	}

	@Override
	public void handleEvent(Event event)
	{
		String str = ((Console.Input.Data)event.getData()).str;
		
		String[] cmdAndArg = str.split(" ", 2);
		
		if (cmdAndArg[0].equalsIgnoreCase(BasicSceneSelector.select))
		{
			if (cmdAndArg.length > 1)
			{
				Scene scene = null;
				
				for (int i = 0; i < scenes.size(); i++)
				{
					Scene t = scenes.get(i);
					
					if (t.name.equals(cmdAndArg[1]))
					{
						scene = t;
						break;
					}
				}
				
				if (scene != null)
					game.setNextScene(scene);
				else
					ConsoleMessage.error("Scene \"" + cmdAndArg[1] + "\" not found.");
			}
			else
			{
				ConsoleMessage.log("Usage: sel <scene name>\n"
								 + "Scene names are case sensitive.");
			}
		}
		else if (cmdAndArg[0].equalsIgnoreCase(BasicSceneSelector.availableScenes))
		{
			ConsoleMessage.log(getListOfScenes());
		}
	}

	@Override
	public String getName()
	{
		return name;
	}
}
