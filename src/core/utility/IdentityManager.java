package core.utility;

import java.util.HashMap;
import java.util.TreeSet;

// Allows for the creation of IDs and unique IDs.
//
// requestIdentity allows for the same hash by providing a unique extension
//
// requestUniqueIdentity demands that the supplied string/hash be the -only- instance.
//
public class IdentityManager
{
	// An identity is a hash and extension to allow for multiple of the same
	// hash while using the extension to differentiate.
	//
	public static class Identity
	{
		public final int hash, ext;
		
		public Identity(int hash, int ext)
		{
			this.hash = hash;
			this.ext = ext;
		}
		
		public boolean equals(Identity id)
		{
			return (id.hash == hash) && (id.ext == ext);
		}
	}
	
	private static final HashMap<Integer, Integer> ids = new HashMap<Integer, Integer>();
	private static final TreeSet<Integer> uids = new TreeSet<Integer>();
	
	private IdentityManager()
	{
		
	}
	
	public static Identity requestIdentity(String str)
	{
		return requestIdentity(str.toLowerCase().hashCode());
	}
	
	public static Identity requestIdentity(int hash)
	{
		return reqIdentImpl(hash);
	}
	
	private static Identity reqIdentImpl(int hash)
	{
		int ext = 0;
		
		Integer i = ids.get(hash);
		
		if (uids.contains(hash))
		{
			throw new RuntimeException("Hash (" + Integer.toHexString(hash) + ") already exists in the unique id table!");
		}
		
		if (i == null)
		{
			ids.put(hash, Integer.MIN_VALUE);
			ext = Integer.MIN_VALUE;
		}
		else
		{
			i += 1;
			ids.replace(hash, i);
			
			ext = i;
		}
		
		return new Identity(hash, ext);
	}
	
	public static Identity requestUniqueIdentity(String str)
	{
		return requestUniqueIdentity(str.toLowerCase().hashCode());
	}
	
	public static Identity requestUniqueIdentity(int hash)
	{
		return reqUIdentImpl(hash);
	}
	
	private static Identity reqUIdentImpl(int hash)
	{
		if (ids.containsKey(hash))
		{
			throw new RuntimeException("Hash (" + Integer.toHexString(hash) + ") already exists in the id table!");
		}
		else if (uids.contains(hash))
		{
			throw new RuntimeException("Hash (" + Integer.toHexString(hash) + ") already exists in the unique id table!");
		}
		
		uids.add(hash);
		
		return new Identity(hash, 0);
	}
}
