package org.mcteam.ancientgates.commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.mcteam.ancientgates.Gate;

public class CommandSetFrom extends BaseCommand {
	
	public CommandSetFrom() {
		aliases.add("setfrom");
		
		requiredParameters.add("id");		
		
		helpDescription = "Set \"from\" to your location.";
	}
	
	public void perform() {
		// The player might stand in a halfblock or a sign or whatever
		// Therefore we load som extra locations and blocks
		Block playerBlock = player.getLocation().getBlock();
		Block upBlock = playerBlock.getRelative(BlockFace.UP);
		
		if (playerBlock.getType() == Material.AIR) {
			gate.setFrom(playerBlock.getLocation());
		} else if (upBlock.getType() == Material.AIR) {
			gate.setFrom(upBlock.getLocation());
		} else {
			sendMessage("There is not enough room for a gate to open here");
			return;
		}
		
		sendMessage("From location for gate \""+gate.getId()+"\" is now where you stand.");
		sendMessage("Build a frame around that block and:");
		sendMessage(new CommandOpen().getUseageTemplate(true, true));
		
		Gate.save();
	}
	
}

