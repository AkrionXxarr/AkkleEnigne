package core.math.vector;

// 4 dimensional floating point vector
//
public class Vector4f 
{
	public float x, y, z, w;
	
	public Vector4f()
	{
		x = 0;
		y = 0;
		z = 0;
		w = 0;
	}
	
	public Vector4f(float x, float y, float z, float w)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4f(Vector3f v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
		w = 0;
	}
	
	public Vector4f(Vector3f v, float w)
	{
		x = v.x;
		y = v.y;
		z = v.z;
		this.w = w;
	}
	
	public Vector4f(Vector4f v)
	{
		x = v.x;
		y = v.y;
		z = v.z;
		w = v.w;
	}
	
	public float magnitude()
	{
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public float squaredMagnitude()
	{
		return x * x + y * y + z * z + w * w;
	}
	
	public Vector4f normalized()
	{
		float m = magnitude();
		
		if (m == 0)
			return new Vector4f(0, 0, 0, 0);
		
		return new Vector4f(x / m, y / m, z / m, w / m);
	}
	
	public float dot(Vector4f v)
	{
		return (x * v.x) + (y * v.y) + (z * v.z) + (w * v.w);
	}
	
	public Vector4f add(Vector4f v)
	{
		return new Vector4f(x + v.x, y + v.y, z + v.z, w + v.w);
	}
	
	public Vector4f sub(Vector4f v)
	{
		return new Vector4f(x - v.x, y - v.y, z - v.z, w - v.w);
	}
	
	public Vector4f mul(float f)
	{
		return new Vector4f(x * f, y * f, z * f, w * f);
	}
	
	public Vector4f div(float f)
	{
		return new Vector4f(x / f, y / f, z / f, w / f);
	}
}
