package core.resource.graphics;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import core.utility.Converter;

// Defines an 8x8 and 16x16 character set laid out in a 16x16 grid.
public class CharacterSet
{	
	HashMap<Integer, BufferedImage> charMap8;
	HashMap<Integer, BufferedImage> charMap16;
	
	public CharacterSet (String char8x8Path, String char8x8Name, String char16x16Path, String char16x16Name)
	{		
		Bitmap bmp1 = new Bitmap(char8x8Path, char8x8Name);
		Bitmap bmp2 = new Bitmap(char16x16Path, char16x16Name);
		
		bmp1.useColorMask(Converter.RGBToInt(255, 0, 255));
		bmp2.useColorMask(Converter.RGBToInt(255, 0, 255));
		
		TileSet char8x8 = new TileSet(bmp1, 8, 8, 1, 1);
		TileSet char16x16 = new TileSet(bmp2, 16, 16, 1, 1);
		
		charMap8 = new HashMap<Integer, BufferedImage>();
		charMap16 = new HashMap<Integer, BufferedImage>();
		
		for (int i = 0; i < 256; i++)
		{
			int y = i / 16;
			int x = i % 16;
			
			charMap8.put(i, char8x8.getTile(x, y));
			charMap16.put(i,  char16x16.getTile(x, y));
		}
	}
	
	public BufferedImage get8x8Char(int code)
	{
		BufferedImage bi = charMap8.get(code);
		
		if (bi == null)
			return null;
		
		return bi;
	}
	
	public BufferedImage get16x16Char(int code)
	{
		BufferedImage bi = charMap16.get(code);
		
		if (bi == null)
			return null;
		
		return bi;
	}
}
