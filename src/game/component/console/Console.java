package game.component.console;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import core.event.Event;
import core.event.EventManager;
import core.input.InputEvent;
import core.math.vector.Vector2f;
import core.math.vector.Vector3f;
import core.utility.Converter;
import core.utility.console.events.ConsoleMessage;
import game.component.base.GameComponent;
import game.component.graphics.BasicBackground;
import game.component.utility.MessageBox;
import game.component.utility.RequireComponent;
import game.component.utility.MessageBox.Pair;
import game.engine.RenderContext;

// A fully functional console interface.
//
public class Console extends GameComponent implements Event.Listener
{
	public static class Input extends Event
	{
		public static class Data implements Event.Data
		{
			public final String str;
			
			public Data(String str)
			{
				this.str = str;
			}
		}
		
		public static final Event.Type command = new Event.Type("console_command");

		public Input(Type eventType, String str)
		{
			super(eventType, new Data(str));
		}
	}
	
	private final MessageBox messageBox;
	private final BasicBackground background;
	private final BasicConsoleCommands basicCommands;
	
	private RequireComponent reqComp;
	
	private final int primaryColor, secondaryColor, tertiaryColor, warnColor, errorColor;
	
	private String input = new String();
	private Point cursorPos = new Point();
	
	private final MessageBox.Pair[] consoleBuffer;
	private final int conBufferWidth;
	private final int conBufferHeight;
	private int yScroll = 0;
	private int startInputLine = 0;
	private int endInputLine = 0;
	private boolean bufferNeedsUpdate;
	
	private char inputHeader = '>';
	private char cursor = 219;
	private float cursorFlashRate = 0;
	private float elapsedFlashTime = 0;
	private boolean cursorIsShowing = true;
	
	private final ArrayList<String> registeredCommands, commandDescriptions;
	
	public Console(String name, 
			Vector2f pos, 
			int conWidth, 
			int conHeight, 
			int additionalLines)
	{
		this(
			name, 
			pos, 
			conWidth, 
			conHeight, 
			additionalLines, 
			Converter.RGBToInt(44, 120, 220),
			Converter.RGBToInt(142, 13, 35),
			Converter.RGBToInt(141, 183, 233),
			Converter.RGBToInt(255, 255, 0),
			Converter.RGBToInt(255, 0, 0),
			0.5f);
	}

	public Console(
			String name, 
			Vector2f pos, 
			int conWidth, 
			int conHeight, 
			int additionalLines,
			int primaryColor, 
			int secondaryColor, 
			int tertiaryColor,
			int warnColor,
			int errorColor,
			float cursorFlashRate)
	{
		super(name);
		
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		this.tertiaryColor = tertiaryColor;
		this.warnColor = warnColor;
		this.errorColor = errorColor;
		this.cursorFlashRate = cursorFlashRate;
		
		messageBox = new MessageBox(
				"Console Message Box", 
				new Vector3f(pos.x, pos.y, 1000), 
				conWidth, 
				conHeight, 
				0, 
				1, 
				this.primaryColor,
				MessageBox.FONT_TYPE.FONT_8x8);

		background = new BasicBackground("background", (int) pos.x, (int) pos.y, conWidth, conHeight, 999, 11, 11, 15);
		
		basicCommands = new BasicConsoleCommands("Basic Console Commands");
		
		if (additionalLines < 0)
			additionalLines = 0;
		
		this.conBufferWidth = messageBox.getBufferWidth();
		this.conBufferHeight = additionalLines + messageBox.getBufferHeight();
		
		consoleBuffer = new Pair[this.conBufferWidth * this.conBufferHeight];
		registeredCommands = new ArrayList<String>();
		commandDescriptions = new ArrayList<String>();
	}
	
	@Override
	public void onAdd()
	{
		parent.addComponent(background);
		parent.addComponent(messageBox);
		parent.addComponent(basicCommands);
		
		reqComp = new RequireComponent();
		reqComp.add(parent, MessageBox.class);
		reqComp.add(parent,  BasicBackground.class);
		reqComp.add(parent, BasicConsoleCommands.class);
	
		EventManager.register(this, InputEvent.Key.typed);
		EventManager.register(this, InputEvent.Key.pressed);
		EventManager.register(this, ConsoleMessage.log);
		EventManager.register(this, ConsoleMessage.warning);
		EventManager.register(this, ConsoleMessage.error);
		
	}
	
	@Override
	public void onRemove()
	{
		reqComp.release();
		
		EventManager.unregister(this, InputEvent.Key.typed);
		EventManager.unregister(this, InputEvent.Key.pressed);
		EventManager.unregister(this, ConsoleMessage.log);
		EventManager.unregister(this, ConsoleMessage.warning);
		EventManager.unregister(this, ConsoleMessage.error);
	}

	@Override
	public void start()
	{
		printInputLine();
	}

	@Override
	public void stop()
	{
	}

	@Override
	public void update(float deltaTime)
	{
		if (cursorFlashRate > 0)
		{
			elapsedFlashTime += deltaTime;
			
			if (elapsedFlashTime >= cursorFlashRate)
			{
				elapsedFlashTime = 0;
				
				if (cursorIsShowing)
				{
					clearCursor();
				}
				else
				{
					printCursor();
				}
				
				cursorIsShowing = !cursorIsShowing;
			}
		}
		if (bufferNeedsUpdate)
		{
			messageBox.setBuffer(getClippedBuffer());
			bufferNeedsUpdate = false;
		}
	}

	@Override
	public void draw(RenderContext renderContext)
	{	
	}
	
	// Typing functions
	protected void typedCharacter(char c)
	{
		input += c;
		printCharAtCursor(c, secondaryColor);
		printCursor();
		elapsedFlashTime = 0;
		cursorIsShowing = true;
	}
	
	protected void backspace()
	{
		if (input.length() > 0)
		{
			input = input.substring(0, input.length() - 1);
			clearInputLine();
			printInputLine();
		}
	}
	
	protected void enter()
	{
		if (isValidCommand(input))
		{
			if (argIsQuestion(input))
				ConsoleMessage.log(getDescription(input));
			else
				EventManager.addEvent(new Console.Input(Console.Input.command, input));
		}
		
		clearCursor();
		nextLine();
		startInputLine = cursorPos.y;
		endInputLine = startInputLine;
		
		input = "";
		
		printInputLine();
	}
	
	// Command functions
	public void listCommands()
	{
		String str = "Commands:\n";
		
		for (int i = 0; i < registeredCommands.size(); i++)
		{
			str += "- " + registeredCommands.get(i) + "\n";
		}
		
		printString(str);
	}
	
	public String getDescription(String str)
	{
		String subStr = str.split(" ", 2)[0].toLowerCase();
		
		for (int i = 0; i < registeredCommands.size(); i++)
		{
			if (subStr.equals(registeredCommands.get(i)))
			{
				return commandDescriptions.get(i);
			}
		}
		
		return null;
	}
	
	public boolean registerCommand(String command, String description)
	{
		command = command.toLowerCase();
		
		if (commandExists(command))
			return false;
		
		registeredCommands.add(command);
		commandDescriptions.add(description);
		
		return true;
	}
	
	private boolean commandExists(String command)
	{
		for (int i = 0; i < registeredCommands.size(); i++)
		{
			if (registeredCommands.get(i).equals(command))
				return true;
		}
		return false;
	}
	
	private boolean isValidCommand(String str)
	{
		str = str.toLowerCase();
		String subStr = "";
		
		for (int i = 0; i < str.length(); i++)
		{
			char c = str.charAt(i);
			
			if (c == ' ')
				break;
			
			subStr += c;
		}
		
		return commandExists(subStr);
	}
	
	private boolean argIsQuestion(String str)
	{
		String[] subStrs = str.split("-", 2);
		
		if (subStrs.length > 1)
		{
			if (subStrs[1].equals("?"))
				return true;
		}
		
		return false;
	}
	
	// This function will first attempt to simply move down a line.
	// Failing that the next attempt will be to scroll down.
	// If we can't scroll any further finally we will move the entire buffer and clip the top.
	private void nextLine()
	{
		cursorPos.x = 0;
		cursorPos.y++;
		
		if (cursorPos.y >= yScroll + messageBox.getBufferHeight())
		{
			scrollDown();
		}
		
		if (cursorPos.y >= conBufferHeight)
		{
			cursorPos.y--;
			
			// Shift the input line up
			
			startInputLine--;
			endInputLine--;
			
			// Move the buffer up 1 line
			for (int i = conBufferWidth; i < consoleBuffer.length; i++)
			{
				consoleBuffer[i - conBufferWidth] = consoleBuffer[i];
			}
			
			// Clear the last line
			for (int i = (conBufferWidth * (conBufferHeight - 1)); i < consoleBuffer.length; i++)
			{
				consoleBuffer[i] = null;
			}
		}
	}
	
	public void printString(String msg)
	{
		printString(msg, primaryColor);
	}
	
	public void printString(String msg, int color)
	{
		clearInputLine();
		
		for (int i = 0; i < msg.length(); i++)
		{
			char c = msg.charAt(i);
			
			if (c == '\n')
			{
				nextLine();
				continue;
			}
			
			printCharAtCursor(msg.charAt(i), color);
		}
		
		nextLine();
		
		printInputLine();
	}
	
	public void printCharSet()
	{
		clearInputLine();
		
		for (int i = 0; i < 256; i++)
		{
			if (i % 16 == 0 && i != 0)
			{
				nextLine();
			}
			
			printCharAtCursor((char) i, primaryColor);
		}
		
		nextLine();
		
		printInputLine();
	}
	
	private void clearInputLine()
	{
		for (int y = startInputLine; y <= endInputLine ; y++)
		{
			clearLine(y);
		}
		cursorPos.y = startInputLine;
	}
	
	private void printInputLine()
	{
		cursorPos.x = 0;
		startInputLine = cursorPos.y;
		
		printCharAtCursor(inputHeader, tertiaryColor);
		printCharAtCursor(' ', 0);
		
		for (int i = 0; i < input.length(); i++)
		{
			printCharAtCursor(input.charAt(i), secondaryColor);
		}
		
		endInputLine = cursorPos.y;
		
		printCursor();
	}
	
	private void printCursor()
	{
		consoleBuffer[cursorPos.y * messageBox.getBufferWidth() + cursorPos.x] = new MessageBox.Pair(cursor, tertiaryColor);
		bufferNeedsUpdate = true;
	}
	
	private void clearCursor()
	{
		consoleBuffer[cursorPos.y * messageBox.getBufferWidth() + cursorPos.x] = null;
		bufferNeedsUpdate = true;
	}
	
	private void clearLine(int y)
	{
		int yIndex = y * conBufferWidth;
		for (int i = 0; i < conBufferWidth; i++)
		{
			consoleBuffer[i + yIndex] = null;
		}
		
		cursorPos.x = 0;
	}
	
	public void clearConsole()
	{
		cursorPos.x = 0;
		cursorPos.y = 0;
		
		for (int i = 0; i < consoleBuffer.length; i++)
		{
			consoleBuffer[i] = null;
		}
		
		printInputLine();
		scrollToInput();
	}
	
	// Print functions	
	private void printCharAtCursor(char c, int color)
	{
		if (cursorPos.x >= conBufferWidth || cursorPos.y >= conBufferHeight)
			return;
		
		consoleBuffer[cursorPos.y * messageBox.getBufferWidth() + cursorPos.x] = new MessageBox.Pair(c, color);
		
		cursorPos.x++;
		
		if (cursorPos.x >= conBufferWidth)
		{
			nextLine();
		}
		
		bufferNeedsUpdate = true;
	}
	
	// Scroll functions
	public void scrollUp()
	{
		if (yScroll > 0)
			yScroll--;
		
		bufferNeedsUpdate = true;
	}
	
	public void scrollDown()
	{
		if (yScroll + messageBox.getBufferHeight() < conBufferHeight)
			yScroll++;

		bufferNeedsUpdate = true;
	}
	
	public void scrollToInput()
	{
		if (yScroll + messageBox.getBufferHeight() <= endInputLine)
		{
			while (yScroll + messageBox.getBufferHeight() <= endInputLine)
			{
				scrollDown();
			}
		}
		else
		{
			while (yScroll > startInputLine)
			{
				scrollUp();
			}
		}
	}	
	
	// Get functions
	private Pair[] getClippedBuffer()
	{
		Pair[] buf = new Pair[messageBox.getBufferSize()];
		int topIndex = yScroll * conBufferWidth;
		
		for (int i = 0; i < buf.length; i++)
		{
			if (topIndex + i < consoleBuffer.length)
				buf[i] = consoleBuffer[topIndex + i];
			else
				buf[i] = null;
		}
		
		return buf;
	}

	@Override
	public void handleEvent(Event event)
	{
		if (event.eventType.id == InputEvent.Key.typed.id)
		{
			InputEvent.Key.Data data = event.getData();
			
			if (data.e.getKeyChar() == 0x8)
				backspace();
			else if (data.e.getKeyChar() == 0xA)
				enter();
			else
				typedCharacter(data.e.getKeyChar());
			
			scrollToInput();
		}
		else if (event.eventType.id == InputEvent.Key.pressed.id)
		{
			InputEvent.Key.Data data = event.getData();
			
			if (data.e.getKeyCode() == KeyEvent.VK_UP)
				scrollUp();
			else if (data.e.getKeyCode() == KeyEvent.VK_DOWN)
				scrollDown();
		}
		else if (event.eventType.id == ConsoleMessage.log.id)
		{
			String str = ((ConsoleMessage.Data)event.getData()).message;
			printString(str);
		}
		else if (event.eventType.id == ConsoleMessage.warning.id)
		{
			String str = ((ConsoleMessage.Data)event.getData()).message;
			printString(str, warnColor);
		}
		else if (event.eventType.id == ConsoleMessage.error.id)
		{
			String str = ((ConsoleMessage.Data)event.getData()).message;
			printString(str, errorColor);
		}
	}

	@Override
	public String getName()
	{
		return name;
	}
}
