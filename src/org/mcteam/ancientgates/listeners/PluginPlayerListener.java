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

import org.mcteam.ancientgates.Conf;
import org.mcteam.ancientgates.Gate;
import org.mcteam.ancientgates.Plugin;
import org.mcteam.ancientgates.util.GeometryUtil;


public class PluginPlayerListener implements Listener 
{
    
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) 
	{
		if (event.isCancelled())
			return;
		
		Block blockTo = event.getTo().getBlock();
		Block blockToUp = blockTo.getRelative(BlockFace.UP);
		
		// Find the nearest gate!
		Gate nearestGate = null;
		Location playerLocation = event.getPlayer().getLocation();
		
		for (Gate gate : Gate.getAll()) 
		{
			if (gate.getFrom() == null ||
				gate.getTo() == null || 
				gate.isOpen() == false ||
				!gate.getFrom().getWorld().equals(playerLocation.getWorld()))
			{
				continue;
			}

			double distance = GeometryUtil.distanceBetweenLocations(playerLocation, gate.getFrom());
			
			if (distance > Conf.getGateSearchRadius())
				continue;
			
			Plugin.log(Level.ALL, "in gate search radius of " + gate.getId());
			
            for (Integer[] blockXYZ: gate.getGateBlocks()) 
            {
                if ((blockTo.getX() == blockXYZ[0] || blockToUp.getX() == blockXYZ[0]) &&
                    (blockTo.getY() == blockXYZ[1] || blockToUp.getY() == blockXYZ[1]) &&
                    (blockTo.getZ() == blockXYZ[2] || blockToUp.getZ() == blockXYZ[2])) 
                {
                    nearestGate = gate;
                    break;
                }
            }
		}
		
		if (nearestGate != null) 
		{
			checkChunkLoad(nearestGate.getTo().getBlock());
            Float newYaw = nearestGate.getFrom().getYaw() - nearestGate.getTo().getYaw() + playerLocation.getYaw();
            Location teleportToLocation = new Location( nearestGate.getTo().getWorld(),
                                						nearestGate.getTo().getX(),
						                                nearestGate.getTo().getY(),
						                                nearestGate.getTo().getZ(),
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
}
