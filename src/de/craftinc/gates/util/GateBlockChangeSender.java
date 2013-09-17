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
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Set;

import static de.craftinc.gates.util.ConfigurationUtil.*;


public class GateBlockChangeSender
{
    /**
     * Replaces gate frame blocks with glowstone for a short period of time.
     * Uses the data stored in 'highlightDuration' inside the config file
     * for determining when to de-highlight the frames.
     * @param player The player for whom the frame should be highlighted.
     *               Must not be null!
     */
    public static void temporaryHighlightGatesFrames(final Player player, final Set<Gate> gates)
    {
        if (player == null) {
            throw new IllegalArgumentException("'player' must not be 'null'!");
        }

        if (gates == null) {
            throw new IllegalArgumentException("'gate' must not be 'null!");
        }

        for (Gate g : gates) {
            Set<Block> frameBlocks = g.getGateFrameBlocks();

            for (Block b : frameBlocks) {
                player.sendBlockChange(b.getLocation(), Material.GLOWSTONE, (byte)0);
            }
        }

        Plugin plugin = Plugin.getPlugin();
        long highlightDuration = 20 * plugin.getConfig().getLong(confHighlightDurationKey);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                dehighlightGatesFrames(player, gates);
            }
        }, highlightDuration);
    }


    public static void temporaryHighlightGateFrame(final Player player, final Gate gate)
    {
        if (gate == null) {
            throw new IllegalArgumentException("'gate' must not be 'null!");
        }

        if (player == null) {
            throw new IllegalArgumentException("'player' must not be 'null'!");
        }

        Set<Block> frameBlocks = gate.getGateFrameBlocks();

        for (Block b : frameBlocks) {
            player.sendBlockChange(b.getLocation(), Material.GLOWSTONE, (byte)0);
        }

        Plugin plugin = Plugin.getPlugin();
        long highlightDuration = 20 * plugin.getConfig().getLong(confHighlightDurationKey);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                dehighlightGateFrame(player, gate);
            }
        }, highlightDuration);
    }


    protected static void dehighlightGatesFrames(final Player player, final Set<Gate> gates)
    {
        for (Gate g : gates) {
            Set<Block> frameBlocks = g.getGateFrameBlocks();

            for (Block b : frameBlocks) {
                player.sendBlockChange(b.getLocation(), b.getType(), (byte)0);
            }
        }
    }


    protected static void dehighlightGateFrame(final Player player, final Gate gate)
    {
        Set<Block> frameBlocks = gate.getGateFrameBlocks();

        for (Block b : frameBlocks) {
            player.sendBlockChange(b.getLocation(), b.getType(), (byte)0);
        }
    }


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
        GateMaterial gateMaterial = getPortalMaterial();

        if (gatesNearby == null) {
            return; // no gates nearby
        }

        for (Gate g : gatesNearby) {

            if (!g.isOpen() || g.isHidden()) {
                continue;
            }

            for (Location l : g.getGateBlockLocations()) {

                if (l.getBlock().getType() == Material.AIR) {
                    player.sendBlockChange(l, gateMaterial.material, gateMaterial.data);
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
     * @param remove Set to true if all visible gate blocks shall be removed.
     */
    public static void updateGateBlocks(final Gate gate, boolean remove)
    {
        if (gate == null) {
            throw new IllegalArgumentException("'gate must not be 'null'!");
        }

        Location gateLocation = gate.getLocation();

        if (gate.getGateBlockLocations().isEmpty()) {
            return;
        }

        ArrayList<Player> playersNearby = new ArrayList<Player>();

        int searchRadius = Plugin.getPlugin().getConfig().getInt(confPlayerGateBlockUpdateRadiusKey);

        for (Player p : Plugin.getPlugin().getServer().getOnlinePlayers()) {

            if (p.getWorld() == gateLocation.getWorld() && p.getLocation().distance(gateLocation) < searchRadius) {
                playersNearby.add(p);
            }
        }

        GateMaterial gateMaterial = getPortalMaterial();
        Material material;
        byte data = 0;

        if (gate.isOpen() && !gate.isHidden() && !remove) {
            material = gateMaterial.material;
            data = gateMaterial.data;
        }
        else {
            material = Material.AIR;
        }

        for (Player p : playersNearby) {

            for (Location l : gate.getGateBlockLocations()) {

                if (l.getBlock().getType() == Material.AIR) { // on server-side a gate is always made out of AIR
                    p.sendBlockChange(l, material, data);
                }
            }
        }
    }
}
