package core.utility;


// Provides various conversions.
//
public class Converter 
{	
	public static int RGBToInt(int r, int g, int b)
	{
		return ARGBToInt(255, r, g, b);
	}
	
	public static int ARGBToInt(int a, int r, int g, int b)
	{
		return (a << 24) | (r << 16) | (g << 8) | b;
	}
	
	public static int[] IntToRGB(int rgb)
	{
		int[] t = new int[3];
		
		t[0] = (rgb & 0x00FF0000) >>> 16;
		t[1] = (rgb & 0x0000FF00) >>> 8;
		t[2] = (rgb & 0x000000FF);
		
		return t;
	}
	
	public static int[] IntToARGB(int argb)
	{
		int[] t = new int[4];
		
		t[0] = (argb & 0xFF000000) >>> 24;
		t[1] = (argb & 0x00FF0000) >>> 16;
		t[2] = (argb & 0x0000FF00) >>> 8;
		t[3] = (argb & 0x000000FF);
		
		return t;
	}
}
