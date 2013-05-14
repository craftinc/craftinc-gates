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
    	this.x = (int)l.getX();
    	this.y = (int)l.getY();
    	this.z = (int)l.getZ();
    }
    
    
    @Override
    public boolean equals(Object o)
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
