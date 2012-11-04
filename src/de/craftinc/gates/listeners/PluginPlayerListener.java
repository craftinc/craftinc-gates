package de.craftinc.gates.listeners;

import java.util.logging.Level;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.craftinc.gates.Gate;
import de.craftinc.gates.Plugin;


public class PluginPlayerListener extends BaseLocationListener implements Listener 
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
		
		// Find the gate at the current location.
		Gate gateAtLocation = getValidGateAtPlayerLocation(event);
		
		
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
	
	
	protected boolean hasPermission(Player player) {
        return player.hasPermission(Plugin.permissionUse) || player.hasPermission(Plugin.permissionAll);
    }
}
