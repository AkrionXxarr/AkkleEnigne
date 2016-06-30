package game.engine;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import core.math.vector.Vector2f;
import core.math.vector.Vector3f;
import core.resource.graphics.Bitmap;
import core.utility.Converter;
import core.window.Display;

// Provides various draw methods.
public class RenderContext 
{
	private final Display display;
	private final float[] zBuffer;
	
	public RenderContext(Display display) 
	{
		this.display = display;
		zBuffer = new float[display.getScreen().getWidth() * display.getScreen().getHeight()];
	
		clearZBuffer();
	}
	
	public void draw(Bitmap bmp, int modulation, Vector3f v)
	{
		draw(bmp.getBuffer(), bmp.getWidth(), bmp.getHeight(), modulation, v);
	}
	
	public void draw(BufferedImage bi, int modulation, Vector3f v)
	{
		draw(((DataBufferInt)bi.getRaster().getDataBuffer()).getData(), bi.getWidth(), bi.getHeight(), modulation, v);
	}
	
	public void draw(
			int[] src, 
			int width, 
			int height, 
			int modulation,
			Vector3f v)
	{
		int[] dest = display.getScreenBuffer();
		
		// Adjust to "center of pixel"
		v.x += 0.5f;
		v.y += 0.5f;
		
		// Clip the draw rect to the screen buffer
		int left = 0;
		int top = 0;
		int ix = (int) v.x;
		int iy = (int) v.y;
		
		if (v.x < 0)
		{
			left = -(int)v.x;
		}
		if (v.y < 0)
		{
			top = -(int)v.y;
		}
		
		int right = Math.min(width, display.getScreen().getWidth() - ix);
		int bottom = Math.min(height, display.getScreen().getHeight() - iy);
		
		// The modulation variable is used to adjust the original color of the source being drawn.
		float rModf = ((modulation & 0x00FF0000) >>> 16) / 255.0f;
		float gModf = ((modulation & 0x0000FF00) >>> 8) / 255.0f;
		float bModf = (modulation & 0x000000FF) / 255.0f;
		
		for (int row = top; row < bottom; row++)
		{
			for (int col = left; col < right; col++)
			{
				int srcIndex = (row * width) + col;
				int destIndex = (((iy + row) * display.getScreen().getWidth()) + (ix + col));
				int zIndex = destIndex;
				
				// Source is 100% transparent
				if ((src[srcIndex] & 0xFF000000) == 0)
					continue;

				int srgb[] = Converter.IntToRGB(src[srcIndex]);
				
				srgb[0] *= rModf;
				srgb[1] *= gModf;
				srgb[2] *= bModf;
				
				if ((src[srcIndex] & 0xFF000000) == 0xFF000000)
				{
					// Source pixel is not closer than destination pixel
					if (v.z <= zBuffer[zIndex])
						continue;
					
					zBuffer[zIndex] = v.z;
					
					// No alpha blending needed
					dest[destIndex] = Converter.RGBToInt(srgb[0], srgb[1], srgb[2]);
				}
				else
				{
					if (v.z <= zBuffer[zIndex])
						continue;
					
					zBuffer[zIndex] = v.z;
					
					// Do alpha blending
					float alpha = src[srcIndex] / 255.0f;	
					
					int drgb[] = Converter.IntToRGB(dest[destIndex]);
					
					drgb[0] = (int) ((drgb[0] - srgb[0]) * alpha + srgb[0]);
					drgb[1] = (int) ((drgb[0] - srgb[0]) * alpha + srgb[1]);
					drgb[2] = (int) ((drgb[0] - srgb[0]) * alpha + srgb[2]);
					
					dest[destIndex] = Converter.RGBToInt(drgb[0], drgb[1],  drgb[2]);
				}
			}
		}
	}
	
	public void drawPoint(int color, float x, float y, float z)
	{
		if (x < 0 || y < 0 || x >= display.getScreen().getWidth() || y >= display.getScreen().getHeight())
			return;
		
		int index = ((int)y * display.getScreen().getWidth()) + (int)x;
		
		if (z > zBuffer[index])
		{
			zBuffer[index] = z;
			display.getScreenBuffer()[index] = color;
		}
	}
	
	public void drawLine(int color, Vector2f v1, Vector2f v2, float z)
	{
		drawPoint(color, v1.x, v1.y, z);
		drawPoint(color, v2.x, v2.y, z);
		
		boolean vertProjIsLonger = (Math.abs(v2.y - v1.y) > Math.abs(v2.x - v1.x));
		
		if (vertProjIsLonger)
		{
			float t = v1.x;
			v1.x = v1.y;
			v1.y = t;
			
			t = v2.x;
			v2.x = v2.y;
			v2.y = t;
		}
		
		if (v1.x > v2.x)
		{
			Vector2f t = v1;
			v1 = v2;
			v2 = t;
		}
		
		float dx = v2.x - v1.x;
		float dy = Math.abs(v2.y - v1.y);
		
		float err = dx / 2.0f;
		
		int yStep = (v1.y < v2.y) ? 1 : -1;
		int y = (int)v1.y;
		
		for (int x = (int)v1.x; x < (int)v2.x; x++)
		{
			if (vertProjIsLonger)
				drawPoint(color, y, x, z);
			else
				drawPoint(color, x, y, z);
			
			err -= dy;
			
			if (err < 0)
			{
				y += yStep;
				err += dx;
			}
		}
	}

	public void clearZBuffer()
	{
		for (int i = 0; i < display.getScreen().getWidth() * display.getScreen().getHeight(); i++)
			zBuffer[i] = -Float.MAX_VALUE;
	}
	
	public final Display getDisplay()
	{
		return display;
	}
}
