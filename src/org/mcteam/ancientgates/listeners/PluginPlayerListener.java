package org.mcteam.ancientgates.listeners;

import java.util.logging.Level;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import org.mcteam.ancientgates.Gate;
import org.mcteam.ancientgates.Plugin;


public class PluginPlayerListener implements Listener 
{
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) 
	{
		if (event.isCancelled())
			return;
		
		// check for permission
		if (!event.getPlayer().hasPermission("ancientgates.use")) {
			return;
		}
		
		
		// Find the nearest gate!
		Gate nearestGate = null;
		
		Location playerLocation = event.getPlayer().getLocation();
		World playerWorld = playerLocation.getWorld();
		
		Block blockTo = event.getTo().getBlock();
		Block blockToUp = blockTo.getRelative(BlockFace.UP);
		
		
		for (Gate gate : Gate.getAll()) 
		{
			// Check if the gate is open and useable
			World gateWorld = gate.getLocation().getWorld();
			
			if (gate.getLocation() == null || gate.getExit() == null || gate.isOpen() == false || !gateWorld.equals(playerWorld)) {
				continue;
			}
			
            
			// Check if the location matches
			for (Location l: gate.getGateBlockLocations()) 
            {
				if (locationsAreAtSamePositions(l, blockTo.getLocation()) || locationsAreAtSamePositions(l, blockToUp.getLocation())) 
                {
                    nearestGate = gate;
                    break;
                }
            }
		}
		
		if (nearestGate != null) 
		{
			checkChunkLoad(nearestGate.getLocation().getBlock());
            Float newYaw = nearestGate.getExit().getYaw() - nearestGate.getExit().getYaw() + playerLocation.getYaw();
            Location teleportToLocation = new Location( nearestGate.getExit().getWorld(),
                                						nearestGate.getExit().getX(),
						                                nearestGate.getExit().getY(),
						                                nearestGate.getExit().getZ(),
						                                newYaw, playerLocation.getPitch() );
                        
			event.getPlayer().teleport(teleportToLocation);
			event.setTo(teleportToLocation);
		}
	}
	
	
	private void checkChunkLoad(Block b) 
	{
		World w = b.getWorld();
		Chunk c = b.getChunk();
		
		if ( ! w.isChunkLoaded(c) )
		{
		    Plugin.log(Level.FINE, "Loading chunk: " + c.toString() + " on: " + w.toString());
			w.loadChunk(c);
		}
	}
	
	
	/**
     * Does the same as the equal method of Location but ignores fitch and yaw.
     */
	private static boolean locationsAreAtSamePositions(final Location l1, final Location l2)
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
