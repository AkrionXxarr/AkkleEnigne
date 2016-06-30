package core.utility.console.events;

import core.event.Event;
import core.event.EventManager;

// Allows for an easy way to throw a formatted console message event.
// 
// How console message events are handled is up to the user, and an event can
// be thrown to include a file path for logging.
//
public class ConsoleMessage extends Event
{
	public static class Data implements Event.Data
	{
		public final String message;
		public final String filePath;
		public final boolean logToFile;
		
		public Data(String message)
		{
			this.message = message;
			filePath = "";
			logToFile = false;
		}
		
		public Data(String message, String filePath)
		{
			this.message = message;
			this.filePath = filePath;
			this.logToFile = true;
		}
	}
	
	public static final Event.Type log = new Event.Type("console_message_log");
	public static final Event.Type warning = new Event.Type("console_message_warning");
	public static final Event.Type error = new Event.Type("console_message_error");
	
	public ConsoleMessage(Event.Type eventType, String message)
	{
		super(eventType, new ConsoleMessage.Data(message));
	}
	
	public ConsoleMessage(Event.Type eventType, String message, String filePath)
	{
		super(eventType, new ConsoleMessage.Data(message, filePath));
	}
	
	public ConsoleMessage(Event.Type eventType, ConsoleMessage.Data data)
	{
		super(eventType, data);
	}
	
	public static void log(String msg)
	{
		EventManager.addEvent(new ConsoleMessage(ConsoleMessage.log, msg));
	}
	public static void log(String msg, String filePath)
	{
		EventManager.addEvent(new ConsoleMessage(ConsoleMessage.log, msg, filePath));
	}
	
	public static void warning(String msg)
	{
		msg = "[Warning] " + msg;
		EventManager.addEvent(new ConsoleMessage(ConsoleMessage.warning, msg));
	}
	public static void warning(String msg, String filePath)
	{
		msg = "[Warning] " + msg;
		EventManager.addEvent(new ConsoleMessage(ConsoleMessage.warning, msg, filePath));
	}
	
	public static void error(String msg)
	{
		msg = "<Error> " + msg;
		EventManager.addEvent(new ConsoleMessage(ConsoleMessage.error, msg));
	}
	public static void error(String msg, String filePath)
	{
		msg = "<Error> " + msg;
		EventManager.addEvent(new ConsoleMessage(ConsoleMessage.error, msg, filePath));
	}
}
