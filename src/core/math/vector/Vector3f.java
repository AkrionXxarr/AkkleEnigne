package core.math.vector;

// 3 dimensional floating point vector
//
public class Vector3f 
{
	public float x, y, z;
	
	public Vector3f()
	{
		x = 0;
		y = 0;
		z = 0;
	}
	
	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3f(Vector2f v)
	{
		x = v.x;
		y = v.y;
		z = 0;
	}
	
	public Vector3f(Vector2f v, float z)
	{
		x = v.x;
		y = v.y;
		this.z = z;
	}
	
	public Vector3f(Vector3f v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
	}
	
	@Override
	public String toString()
	{
		return ("(" + x + ", " + y + ", " + z + " )");
	}
	
	public float magnitude()
	{
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	public float squaredMagnitude()
	{
		return x * x + y * y + z * z;
	}
	
	public Vector3f normalized()
	{
		float m = magnitude();
		
		if (m == 0)
			return new Vector3f(0, 0, 0);
		
		return new Vector3f(x / m, y / m, z / m);
	}
	
	public float dot(Vector3f v)
	{
		return (x * v.x) + (y * v.y) + (z * v.z);
	}
	
	public Vector3f cross(Vector3f v)
	{
		// (AyBz - AzBy, AzBx - AxBz, AxBy - AyBx);
		// (a2b3 - a3b2, a3b1 - a1b3, a1b2 - a2b1); 
		float i = y * v.z - z * v.y;
		float j = z * v.x - x * v.z;
		float k = x * v.y - y * v.x;
		
		return new Vector3f(i, j, k);
	}
	
	public Vector3f lerp(Vector3f dest, float factor)
	{
		Vector3f v;
		
		v = dest.sub(this).mul(factor).add(this);
		
		return v;
	}
	
	public Vector3f add(Vector2f v)
	{
		return new Vector3f(x + v.x, y + v.y, z);
	}
	
	public Vector3f add(Vector3f v)
	{
		return new Vector3f(x + v.x, y + v.y, z + v.z);
	}
	
	public Vector3f sub(Vector2f v)
	{
		return new Vector3f(x - v.x, y - v.y, z);
	}
	
	public Vector3f sub(Vector3f v)
	{
		return new Vector3f(x - v.x, y - v.y, z - v.z);
	}
	
	public Vector3f mul(float f)
	{
		return new Vector3f(x * f, y * f, z * f);
	}
	
	public Vector3f div(float f)
	{
		return new Vector3f(x / f, y / f, z / f);
	}
}

