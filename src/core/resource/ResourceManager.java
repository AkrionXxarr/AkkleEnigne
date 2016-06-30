package core.resource;

import java.util.HashMap;

// Uses a nested hashmap to manage resources by class type and object instance.
//
// This resource manager uses the class type to group like-resource types together.
// When a resource is no longer held by anything, it is removed.
// 
// Example resource table:
// Bitmap
// --8x8 character set
// --16x16 character set
// --Background
// AudioClip
// --explosion
// --level up
// 
public class ResourceManager
{
	public static class ResourceData
	{
		public int count;
		public Object resource;
	}
	
	private final static HashMap<Integer, HashMap<Integer, ResourceData>> resourceMap = new HashMap<Integer, HashMap<Integer, ResourceData>>();
	
	private ResourceManager()
	{
	}
	
	// Returns an existing resource if one exists,
	// returns a new resource otherwise.
	//
	public static <T> ResourceData getResource(Class<T> c, int id)
	{
		HashMap<Integer, ResourceData> a = resourceMap.get(c.hashCode());
		ResourceData data = null;
		
		if (a == null)
		{
			// Class type is new
			
			HashMap<Integer, ResourceData> b = new HashMap<Integer, ResourceData>();
			data = new ResourceData();
			
			data.resource = null;
			data.count = 1;
			
			b.put(id, data);
			
			resourceMap.put(c.hashCode(), b);
		}
		else
		{
			// Class type exists
			
			data = a.get(id);
			
			if (data == null)
			{
				// Object instance is new
				
				data = new ResourceData();
				
				data.resource = null;
				data.count = 1;
				
				a.put(id, data);
			}
			else
			{
				// Object instance exists
				
				data.count++;
			}
		}
		
		return data;
	}
	
	public static <T> int releaseResource(Class<T> c, int id)
	{
		HashMap<Integer, ResourceData> a = resourceMap.get(c.hashCode());
		ResourceData data = null;
		
		if (a != null)
		{		
			data = a.get(id);
			if (data != null)
			{
				data.count--;
				
				if (data.count <= 0)
				{
					// Resource is no longer being referenced, remove it.
					a.remove(id);
				}
			}
			
			return data.count;
		}
		return -1;
	}
}
