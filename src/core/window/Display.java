package core.window;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

// Handles everything to do with the actual window and screen.
//
public class Display extends Canvas
{
	private static final long serialVersionUID = 1L;
	
	private final JFrame frame;
	private final BufferedImage screen;
	private final int[] screenBuffer;
	private final BufferStrategy bufferStrategy;
	private final Graphics gfx;
	
	public Display(String title, int width, int height, int scale)
	{
		Dimension size = new Dimension(width * scale, height * scale);
		super.setPreferredSize(size);
		super.setMinimumSize(size);
		super.setMaximumSize(size);
		
		screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
		screenBuffer = ((DataBufferInt)screen.getRaster().getDataBuffer()).getData();
		
		frame = new JFrame(title);
		
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		super.createBufferStrategy(1);
		bufferStrategy = super.getBufferStrategy();
		gfx = bufferStrategy.getDrawGraphics();
	}
	
	// Copies the screen buffer to the window
	public void slam()
	{
		gfx.drawImage(screen, 0, 0, getWidth(), getHeight(), null);
		bufferStrategy.show();
	}
	
	public final JFrame getFrame()
	{
		return frame;
	}
	
	public final BufferedImage getScreen()
	{
		return screen;
	}
	
	public final int[] getScreenBuffer()
	{
		return screenBuffer;
	}
}
