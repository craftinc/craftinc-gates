/*  Craft Inc. Gates
    Copyright (C) 2011-2013 Craft Inc. Gates Team (see AUTHORS.txt)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this program (LGPLv3).  If not, see <http://www.gnu.org/licenses/>.
*/
package de.craftinc.gates.util;

import org.bukkit.Chunk;
import org.bukkit.World;

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


    public SimpleChunk(int x, int z, World w)
    {
        this.x = x;
        this.z = z;
        this.world = w.getName();
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
