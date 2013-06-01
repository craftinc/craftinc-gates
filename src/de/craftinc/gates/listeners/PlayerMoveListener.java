package de.craftinc.gates.listeners;

import java.util.Calendar;
import java.util.HashMap;

import de.craftinc.gates.util.TeleportRequest;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
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

        if (!gateAtLocation.isOpen()) {
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
		}
        else {
            this.teleportPlayer(event.getPlayer(), gateAtLocation);
        }
	}


    /**
     * Teleports a player. This method will check if the destination chunk is loaded and will wait until the chunk
     * is loaded before executing the teleportion event.
     * @param p The player to teleport.
     * @param g The gate to which exit the player will be teleported.
     */
	private void teleportPlayer(Player p, Gate g)
	{
        Location playerLocation = p.getLocation();
        Location exit = g.getExit();

        Float newYaw = g.getExit().getYaw() - g.getLocation().getYaw() + playerLocation.getYaw();

        Location teleportToLocation = new Location( g.getExit().getWorld(),
                                                    g.getExit().getX(),
                                                    g.getExit().getY(),
                                                    g.getExit().getZ(),
                                                    newYaw,
                                                    playerLocation.getPitch()
                                                  );

        Chunk teleportToChunk = teleportToLocation.getChunk();

		if (teleportToChunk.isLoaded()) {
            p.teleport(teleportToLocation);
            p.sendMessage(ChatColor.DARK_AQUA + "Thank you for traveling with Craft Inc. Gates.");
        }
        else {
            TeleportRequest request = new TeleportRequest(p, teleportToLocation);
            Plugin.getPlugin().getChunkLoadListener().addTeleportRequest(request);

			teleportToChunk.load();
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
