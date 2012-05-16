package org.mcteam.ancientgates.listeners;

import java.util.logging.Level;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
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
		if (event.isCancelled()) {
			return;
		}
		
		// Check for permission
		if (!hasPermission(event.getPlayer())) {
			return;
		}
		
		// Find the nearest gate!
		Gate gateAtLocation = getGateAtPlayerLocation(event);
		
		
		if (gateAtLocation == null) {
			return;
		}
		
		// Teleport the player
		checkChunkLoad(gateAtLocation.getLocation().getBlock());
		
		Location gateExit = gateAtLocation.getExit();
		Location playerLocation = event.getPlayer().getLocation();
		
        Float newYaw = gateExit.getYaw() - gateExit.getYaw() + playerLocation.getYaw();
        
        Location teleportToLocation = new Location( gateExit.getWorld(),
        											gateExit.getX(),
        											gateExit.getY(),
        											gateExit.getZ(),
					                                newYaw, 
					                                playerLocation.getPitch() );
                    
		event.getPlayer().teleport(teleportToLocation);
		event.setTo(teleportToLocation);
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
	
	
	protected boolean hasPermission(Player player) {
		if (player.hasPermission(Plugin.permissionUse)) {
			return true;
		}
		
		if (player.hasPermission(Plugin.permissionAll)) {
			return true;
		}
	
		return false;
	}
	
	
	protected Gate getGateAtPlayerLocation(PlayerMoveEvent e) {
		Gate gate = null;
		
		Location playerLocation = e.getPlayer().getLocation();
		World playerWorld = playerLocation.getWorld();
		
		Block blockTo = e.getTo().getBlock();
		Block blockToUp = blockTo.getRelative(BlockFace.UP);
		
		
		for (Gate g : Gate.getAll()) {
			// Check if the gate is open and useable
			World gateWorld = g.getLocation().getWorld();
			
			if (g.isOpen() == false || !gateWorld.equals(playerWorld)) {
				continue;
			}
			
            
			// Check if the location matches
			for (Location l: g.getGateBlockLocations()) {
				
				if (locationsAreAtSamePositions(l, blockTo.getLocation()) || locationsAreAtSamePositions(l, blockToUp.getLocation())) {
					// Check if the gate is still valid
					g.validate();
					
					if (g.isOpen()) {
						gate = g;
	                    break;
					}
                }
            }
		}
		
		return gate;
	}
	
}
