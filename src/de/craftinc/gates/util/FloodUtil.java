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

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import de.craftinc.gates.Plugin;


public class FloodUtil 
{
	private final static int frameBlockSearchLimit = 100; // TODO: move search radius into a config file / get value from config class
	
	private static final Set<BlockFace> exp1 = new HashSet<BlockFace>();
	private static final Set<BlockFace> exp2 = new HashSet<BlockFace>();
	
	static 
	{
		exp1.add(BlockFace.UP);
		exp1.add(BlockFace.DOWN);
		exp1.add(BlockFace.EAST);
		exp1.add(BlockFace.WEST);
		
		exp2.add(BlockFace.UP);
		exp2.add(BlockFace.DOWN);
		exp2.add(BlockFace.NORTH);
		exp2.add(BlockFace.SOUTH);
	}
	
	
	// For the same frame and location this set of blocks is deterministic
	public static Set<Block> getGateFrameBlocks(Block block) 
	{
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
	
	
	private static Set<Block> getAirFloodBlocks(Block startBlock, Set<Block> foundBlocks, Set<BlockFace> expandFaces, int limit) 
	{
		if (foundBlocks == null) {
			return null;
		}
		
		if  (foundBlocks.size() > limit) {
			Plugin.log(Level.ALL, "exceeding gate size limit.");
			return null;
		}
		
		if (foundBlocks.contains(startBlock))  {
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
