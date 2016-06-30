package core.utility.console.listeners;

import core.event.Event;
import core.utility.console.events.ConsoleMessage;

// A basic listener for pushing console message events to the system console.
// Logging is not handled.
//
public class ConsoleMessageListener implements Event.Listener
{
	private String name;

	public ConsoleMessageListener(String name)
	{
		this.name = name;
	}

	@Override
	public void handleEvent(Event event)
	{
		ConsoleMessage.Data data = event.getData();
		
		System.out.println(data.message);
		
		if (data.logToFile)
			System.out.println("LogToFile not implemented");
	}

	@Override
	public String getName()
	{
		return name;
	}

}
