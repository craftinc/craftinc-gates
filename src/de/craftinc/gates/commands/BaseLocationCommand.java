package de.craftinc.gates.commands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public abstract class BaseLocationCommand extends BaseCommand 
{
	protected Location getValidPlayerLocation()
	{
		// The player might stand in a halfblock or a sign or whatever
		// Therefore we load som extra locations and blocks
		Block playerBlock = player.getLocation().getBlock();
		Block upBlock = playerBlock.getRelative(BlockFace.UP);
                
		if (playerBlock.getType() == Material.AIR) {
			return player.getLocation();
		} 
		else if (upBlock.getType() == Material.AIR) {
			return new Location(player.getLocation().getWorld(),
					 			player.getLocation().getX(),
					 			player.getLocation().getY() + 1,
					 			player.getLocation().getZ(),
					 			player.getLocation().getYaw(),
					 			player.getLocation().getPitch());
		} 
		
		return null;
	}
}
