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

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import de.craftinc.gates.Plugin;


public class FloodUtil {
    private static final Set<BlockFace> exp1 = new HashSet<>();
    private static final Set<BlockFace> exp2 = new HashSet<>();

    static {
        exp1.add(BlockFace.UP);
        exp1.add(BlockFace.DOWN);
        exp1.add(BlockFace.EAST);
        exp1.add(BlockFace.WEST);

        exp2.add(BlockFace.UP);
        exp2.add(BlockFace.DOWN);
        exp2.add(BlockFace.NORTH);
        exp2.add(BlockFace.SOUTH);
    }


    /**
     * Returns the all frame blocks of an gate.
     *
     * @param blocks All blocks inside the gate.
     * @return A Set containing all frame block. Will never return 'null'.
     */
    public static Set<Block> getFrame(final Set<Block> blocks) {
        if (blocks == null || blocks.isEmpty()) {
            return new HashSet<>();
        }

        // try to find gate's direction (north-south or east-west)
        Set<BlockFace> gateFrameSearchFaces = null;

        for (Block b : blocks) {

            if (blocks.contains(b.getRelative(BlockFace.EAST)) ||
                    blocks.contains(b.getRelative(BlockFace.WEST))) {

                gateFrameSearchFaces = exp1;
                break;
            }

            if (blocks.contains(b.getRelative(BlockFace.NORTH)) ||
                    blocks.contains(b.getRelative(BlockFace.SOUTH))) {

                gateFrameSearchFaces = exp2;
                break;
            }

        }

        if (gateFrameSearchFaces != null) {
            return _getFrame(blocks, gateFrameSearchFaces);
        } else { // no direction found (the gate might only consist of blocks one over another)

            // Try one direction and check if the found blocks are not air.
            // If air is found (frame broken or wrong direction) return the other direction
            Set<Block> frameBlocks = _getFrame(blocks, exp1);

            for (Block b : frameBlocks) {

                if (b.getType() == Material.AIR) {
                    return _getFrame(blocks, exp2);
                }
            }

            return frameBlocks;
        }
    }


    private static Set<Block> _getFrame(final Set<Block> blocks, final Set<BlockFace> searchDirections) {
        Set<Block> frameBlocks = new HashSet<>();

        for (Block b : blocks) {

            for (BlockFace bf : searchDirections) {
                Block bb = b.getRelative(bf);

                if (!blocks.contains(bb)) {
                    frameBlocks.add(bb);
                }
            }
        }

        return frameBlocks;
    }


    /**
     * Returns the all frame blocks of an gate.
     *
     * @param locations All locations inside the gate.
     * @return A Set containing all frame block. Will never return 'null'.
     */
    public static Set<Block> getFrameWithLocations(final Set<Location> locations) {
        if (locations == null) {
            throw new IllegalArgumentException("'locations' must not be 'null'");
        }

        Set<Block> blocks = new HashSet<>();

        for (Location l : locations) {
            blocks.add(l.getBlock());
        }

        return getFrame(blocks);
    }


    // For the same frame and location this set of blocks is deterministic
    public static Set<Block> getGatePortalBlocks(final Block block) {
        if (block == null) {
            throw new IllegalArgumentException("'block' must not be 'null'");
        }

        int frameBlockSearchLimit = Plugin.getPlugin().getConfig().getInt(ConfigurationUtil.confMaxGateBlocksKey);

        Set<Block> blocks1 = getAirFloodBlocks(block, new HashSet<Block>(), exp1, frameBlockSearchLimit);
        Set<Block> blocks2 = getAirFloodBlocks(block, new HashSet<Block>(), exp2, frameBlockSearchLimit);

        if (blocks1 == null && blocks2 == null) {
            return null;
        }

        if (blocks1 == null) {
            return blocks2;
        }

        if (blocks2 == null) {
            return blocks1;
        }

        if (blocks1.size() > blocks2.size()) {
            return blocks2;
        }

        return blocks1;
    }


    private static Set<Block> getAirFloodBlocks(final Block startBlock,
                                                  Set<Block> foundBlocks,
                                                  final Set<BlockFace> expandFaces,
                                                  int limit) {
        if (foundBlocks == null) {
            return null;
        }

        if (foundBlocks.size() > limit) {
            Plugin.log(Level.ALL, "exceeding gate size limit.");
            return null;
        }

        if (foundBlocks.contains(startBlock)) {
            return foundBlocks;
        }

        if (startBlock.getType() == Material.AIR) {
            // ... We found a block :D ...
            foundBlocks.add(startBlock);

            // ... And flood away !
            for (BlockFace face : expandFaces) {
                Block potentialBlock = startBlock.getRelative(face);
                foundBlocks = getAirFloodBlocks(potentialBlock, foundBlocks, expandFaces, limit);
            }
        }

        return foundBlocks;
    }
}
