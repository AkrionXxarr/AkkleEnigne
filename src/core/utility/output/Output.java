package core.utility.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import core.event.Event;
import core.event.EventManager;

// Allows for dated or undated messages to be output to a log file.
//
// Simply instantiate the class and call its start() function.
// Then Output.Log events can be thrown and handled.
//
public class Output
{
	public static class Log extends Event
	{
		public static class Data implements Event.Data
		{
			public final String fileName, msg;
			public final boolean dated;
			
			public Data(String fileName, String msg, boolean dated)
			{
				this.fileName = fileName;
				this.msg = msg;
				this.dated = dated;
			}
		}
		
		public Log(String fileName, String msg, boolean dated)
		{
			super(Output.log, new Data(fileName, msg, dated));
		}
	}
	
	private static class Listener implements Event.Listener
	{
		private String name;
		private final Output output;
		
		public Listener(String name, Output output)
		{
			this.name = name;
			this.output = output;
		}

		@Override
		public void handleEvent(Event event)
		{
			if (event.eventType == Output.log)
			{
				Log.Data log = event.getData();
				
				String fileName = output.date + "_log_" + log.fileName;
				String msg = log.msg;
				
				if (log.dated)
				{
					DateFormat df = new SimpleDateFormat("[MM/dd/yyyy hh:mm:ssa]");
					Date today = Calendar.getInstance().getTime();		
					String date = df.format(today);
					
					msg = date + " " + msg;
				}
	
				File file = new File(output.filePath + "/log/" + fileName);
	
				try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true), "utf-8")))
				{
					writer.write(msg);
					writer.newLine();
				} catch (UnsupportedEncodingException e)
				{
					e.printStackTrace();
				} catch (FileNotFoundException e)
				{
					e.printStackTrace();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		@Override
		public String getName()
		{
			return name;
		}
	}
	
	public static final Event.Type log = new Event.Type("output_log");
	
	private final Listener listener;
	private final String filePath;
	private final String date;
	
	public Output(String filePath)
	{
		this.filePath = filePath;
		listener = new Listener("Logger_listener", this);
		
		DateFormat df = new SimpleDateFormat("[MM-dd-yyyy--hhmmssa]");
		Date today = Calendar.getInstance().getTime();
		
		date = df.format(today);
	}
	
	public void start()
	{	
		EventManager.register(listener, Output.log);
	}
	
	public void stop()
	{
		EventManager.unregister(listener, Output.log);
	}
}
