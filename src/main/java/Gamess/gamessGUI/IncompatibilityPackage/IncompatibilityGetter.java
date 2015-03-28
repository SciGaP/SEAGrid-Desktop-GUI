package Gamess.gamessGUI.IncompatibilityPackage;

import java.util.Hashtable;

import Gamess.gamessGUI.IncompatibilityPackage.Restriction.RestrictionsHolder;

public class IncompatibilityGetter 
{
	private static Hashtable<String, RestrictionsHolder> incompatibilityCache = new Hashtable<String, RestrictionsHolder>();
	
	private static boolean hit(String Data)
	{
		if(incompatibilityCache.containsKey(Data))
			return true;
		return false;
	}
	
	public static RestrictionsHolder getIncompatibility(String Data)
	{
		//if the data is present in the cache then return it 
		if(hit(Data))
			return retrieve(Data);
		
		//It is a miss. 
		//Build a new set of restriction and add it to the cache for later use.
		RestrictionsHolder restriction = new RestrictionsHolder();
		restriction.buildRestriction(Data);
		
		addToCache(Data, restriction);
		
		//Return the restriction
		return restriction;
	}
	
	private static RestrictionsHolder retrieve(String Data)
	{
		if(!hit(Data))
			return null;
		return incompatibilityCache.get(Data);
	}
	
	private static void addToCache(String Data, RestrictionsHolder restriction)
	{
		incompatibilityCache.put(Data, restriction);
	}
}
