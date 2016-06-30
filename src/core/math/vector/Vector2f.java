package core.math.vector;

// 2 dimensional floating point vector
//
public class Vector2f 
{
	public float x, y;
	
	public Vector2f()
	{
		x = 0;
		y = 0;
	}
	
	public Vector2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2f(Vector2f v)
	{
		x = v.x;
		y = v.y;
	}
	
	public Vector2f(Vector3f v)
	{
		x = v.x;
		y = v.y;
	}
	
	@Override
	public String toString()
	{
		return ("(" + x + ", " + y + " )");
	}
	
	public float magnitude()
	{
		return (float)Math.sqrt(x * x + y * y);
	}
	
	public float squaredMagnitude()
	{
		return x * x + y * y;
	}
	
	public Vector2f normalized()
	{
		float m = magnitude();
		
		if (m == 0)
			return new Vector2f(0, 0);
		
		return new Vector2f(x / m, y / m);
	}
	
	public float dot(Vector2f v)
	{
		return (x * v.x) + (y * v.y);
	}
	
	public float cross(Vector2f v)
	{
		return (x * v.y) - (y * v.x);
	}
	
	public Vector2f lerp(Vector2f dest, float factor)
	{
		Vector2f v;
		
		v = dest.sub(this).mul(factor).add(this);
		
		return v;
	}
	
	public Vector2f add(Vector2f v)
	{
		return new Vector2f(x + v.x, y + v.y);
	}
	
	public Vector2f sub(Vector2f v)
	{
		return new Vector2f(x - v.x, y - v.y);
	}
	
	public Vector2f mul(float f)
	{
		return new Vector2f(x * f, y * f);
	}
	
	public Vector2f div(float f)
	{
		return new Vector2f(x / f, y / f);
	}
}

