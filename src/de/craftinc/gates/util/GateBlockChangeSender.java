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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;


public class GateBlockChangeSender
{
    protected static final int searchRadius = 64; // TODO: move search radius into a config file / get value from config class


    public static void updateGateBlocks(final Player player)
    {
        if (player == null) {
            throw new IllegalArgumentException("'player' must not be 'null'!");
        }

        updateGateBlocks(player, player.getLocation());
    }


    public static void updateGateBlocks(final Player player, final Location location)
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
    }



    public static void updateGateBlocks(final Gate gate)
    {
        if (gate == null) {
            throw new IllegalArgumentException("'gate must not be 'null'!");
        }

        Location gateLocation = gate.getLocation();
        ArrayList<Player> playersNearby = new ArrayList<Player>();

        for (Player p : Plugin.getPlugin().getServer().getOnlinePlayers()) {

            if (p.getLocation().distance(gateLocation) < searchRadius) {
                playersNearby.add(p);
            }
        }

        Material material;

        if (gate.isOpen() && !gate.isHidden()) {
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
