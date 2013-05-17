package de.craftinc.gates.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

import de.craftinc.gates.Gate;

public class GateUtil 
{
	public static Gate closestGate(Location location)
	{
		Gate gate = null;
		double minmalDist = Double.MAX_VALUE;
		
		for (Gate g : Gate.getAll()) {
			
			if (!g.getLocation().getWorld().equals(location.getWorld()))
			{
				continue;
			}
			
			double tempDist = g.getLocation().distance(location);
			
			if (tempDist < minmalDist) 
			{
				gate = g;
				minmalDist = tempDist;
			}
			
		}
		
		return gate;
	}
	
	
	
	public static Gate getGateAtPlayerLocation(Location location)
	{
		Gate gate = null;
		World playerWorld = location.getWorld();
		
		// players are sometime stuck into the ground
		Location locationUp = location.getBlock().getRelative(BlockFace.UP).getLocation();
		
		for (Gate g : Gate.getAll()) 
		{
			if (gate != null)
			{
				break;
			}
			
			// Check if the gate is open and useable
			if (g.getLocation() == null) {
				continue;
			}
			
			World gateWorld = g.getLocation().getWorld();
			
			if (!g.isOpen() || !gateWorld.equals(playerWorld)) {
				continue;
			}
			
            
			// Check if the location matches
			for (Location l: g.getGateBlockLocations()) {
				
				if (LocationUtil.locationsAreAtSamePositions(l, location) || LocationUtil.locationsAreAtSamePositions(l, locationUp)) 
				{
					// Check if the gate is still valid
					try {
						g.validate();
	
						gate = g;
	                    break;
					} 
					catch (Exception e2) {
						break; // do nothing - gate got closed
					}
                }
            }
		}
		
		return gate;
	}
}
