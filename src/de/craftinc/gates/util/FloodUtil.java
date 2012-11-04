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
	private final static int frameBlockSearchLimit = 20;
	
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
	
	
	public static Set<Block> getAirFloodBlocks(Block startBlock, Set<Block> foundBlocks, Set<BlockFace> expandFaces, int limit) 
	{
		if (foundBlocks == null)
			return null;
		
		if  (foundBlocks.size() > limit) 
		{
			Plugin.log(Level.ALL, "exceeding gate size limit.");
			return null;
		}
		
		if (foundBlocks.contains(startBlock)) 
			return foundBlocks;
		
		if (startBlock.getType() == Material.AIR || startBlock.getType() == Material.PORTAL) 
		{
			// ... We found a block :D ...
			foundBlocks.add(startBlock);
			
			// ... And flood away !
			for (BlockFace face : expandFaces) 
			{
				Block potentialBlock = startBlock.getRelative(face);
				foundBlocks = getAirFloodBlocks(potentialBlock, foundBlocks, expandFaces, limit);
			}
		}
		
		return foundBlocks;
	}
	
}