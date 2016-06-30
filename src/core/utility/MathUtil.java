package core.utility;

// Math utility functions
//
public class MathUtil
{
	public static float clamp(float min, float max, float val)
	{
		float t = val;
		
		if (val < min)
			t = min;
		else if (val > max)
			t = max;
		
		return t;
	}
}
