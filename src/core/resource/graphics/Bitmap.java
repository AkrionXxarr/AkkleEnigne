package core.resource.graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import core.resource.ResourceManager;

// TODO Think of a better way to interface Bitmap with the resource manager
// Currently only bitmaps loaded from files are managed

// Handles the loading and manipulating of bitmaps.
//
public class Bitmap
{	
	private final int id;
	
	private final BufferedImage bufferedImage;
	private final int[] buffer;
	private final byte[] alphaStore;
	
	private Graphics gfx;
	
	private int colorMask = 0;
	private boolean usingColorMask = false;
	
	private final int width, height, halfWidth, halfHeight, imgSize;
	
	public Bitmap(int width, int height)
	{
		this.id = this.hashCode();
		
		this.width = width;
		this.height = height;
		halfWidth = width / 2;
		halfHeight = height / 2;
		imgSize = width * height;
		
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		buffer = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
		gfx = bufferedImage.createGraphics();
		
		alphaStore = new byte[imgSize];
		
		for (int i = 0; i < imgSize; i++)
		{
			buffer[i] = 0;
			alphaStore[i] = 0;
		}
	}
	
	public Bitmap(String filePath, String fileName)
	{	
		BufferedImage t = null;
		
		try {
			if (!filePath.endsWith("/"))
				filePath += "/";
			
			t = ImageIO.read(new File(filePath + fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.id = (filePath + fileName).hashCode();
		
		this.width = t.getWidth();
		this.height = t.getHeight();
		halfWidth = width / 2;
		halfHeight = height / 2;
		imgSize = width * height;
		
		ResourceManager.ResourceData resData = ResourceManager.getResource(Bitmap.class, id);
		
		if (resData.resource == null)
		{
			bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			resData.resource = bufferedImage;
		}
		else
		{
			bufferedImage = (BufferedImage)resData.resource;
		}
		
		gfx = bufferedImage.createGraphics();
		gfx.drawImage(t, 0, 0, null);
		
		buffer = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
		alphaStore = new byte[imgSize];
		
		for (int i = 0; i < imgSize; i++)
		{
			alphaStore[i] = (byte) ((buffer[i] & 0xFF000000) >>> 24);
		}
	}
	
	public Bitmap(BufferedImage bi)
	{
		this.id = this.hashCode();
		
		this.width = bi.getWidth();
		this.height = bi.getHeight();
		halfWidth = width / 2;
		halfHeight = height / 2;
		imgSize = width * height;
		
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		gfx = bufferedImage.createGraphics();
		gfx.drawImage(bi,  0,  0,  null);
		
		buffer = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
		alphaStore = new byte[imgSize];
		
		for (int i = 0; i < imgSize; i++)
		{
			alphaStore[i] = (byte) ((buffer[i] & 0xFF000000) >>> 24);
		}
	}
	
	public Bitmap(int[] buffer, int width, int height)
	{
		this.id = this.hashCode();
		
		this.width = width;
		this.height = height;
		halfWidth = width / 2;
		halfHeight = height / 2;
		imgSize = width * height;
		bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		this.buffer = ((DataBufferInt)bufferedImage.getRaster().getDataBuffer()).getData();
		alphaStore = new byte[imgSize];
	
		for (int i = 0; i < imgSize; i++)
		{
			this.buffer[i] = buffer[i];
		}
		for (int i = 0; i < imgSize; i++)
		{
			alphaStore[i] = (byte) ((buffer[i] & 0xFF000000) >>> 24);
		}
	}
	
	@Override
	public void finalize()
	{
		ResourceManager.releaseResource(Bitmap.class, id);
	}
	
	public void copy(int[] dest)
	{
		if (dest.length != imgSize)
			throw new RuntimeException(this.toString() + " | dest and buffer are not the same size");
		
		for (int i = 0; i < imgSize; i++)
		{
			dest[i] = buffer[i];
		}
	}
	
	public void draw(Bitmap bitmap, int x, int y)
	{
		draw(bitmap.getBufferedImage(), x,  y);
	}
	
	public void draw(BufferedImage bi, int x, int y)
	{
		gfx.drawImage(bi, x, y, null);
	}
	
	public void drawScaled(Bitmap bitmap, int x, int y, float xScale, float yScale)
	{
		drawScaled(bitmap.getBufferedImage(), x, y, xScale, yScale);
	}
	
	public void drawScaled(BufferedImage bi, int x, int y, float xScale, float yScale)
	{
		Image img = bi.getScaledInstance(
				(int)(bi.getWidth() * xScale), 
				(int)(bi.getHeight() * yScale), 
				BufferedImage.SCALE_DEFAULT
				);
		
		gfx.drawImage(img, x, y, null);
	}
	
	public void drawPixel(int argb, int x, int y)
	{
		if (inBounds(x, y))
		{
			int i = (y * width) + x;
			
			buffer[i] = argb;
		}
	}
	
	public boolean inBounds(int x, int y)
	{
		return !(x < 0 || y < 0 || x >= width || y >= height);
	}
	
	public Bitmap getFlippedHorizontal()
	{
		int[] t = new int[imgSize];
		
		for (int row = 0; row < height; row++)
		{
			int rowIndex = row * width;
			for (int col = height - 1; col >= 0; col--)
			{
				int tIndex = rowIndex + col;
				int bIndex = rowIndex + (width - col - 1);
				
				t[tIndex] = buffer[bIndex];
			}
		}
		
		return new Bitmap(t, width, height);
	}
	
	public Bitmap getFlippedVertical()
	{
		int[] t = new int[imgSize];
		
		for (int row = height - 1; row >= 0; row--)
		{
			int rowIndexA = row * width;
			int rowIndexB = (height - row - 1) * width;
			for (int col = 0; col < width; col++)
			{
				int tIndex = (rowIndexA + col);
				int bIndex = (rowIndexB + col);
				
				t[tIndex] = buffer[bIndex];
			}
		}
		
		return new Bitmap(t, width, height);
	}
	
	public final BufferedImage getBufferedImage()
	{
		return bufferedImage;
	}
	
	// Use the alpha to mimic a color mask
	public void useColorMask(int rgb)
	{
		if (usingColorMask)
			return;
		
		usingColorMask = true;
		colorMask = rgb;
		
		for (int i = 0; i < imgSize; i++)
		{
			if ((buffer[i] & 0x00FFFFFF) == (0x00FFFFFF & colorMask))
			{
				buffer[i] &= 0x00FFFFFF;
			}
			else
			{
				buffer[i] |= (alphaStore[i] << 24);
			}
		}
	}
	
	public void restoreAlphaMask()
	{
		if (!usingColorMask)
			return;
	
		usingColorMask = false;
		
		for (int i = 0; i < imgSize; i++)
		{
			buffer[i] |= alphaStore[i] << 24;
		}
	}
	
	public final int getWidth()
	{
		return width;
	}
	
	public final int getHalfWidth()
	{
		return halfWidth;
	}
	
	public final int getHeight()
	{
		return height;
	}
	
	public final int getHalfHeight()
	{
		return halfHeight;
	}
	
	public final int getImageSize()
	{
		return imgSize;
	}
	
	public final int[] getBuffer()
	{
		return buffer;
	}
	
	public final int getImageType()
	{
		return bufferedImage.getType();
	}
	
	public final int getColorMask()
	{
		return colorMask;
	}
}
