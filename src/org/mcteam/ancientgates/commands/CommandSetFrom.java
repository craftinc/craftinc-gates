package org.mcteam.ancientgates.commands;

import java.util.Set;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.mcteam.ancientgates.Conf;
import org.mcteam.ancientgates.Gate;
import org.mcteam.ancientgates.util.FloodUtil;

public class CommandSetFrom extends BaseCommand {
	
	public CommandSetFrom() {
		aliases.add("setfrom");
		aliases.add("sf");
		
		requiredParameters.add("id");		
		
		helpDescription = "Set \"from\" to your location.";
	}
	
	public void perform() {
		// The player might stand in a halfblock or a sign or whatever
		// Therefore we load som extra locations and blocks
		Block playerBlock = player.getLocation().getBlock();
		Block upBlock = playerBlock.getRelative(BlockFace.UP);
		Location playerUpLocation = new Location(player.getLocation().getWorld(),
                        player.getLocation().getX(),
                        player.getLocation().getY() + 1,
                        player.getLocation().getZ(),
                        player.getLocation().getYaw(),
                        player.getLocation().getPitch());
                
                Set<Block> gateBlocks = FloodUtil.getGateFrameBlocks(player.getLocation().getBlock());
                if (gateBlocks == null) {
                        sendMessage("There is no portal here, or your portal is too large.\nMax size is: " + Conf.getGateMaxArea() + " Blocks.");
                        return;
                }
                
		if (playerBlock.getType() == Material.AIR) {
			gate.setFrom(player.getLocation());
			gate.setGateBlocks(gateBlocks);
		} else if (upBlock.getType() == Material.AIR) {
			gate.setFrom(playerUpLocation);
                        gate.setGateBlocks(gateBlocks);
		} else {
			sendMessage("There is not enough room for a gate to open here");
			return;
		}
                

		
		sendMessage("From location for gate \""+gate.getId()+"\" is now where you stand.");
		sendMessage("Your gate includes " + gateBlocks.size() + " Blocks.");

		Gate.save();
	}
	
}

