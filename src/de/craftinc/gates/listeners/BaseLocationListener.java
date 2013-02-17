package de.craftinc.gates.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerMoveEvent;

import de.craftinc.gates.Gate;


public abstract class BaseLocationListener 
{
	protected Gate getValidGateAtPlayerLocation(PlayerMoveEvent e) {
		Gate gate = null;
		
		Location playerLocation = e.getPlayer().getLocation();
		World playerWorld = playerLocation.getWorld();
		
		Block blockTo = e.getFrom().getBlock();
		Block blockToUp = blockTo.getRelative(BlockFace.UP);
		
		
		for (Gate g : Gate.getAll()) {
			// Check if the gate is open and useable
			World gateWorld = g.getLocation().getWorld();
			
			if (!g.isOpen() || !gateWorld.equals(playerWorld)) {
				continue;
			}
			
            
			// Check if the location matches
			for (Location l: g.getGateBlockLocations()) {
				
				if (locationsAreAtSamePositions(l, blockTo.getLocation()) || locationsAreAtSamePositions(l, blockToUp.getLocation())) {
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
	
	
	protected Gate getGateAtPlayerLocation(PlayerMoveEvent e) {
		Gate gate = null;
		
		Block blockTo = e.getFrom().getBlock();
		Block blockToUp = blockTo.getRelative(BlockFace.UP);
		
		
		for (Gate g : Gate.getAll()) {
			// Check if the location matches
			for (Location l: g.getGateBlockLocations()) {
				
				if (locationsAreAtSamePositions(l, blockTo.getLocation()) || locationsAreAtSamePositions(l, blockToUp.getLocation())) {
					gate = g;
                }
            }
		}
		
		return gate;
	}
	
	
	/**
     * Does the same as the equal method of Location but ignores pitch and yaw.
     */
	protected boolean locationsAreAtSamePositions(final Location l1, final Location l2)
    {
		if (l1.getWorld() != l2.getWorld() && (l1.getWorld() == null || !l1.getWorld().equals(l2.getWorld()))) {
			return false;
        }
		if (Double.doubleToLongBits(l1.getX()) != Double.doubleToLongBits(l2.getX())) {
			return false;
		}
		if (Double.doubleToLongBits(l1.getY()) != Double.doubleToLongBits(l2.getY())) {
			return false;
		}
		if (Double.doubleToLongBits(l1.getZ()) != Double.doubleToLongBits(l2.getZ())) {
			return false;
		}
		
		return true;
    }
}
