package de.craftinc.gates.util;

import org.bukkit.Location;

public class SimpleLocation 
{
	private String world;
    private int x;
    private int y;
    private int z;
    
    
    public SimpleLocation(Location l)
    {
    	this.world = l.getWorld().getName();

        // Using Block coordinates makes it possible to compare block locations with player locations.
        // There might be an offset of 1 otherwise.
    	this.x = l.getBlockX();
    	this.y = l.getBlockY();
    	this.z = l.getBlockZ();
    }
    
    
    @Override
    public boolean equals(final Object o)
    {
    	if (o instanceof SimpleLocation) {
    		SimpleLocation otherLocation = (SimpleLocation)o;
    		
    		if (otherLocation.x == this.x 
    			&& otherLocation.y == this.y 
    			&& otherLocation.z == this.z 
    			&& otherLocation.world.equals(this.world)) {
    			
    			return true;
    		}
    	}
    	
    	return false;
    }

    
    @Override
    public int hashCode()
    {
    	int hash = 13;
    	hash = 37 * hash + x;
    	hash = 31 * hash + y;
    	hash = 37 * hash + z;
    	hash = 31 * hash + world.hashCode();
    	
    	return hash;
    }
}
