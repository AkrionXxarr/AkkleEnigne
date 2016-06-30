package core.event;

// Event to be thrown for any specified purpose.
//
// Other event related classes and interfaces are included in this class
// to pack the relevant code more closely together.
//
public class Event
{
	/////////////////////////////////////////////////
	// Event related classes and interfaces
	//
	public static interface Listener
	{
		public abstract void handleEvent(Event event);
		public abstract String getName();
	}
	
	public static class Type
	{
		public final int id;
		public final String name;
		
		public Type(String name)
		{
			this.name = name.toLowerCase();
			id = this.name.hashCode();
		}
	}
	
	public static interface Data
	{
	}
	
	
	////////////////////
	// Event
	//
	public final Event.Type eventType;
	private final Event.Data data;

	public Event(Event.Type eventType, Event.Data data) 
	{
		this.eventType = eventType;
		this.data = data;
	}

	@SuppressWarnings("unchecked")
	public <T extends Event.Data> T getData()
	{
		return (T) data;
	}
}
