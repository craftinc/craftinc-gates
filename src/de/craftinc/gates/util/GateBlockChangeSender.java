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
package de.craftinc.gates.util;


import de.craftinc.gates.Plugin;
import de.craftinc.gates.Gate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;


public class GateBlockChangeSender
{
    /**
     * Sends gate blocks to player at a given location. Will send the updates either immediately or
     * immediately and after a short delay.
     * @param player      A player to send block changes to. Must not be null!
     * @param location    The location to look for gates nearby. Must not be null!
     * @param sendDelayed Set to 'true' if the block changes shall be send a second time after a one
     *                    second delay.
     */
    public static void updateGateBlocks(final Player player, final Location location, boolean sendDelayed)
    {
        if (player == null) {
            throw new IllegalArgumentException("'player' must not be 'null'!");
        }

        if (location == null) {
            throw new IllegalArgumentException("'location' must not be 'null'!");
        }

        Set<Gate> gatesNearby = Plugin.getPlugin().getGatesManager().getNearbyGates(location.getChunk());

        if (gatesNearby == null) {
            return; // no gates nearby
        }

        for (Gate g : gatesNearby) {

            if (!g.isOpen() || g.isHidden()) {
                continue;
            }

            for (Location l : g.getGateBlockLocations()) {

                if (l.getBlock().getType() == Material.AIR) {
                    player.sendBlockChange(l, Material.PORTAL, (byte)0);
                }
            }
        }

        if (sendDelayed) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Plugin.getPlugin(), new Runnable()
            {
                @Override
                public void run()
                {
                    updateGateBlocks(player, location, false);
                }
            }, 20L);
        }
    }


    /**
     * This method calls: updateGateBlocks(player, location, false);
     */
    public static void updateGateBlocks(final Player player, final Location location)
    {
        updateGateBlocks(player, location, false);
    }

    /**
     * This method calls: updateGateBlocks(player, player.getLocation(), false);
     */
    public static void updateGateBlocks(final Player player)
    {
        if (player == null) {
            throw new IllegalArgumentException("'player' must not be 'null'!");
        }

        updateGateBlocks(player, player.getLocation(), false);
    }


    public static void updateGateBlocks(final Gate gate)
    {
        updateGateBlocks(gate, false);
    }

    /**
     * Sends block changes to players near a given gate.
     * @param gate Must not be 'null'!
     */
    public static void updateGateBlocks(final Gate gate, boolean deleted)
    {
        if (gate == null) {
            throw new IllegalArgumentException("'gate must not be 'null'!");
        }

        Location gateLocation = gate.getLocation();
        ArrayList<Player> playersNearby = new ArrayList<Player>();

        int searchRadius = Plugin.getPlugin().getConfig().getInt(Plugin.confPlayerGateBlockUpdateRadiusKey);

        for (Player p : Plugin.getPlugin().getServer().getOnlinePlayers()) {

            if (p.getWorld() == gateLocation.getWorld() && p.getLocation().distance(gateLocation) < searchRadius) {
                playersNearby.add(p);
            }
        }

        Material material;

        if (gate.isOpen() && !gate.isHidden() && !deleted) {
            material = Material.PORTAL;
        }
        else {
            material = Material.AIR;
        }

        for (Player p : playersNearby) {

            for (Location l : gate.getGateBlockLocations()) {

                if (l.getBlock().getType() == Material.AIR) { // on server-side a gate is always made out of AIR
                    p.sendBlockChange(l, material, (byte)0);
                }
            }
        }
    }
}
