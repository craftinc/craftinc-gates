package de.craftinc.gates.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;

import de.craftinc.gates.Plugin;


/**
 * NOTE: We do not care about yaw and pitch for gate locations. So we won't serialize them.
 */
public class LocationSerializer 
{
	protected static String worldKey = "world";
	protected static String xKey = "x";
	protected static String yKey = "y";
	protected static String zKey = "z";

	
	protected static World getWorld(String name) throws Exception 
	{
		World world = Plugin.instance.getServer().getWorld(name);

		if (world == null) {
			throw new Exception("World '" + name + "' does not exists anymore! Cannot get instance!");
		}
		
		return world;
	}
	
	
	public static Map<String, Object> serializeLocation(Location l)
	{
		Map<String, Object> serializedLocation = new HashMap<String, Object>();
		
		serializedLocation.put(worldKey, l.getWorld().getName());
		serializedLocation.put(xKey, l.getX());
		serializedLocation.put(yKey, l.getY());
		serializedLocation.put(zKey, l.getZ());
		
		return serializedLocation;
	}
	

	public static Location deserializeLocation(Map<String, Object> map) throws Exception
	{
		World w = getWorld((String)map.get(worldKey));
		
		
		// verbose loading of coordinates (they might be Double or Integer)
		Object objX =  map.get(xKey);
		Object objY =  map.get(yKey);
		Object objZ =  map.get(zKey);
		
		double x,y,z;
		
		if (objX instanceof Integer)
			x = (double)(Integer)objX;
		else
			x = (Double)objX;
		
		if (objY instanceof Integer)
			y = (double)(Integer)objY;
		else
			y = (Double)objY;
		
		if (objZ instanceof Integer)
			z = (double)(Integer)objZ;
		else
			z = (Double)objZ;
		
		
		return new Location(w, x, y, z);
	}
}
