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
package de.craftinc.gates.listeners;

import java.util.Calendar;
import java.util.HashMap;

import de.craftinc.gates.util.GateBlockChangeSender;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
	protected HashMap<String, Long> lastNoPermissionMessages = new HashMap<String, Long>();
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) 
	{
		if (event.isCancelled()) {
			return;
		}

        if (event.getFrom().getChunk() != event.getTo().getChunk()) {
            GateBlockChangeSender.updateGateBlocks(event.getPlayer(), event.getTo());
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
		if (!hasPermission(event.getPlayer(), gateAtLocation)
            && Plugin.getPlugin().getConfig().getBoolean(Plugin.confShowTeleportNoPermissionMessageKey)) {
			
			String playerName = event.getPlayer().getName();
			
			if (playerName == null) {
				return;
			}
	        
	        // get the current time
	        Long now = Calendar.getInstance().getTimeInMillis();
			
			// do not display messages more often than once per second
			if (!this.lastNoPermissionMessages.containsKey(playerName) || this.lastNoPermissionMessages.get(playerName) < now - 10000L) {

                String noPermissionString = Plugin.getPlugin().getConfig().getString(Plugin.confGateTeleportNoPermissionMessageKey);
                event.getPlayer().sendMessage(ChatColor.RED + noPermissionString);
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

        if (Plugin.getPlugin().getConfig().getBoolean(Plugin.confShowTeleportMessageKey)) {
            String teleporMessage = Plugin.getPlugin().getConfig().getString(Plugin.confGateTeleportMessageKey);
            p.sendMessage(ChatColor.DARK_AQUA + teleporMessage);
        }
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
