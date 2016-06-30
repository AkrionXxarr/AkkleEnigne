package game.component.console;

import core.event.Event;
import core.event.EventManager;
import core.utility.console.events.ConsoleMessage;
import game.component.base.GameComponent;
import game.component.utility.RequireComponent;
import game.engine.RenderContext;

// A basic list of commands for the console component.
//
public class BasicConsoleCommands extends GameComponent implements Event.Listener
{
	private RequireComponent reqComp;
	private Console console;

	private static final String listCommands = "commands";
	private static final String echoMessage = "echo";
	private static final String clearScreen = "cls";
	private static final String charSet = "charset";
	//private static final String version = "version";
	
	public BasicConsoleCommands(String name)
	{
		super(name);
	}
	
	@Override
	public void onAdd()
	{
		reqComp = new RequireComponent();
		reqComp.add(parent, Console.class);
		
		console = parent.findComponent(Console.class);
		
		console.registerCommand(listCommands, "Displays list of commands");
		console.registerCommand(echoMessage, "Echos the following string back to the console");
		console.registerCommand(clearScreen, "Clears the console");
		console.registerCommand(charSet, "Displays the entire available character set");
		//console.registerCommand(version, "Displays the current engine version");
		
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
		
	}

	@Override
	public void stop()
	{
		
	}

	@Override
	public void update(float deltaTime)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(RenderContext renderContext)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handleEvent(Event event)
	{
		String str = ((Console.Input.Data)event.getData()).str;
		
		String[] cmdAndArg = str.split(" ", 2);
		
		if (cmdAndArg[0].equalsIgnoreCase((BasicConsoleCommands.echoMessage)))
		{
			if (cmdAndArg.length > 1)
			{
				ConsoleMessage.log(": " + cmdAndArg[1]);
			}
			else
			{
				ConsoleMessage.log(": ");
			}
		}
		else if (cmdAndArg[0].equalsIgnoreCase(BasicConsoleCommands.clearScreen))
		{
			console.clearConsole();
		}
		else if (cmdAndArg[0].equalsIgnoreCase(BasicConsoleCommands.listCommands))
		{
			console.listCommands();
		}
		else if (cmdAndArg[0].equalsIgnoreCase(BasicConsoleCommands.charSet))
		{
			console.printCharSet();
		}
	}

	@Override
	public String getName()
	{
		return name;
	}

}
