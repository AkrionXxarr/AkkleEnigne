package game.component.graphics;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import core.math.vector.Vector3f;
import core.utility.Converter;
import game.component.base.GameComponent;
import game.engine.RenderContext;

public class BasicBackground extends GameComponent
{
	private final int color;
	private final BufferedImage bi;
	private final int[] buffer;
	private final float depth;
	private final Rectangle rect;

	public BasicBackground(String name, int x, int y, int width, int height, float depth, int r, int g, int b)
	{
		super(name);
		
		color = Converter.ARGBToInt(255, r, g, b);
		
		bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		buffer = ((DataBufferInt)bi.getRaster().getDataBuffer()).getData();
		
		this.depth = depth;
		this.rect = new Rectangle(x, y, width, height);
		
		for (int row = 0; row < height; row++)
		{
			for (int col = 0; col < width; col++)
			{
				buffer[row * width + col] = color;
			}
		}
	}

	@Override
	public void onAdd()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRemove()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float deltaTime)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(RenderContext renderContext)
	{
		renderContext.draw(buffer, bi.getWidth(), bi.getHeight(), Converter.RGBToInt(255, 255, 255), new Vector3f(rect.x, rect.y, depth));
	}

}
