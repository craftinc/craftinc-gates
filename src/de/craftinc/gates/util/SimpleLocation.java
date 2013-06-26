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


    public SimpleLocation(Location l, boolean isHeadPosition)
    {
        this.world = l.getWorld().getName();

        // Using Block coordinates makes it possible to compare block locations with player locations.
        // There might be an offset of 1 otherwise.
        this.x = l.getBlockX();
        this.y = l.getBlockY();
        this.z = l.getBlockZ();

        if (isHeadPosition) {
            this.y--;
        }
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
