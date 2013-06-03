package de.craftinc.gates.listeners;

import java.util.Calendar;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.craftinc.gates.gates.Gate;
import de.craftinc.gates.gates.GatesManager;
import de.craftinc.gates.Plugin;


public class PlayerMoveListener implements Listener
{
	protected HashMap<String, Long> lastNoPermissionMessages = new HashMap<String, Long>();
	
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
			if (!this.lastNoPermissionMessages.containsKey(playerName) || this.lastNoPermissionMessages.get(playerName) < now - 10000L) {
				event.getPlayer().sendMessage(ChatColor.RED + "You are not allowed to use this gate!");
				this.lastNoPermissionMessages.put(playerName, now);
			}
		}
        else {
            this.teleportPlayer(event.getPlayer(), gateAtLocation);
        }
	}


    /**
     * Teleports a player.
     * @param p The player to teleport.
     * @param g The gate to which exit the player will be teleported.
     */
	private void teleportPlayer(Player p, Gate g)
	{
        Float newYaw = g.getExit().getYaw() - g.getLocation().getYaw() + p.getLocation().getYaw();

        Location destLocation = new Location( g.getExit().getWorld(),
                                              g.getExit().getX(),
                                              g.getExit().getY(),
                                              g.getExit().getZ(),
                                              newYaw,
                                              p.getLocation().getPitch()
                                            );

        p.teleport(destLocation);
        p.sendMessage(ChatColor.DARK_AQUA + "Thank you for traveling with Craft Inc. Gates.");
	}
	
	
	protected boolean hasPermission(Player player, Gate gate) 
	{
		if (Plugin.getPermission() == null) { // fallback: use the standard bukkit permission system
			return player.hasPermission(Plugin.permissionUse);
		}
		else {
			boolean permAtLocation = Plugin.getPermission().has(gate.getLocation().getWorld(), player.getName(), Plugin.permissionUse);
			boolean permAtExit = Plugin.getPermission().has(gate.getExit().getWorld(), player.getName(), Plugin.permissionUse);
			
			return permAtLocation && permAtExit;
		}
    }
}
