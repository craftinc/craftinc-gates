package de.craftinc.gates.listeners;

import java.util.Calendar;
import java.util.HashMap;
import java.util.logging.Level;

import org.bukkit.ChatColor;
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
import de.craftinc.gates.GatesManager;
import de.craftinc.gates.Plugin;


public class PlayerMoveListener implements Listener
{
	protected HashMap<String, Long> lastBorderMessage = new HashMap<String, Long>();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) 
	{
		if (event.isCancelled()) {
			return;
		}
		
		GatesManager gateManager = Plugin.getPlugin().getGatesManager();
		Gate gateAtLocation = gateManager.getGateAtLocation(event.getTo());

		if (gateAtLocation == null) {
			return;
		}
		
		// Check for permission
		if (!hasPermission(event.getPlayer(), gateAtLocation)) {
			
			String playerName = event.getPlayer().getName();
			
			if (playerName == null) {
				return;
			}
	        
	        // get the current time
	        Long now = Calendar.getInstance().getTimeInMillis();
			
			// do not display messages more often than once per second
			if (!this.lastBorderMessage.containsKey(playerName) || this.lastBorderMessage.get(playerName) < now - 10000L) {
				event.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to use this gate!");
				this.lastBorderMessage.put(playerName, now);
			}
			
			return;
		}
		
		// Teleport the player
		checkChunkLoad(gateAtLocation.getLocation().getBlock());
		
		Location gateExit = gateAtLocation.getExit();
		Location gateLocation = gateAtLocation.getLocation();
		Location playerLocation = event.getPlayer().getLocation();
		
        Float newYaw = gateExit.getYaw() - gateLocation.getYaw() + playerLocation.getYaw();
        
        Location teleportToLocation = new Location( gateExit.getWorld(),
        											gateExit.getX(),
        											gateExit.getY(),
        											gateExit.getZ(),
					                                newYaw, 
					                                playerLocation.getPitch() );
                    
		event.getPlayer().teleport(teleportToLocation);
		event.setTo(teleportToLocation);
		
		event.getPlayer().sendMessage(ChatColor.DARK_AQUA + "Thank you for traveling with Craft Inc. Gates.");
	}
	
	
	private void checkChunkLoad(Block b) 
	{
		World w = b.getWorld();
		Chunk c = b.getChunk();
		
		if (!w.isChunkLoaded(c))
		{
		    Plugin.log(Level.FINE, "Loading chunk: " + c.toString() + " on: " + w.toString());
			w.loadChunk(c);
		}
	}
	
	
	protected boolean hasPermission(Player player, Gate gate) 
	{
		if (Plugin.getPermission() == null) // fallback: use the standard bukkit permission system
		{
			return player.hasPermission(Plugin.permissionUse);
		}
		else {
			boolean permAtLocation = Plugin.getPermission().has(gate.getLocation().getWorld(), player.getName(), Plugin.permissionUse);
			boolean permAtExit = Plugin.getPermission().has(gate.getExit().getWorld(), player.getName(), Plugin.permissionUse);
			
			return permAtLocation && permAtExit;
		}
    }
}
