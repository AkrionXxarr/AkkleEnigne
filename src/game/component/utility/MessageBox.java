package game.component.utility;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import core.math.vector.Vector3f;
import game.component.base.GameComponent;
import game.engine.RenderContext;

public class MessageBox extends GameComponent
{
	public static class Pair
	{
		private char c;
		private int color;
		
		public Pair()
		{
			
		}
		
		public Pair(char c, int color)
		{
			this.c = c;
			this.color = color;
		}
		
		public void setChar(char c)
		{
			this.c = c;
		}
		
		public void setColor(int color)
		{
			this.color = color;
		}
		
		public char getChar()
		{
			return c;
		}
		
		public int getColor()
		{
			return color;
		}
	}
	
	public enum FONT_TYPE
	{
		FONT_8x8,
		FONT_16x16
	}
	
	// Message box specific details
	private Vector3f boxPos;
	private int xStep, yStep; // Used for adding additional print steps
	
	// Message details
	private int defaultColor;
	private final FONT_TYPE fontType;
	private Pair[] charBuffer;

	private final Rectangle bufferRect;
	private final int bufferSize;

	public MessageBox(
			String name, 
			Vector3f boxPos, 
			int width, 
			int height, 
			int xStep,
			int yStep,
			int color, 
			FONT_TYPE fontType)
	{
		super(name);
		
		this.boxPos = boxPos;
		this.xStep = xStep;
		this.yStep = yStep;
		
		defaultColor = color;
		this.fontType = fontType;	

		if (fontType == FONT_TYPE.FONT_8x8)
		{
			this.xStep += 8;
			this.yStep += 8;
		}
		else if (fontType == FONT_TYPE.FONT_16x16)
		{
			this.xStep += 16;
			this.yStep += 16;
		}
		
		bufferRect = new Rectangle();
		
		bufferRect.x = 0;
		bufferRect.y = 0;
		bufferRect.width = (width / this.xStep);
		bufferRect.height = (height / this.yStep);
		
		bufferSize = bufferRect.width * bufferRect.height;
		
		charBuffer = new Pair[bufferSize];
	}

	@Override
	public void onAdd() { }

	@Override
	public void onRemove() { }

	@Override
	public void start() { }

	@Override
	public void stop() { }

	@Override
	public void update(float deltaTime) { }

	@Override
	public void draw(RenderContext renderContext)
	{
		BufferedImage bi = null;
		
		for (int row = bufferRect.y; row < bufferRect.height; row++)
		{
			int rowIndex = row * bufferRect.width;
			float y = row * yStep + boxPos.y;
			
			for (int col = bufferRect.x; col < bufferRect.width; col++)
			{
				int index = rowIndex + col;
				float x = col * xStep + boxPos.x;
				
				Pair pair = charBuffer[index];
				
				if (pair == null)
				{
					pair = new Pair(' ', 0);
				}
				
				if (fontType == FONT_TYPE.FONT_8x8)
					bi = game.charSet.get8x8Char(pair.getChar());	
				else
					bi = game.charSet.get16x16Char(pair.getChar());
				
				if (bi == null)
					continue;			
				
				renderContext.draw(
						bi, 
						pair.getColor(), 
						new Vector3f(x, y, boxPos.z)
						);
			}
		}
	}
	
	public Pair[] parseAndFill(String msg)
	{
		ArrayList<Pair> pairList = new ArrayList<Pair>();		
		int curColor = defaultColor;
		
		for (int i = 0; i < msg.length(); i++)
		{
			if (isTag(msg, i))
			{
				i += 2;
				String tag = "";
				
				if (i < msg.length())
				{	
					while (msg.charAt(i) != '>' && (i + 1 < msg.length()))
					{
						tag += msg.charAt(i);
						i++;
					}
				}
				
				if (tag.toLowerCase().equals("d"))
					curColor = defaultColor;
				else if (tag.length() > 0 && tag.length() < 9)
					curColor = (int) (Long.parseLong(tag, 16));
				else
					throw new RuntimeException(this.toString() + "| Invalid tag: " + tag);
				
				continue;
			}
			
			pairList.add(new Pair(msg.charAt(i), curColor));
		}
		
		return pairList.toArray(new Pair[pairList.size()]);
	}
	
	boolean isTag(String msg, int i)
	{
		if (i + 1 >= msg.length())
			return false;
		
		if (msg.charAt(i) != '$')
			return false;
		
		if (msg.charAt(i + 1) != '<')
			return false;
		
		return true;
	}
	
	public void clearMessage()
	{
		for (int i = 0; i < charBuffer.length; i++)
		{
			charBuffer[i] = null;
		}
	}
	
	public void setMessage(String msg)
	{		
		clearMessage();
		Pair[] pairs = parseAndFill(msg);
		
		Point head = new Point(bufferRect.x, bufferRect.y);
		
		for (int i = 0; i < pairs.length; i++)
		{
			Pair pair = pairs[i];
			
			if (head.x >= bufferRect.width || pair.getChar() == '\n')
			{
				head.x = bufferRect.x;
				head.y++;
				
				if (head.y >= bufferRect.height)
				{
					break;
				}
				continue;
			}
			
			charBuffer[head.y * bufferRect.width + head.x] = pair;
			
			head.x++;
		}
	}
	
	public void setBuffer(Pair[] src)
	{
		if (src.length < charBuffer.length)
			throw new RuntimeException(this.toString() + " | src buffer not la. (src: " + src.length + ") (this: " + charBuffer.length + ")");
		
		for (int i = 0; i < charBuffer.length; i++)
		{
			if (src[i] != null)
				charBuffer[i] = new Pair(src[i].getChar(), src[i].getColor());
			else
				charBuffer[i] = null;
		}
	}
	
	public final Pair[] getBuffer()
	{
		return charBuffer;
	}
	
	public final int getBufferWidth()
	{
		return bufferRect.width;
	}
	
	public final int getBufferHeight()
	{
		return bufferRect.height;
	}
	
	public final int getBufferSize()
	{
		return bufferSize;
	}
	
	public final Vector3f getBoxPos()
	{
		return boxPos;
	}
	
	public final int getXStep()
	{
		return xStep;
	}
	
	public final int getYStep()
	{
		return yStep;
	}
}
