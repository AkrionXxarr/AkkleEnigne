package core.resource.graphics;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

// Allows for a bitmap to be divided into a bunch of smaller tiles which can
// then be retrieved with an X,Y position.
//
public class TileSet 
{
	private final Bitmap bitmap;
	
	private final int xTileSize, yTileSize;
	private final int xOffset, yOffset;
	private final int columns, rows;
	
	private final BufferedImage[] tiles;
	
	public TileSet(
			String filePath,
			String fileName, 
			int xTileSize, 
			int yTileSize, 
			int xOffset, 
			int yOffset) 
	{	
		this.xTileSize = xTileSize;
		this.yTileSize = yTileSize;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		bitmap = new Bitmap(filePath, fileName);
		
		columns = (bitmap.getWidth() - xOffset) / (xTileSize + xOffset);
		rows =  (bitmap.getHeight() - yOffset) / (yTileSize + yOffset);
		
		tiles = new BufferedImage[columns * rows];
		
		loadTiles();
	}
	
	public TileSet(Bitmap bitmap, int xTileSize, int yTileSize, int xOffset, int yOffset)
	{
		this.xTileSize = xTileSize;
		this.yTileSize = yTileSize;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		this.bitmap = bitmap;
		
		columns = (bitmap.getWidth() - xOffset) / (xTileSize + xOffset);
		rows =  (bitmap.getHeight() - yOffset) / (yTileSize + yOffset);
		
		tiles = new BufferedImage[columns * rows];
		
		loadTiles();
	}
	
	private void loadTiles()
	{
		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col < columns; col++)
			{
				Rectangle rect = new Rectangle();
				
				rect.x = (col * xTileSize) + (col * xOffset) + xOffset;
				rect.y = (row * yTileSize) + (row * yOffset) + yOffset;
				rect.width = xTileSize;
				rect.height = yTileSize;
				
				BufferedImage bi = new BufferedImage(xTileSize, yTileSize, BufferedImage.TYPE_INT_ARGB);
				Graphics graphics = bi.createGraphics();
				
				graphics.drawImage(
						bitmap.getBufferedImage(), 
						0, 
						0, 
						xTileSize, 
						yTileSize, 
						rect.x, 
						rect.y, 
						rect.x + rect.width, 
						rect.y + rect.height, 
						null
						);
				
				
				graphics.dispose();
				
				tiles[row * columns + col] = bi;
			}
		}
	}
	
	public BufferedImage getTile(int x, int y)
	{
		BufferedImage bi = null;
		
		if (x < columns && x >= 0 && y < rows && y >= 0)
		{
			bi = tiles[y * columns + x];
		}
		
		return bi;
	}
}

