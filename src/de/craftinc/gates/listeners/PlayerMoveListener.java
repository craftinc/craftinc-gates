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

import de.craftinc.gates.util.ConfigurationUtil;
import de.craftinc.gates.util.GateBlockChangeSender;
import de.craftinc.gates.util.VehicleCloner;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.craftinc.gates.Gate;
import de.craftinc.gates.GatesManager;
import de.craftinc.gates.Plugin;
import org.bukkit.scheduler.BukkitScheduler;


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

		final GatesManager gateManager = Plugin.getPlugin().getGatesManager();
		final Gate gateAtLocation = gateManager.getGateAtLocation(event.getTo());

		if ((gateAtLocation == null) || !gateAtLocation.isOpen()) {
                return;
		}

		// Check for permission
		if (!hasPermission(event.getPlayer(), gateAtLocation)
            && Plugin.getPlugin().getConfig().getBoolean(ConfigurationUtil.confShowTeleportNoPermissionMessageKey)) {
			
			final String playerName = event.getPlayer().getName();
			
			if (playerName == null) {
				return;
			}
	        
	        // get the current time
	        final Long now = Calendar.getInstance().getTimeInMillis();
			
			// do not display messages more often than once per second
			if (!this.lastNoPermissionMessages.containsKey(playerName) || this.lastNoPermissionMessages.get(playerName) < now - 10000L) {

                final String noPermissionString = Plugin.getPlugin().getConfig().getString(ConfigurationUtil.confGateTeleportNoPermissionMessageKey);
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
     * @param player The player to teleport.
     * @param gate The gate to which exit the player will be teleported.
     */
	private void teleportPlayer(final Player player, final Gate gate)
	{
        // Destination
        final Float newYaw = gate.getExit().getYaw() - gate.getLocation().getYaw() + player.getLocation().getYaw();
        final Location destLocation = new Location( gate.getExit().getWorld(),
                                                    gate.getExit().getX(),
                                                    gate.getExit().getY(),
                                                    gate.getExit().getZ(),
                                                    newYaw,
                                                    player.getLocation().getPitch()
                                                  );

        // Riding (eject player)
        final Entity vehicle = player.getVehicle();
        final boolean vehicleIsSuitable = (vehicle != null) && (vehicle instanceof Vehicle);

        if (vehicleIsSuitable) {
            vehicle.eject();
            vehicle.remove();
        }

        // Teleport
        player.teleport(destLocation);

        // Riding (mount player)
        if (vehicleIsSuitable) {
            final Plugin plugin = Plugin.getPlugin();
            final BukkitScheduler scheduler = plugin.getServer().getScheduler();

            destLocation.getChunk().load(); // load the destination chunk, no new entity will be created otherwise

            scheduler.scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run()
                {
                    // TODO: the code below should be executed after the chunk got loaded and not after a fixed time!

                    // create a new entity at the destination location
                    final Vehicle newVehicle = VehicleCloner.clone((Vehicle)vehicle, destLocation);
                    newVehicle.setPassenger(player);
                }
            }, 2);
        }

        // Message
        if (Plugin.getPlugin().getConfig().getBoolean(ConfigurationUtil.confShowTeleportMessageKey)) {
            final String teleportMessage = Plugin.getPlugin().getConfig().getString(ConfigurationUtil.confGateTeleportMessageKey);
            player.sendMessage(ChatColor.DARK_AQUA + teleportMessage);
        }
	}
	
	
	protected boolean hasPermission(final Player player, final Gate gate)
	{
		if (Plugin.getPermission() == null) { // fallback: use the standard bukkit permission system
			return player.hasPermission(Plugin.permissionUse);
		}
		else {
			final boolean permAtLocation = Plugin.getPermission().has(gate.getLocation().getWorld(), player.getName(), Plugin.permissionUse);
			final boolean permAtExit = Plugin.getPermission().has(gate.getExit().getWorld(), player.getName(), Plugin.permissionUse);
			
			return permAtLocation && permAtExit;
		}
    }
}
