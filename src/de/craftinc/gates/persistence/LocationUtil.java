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
package de.craftinc.gates.persistence;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.World;

import de.craftinc.gates.Plugin;


public class LocationUtil 
{
	protected final static String worldKey = "world";
	protected final static String xKey = "x";
	protected final static String yKey = "y";
	protected final static String zKey = "z";

	
	protected static World getWorld(final String name) throws Exception
	{
		if (name == null) {
            throw new IllegalArgumentException("The name of the world must not be 'null");
        }

        World world = Plugin.getPlugin().getServer().getWorld(name);

		if (world == null) {
			throw new Exception("World '" + name + "' does not exists anymore! Cannot get instance!");
		}
		
		return world;
	}


    /**
     * Serializes a location. Helps storing locations inside yaml files. NOTE: We do not care about yaw
     * and pitch for gate locations. So we won't serialize them.
     *
     * @param l The location to serialize. Supplying 'null' is ok..
     * @return A Map object ready for storing inside a yaml file. Will return 'null' if 'l' is null.
     */
	public static Map<String, Object> serializeLocation(final Location l)
	{
		if (l == null) {
			return null;
		}
		
		Map<String, Object> serializedLocation = new HashMap<String, Object>();
		
		serializedLocation.put(worldKey, l.getWorld().getName());
		serializedLocation.put(xKey, l.getX());
		serializedLocation.put(yKey, l.getY());
		serializedLocation.put(zKey, l.getZ());
		
		return serializedLocation;
	}


    /**
     *
     * @param map A map generated with the 'serializeLocation' method. Supplying 'null' is ok.
     * @return A deserialized location. This method will return 'null' if 'map' is null!
     * @throws Exception This method will throw an exception if the world of the supplied serialized location
     *         does not exist or if 'map' does not contain all necessary keys!
     */
	public static Location deserializeLocation(final Map<String, Object> map) throws Exception
	{
		if (map == null) {
			return null;
		}
		
		World w = getWorld((String)map.get(worldKey));

		Number x =  (Number)map.get(xKey);
        Number y =  (Number)map.get(yKey);
        Number z =  (Number)map.get(zKey);

        if (x == null || y == null || z == null) {
            throw new IllegalArgumentException("Supplied map is invalid x, y or z coordinate was not supplied");
        }
		
		return new Location(w, x.doubleValue(), y.doubleValue(), z.doubleValue());
	}
}
