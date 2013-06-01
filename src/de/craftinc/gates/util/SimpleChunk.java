package de.craftinc.gates.util;

import org.bukkit.Chunk;

public class SimpleChunk 
{
	private int x;
	private int z;
	private String world;
	
	public SimpleChunk(Chunk c)
	{
		this.x = c.getX();
		this.z = c.getZ();
		this.world = c.getWorld().getName();
	}
	
	
	@Override
    public boolean equals(Object o)
    {
    	if (o instanceof SimpleChunk) {
    		SimpleChunk otherLocation = (SimpleChunk)o;
    		
    		if (otherLocation.x == this.x 
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
    	int hash = 11;
    	hash = 29 * hash + x;
    	hash = 37 * hash + z;
    	hash = 29 * hash + world.hashCode();
    	
    	return hash;
    }


    @Override
    public String toString()
    {
        return this.getClass().toString() + " (x=" + this.x + " z=" + this.z + " world='" + this.world + "')";
    }
}
