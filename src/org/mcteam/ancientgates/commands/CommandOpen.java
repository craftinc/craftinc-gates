package org.mcteam.ancientgates.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.mcteam.ancientgates.Gate;

public class CommandOpen extends BaseCommand {
	
	public CommandOpen() {
		aliases.add("open");
		
		requiredParameters.add("id");		
		
		helpDescription = "Open that gate";
	}
	
	public void perform() {
		if (gate.getFrom() == null) {
			sendMessage("You must set the from location first. To fix that:");
			sendMessage(new CommandSetFrom().getUsageTemplate(true, true));
			return;
		}
		
		if (gate.getTo() == null) {
			sendMessage("Sure, but note that this gate does not point anywhere :P");
			sendMessage("To fix that: " + new CommandSetTo().getUsageTemplate(true, true));
		}
		
		if (gate.getFrom().getBlock().getType() != Material.AIR) {
			sendMessage("The gate could not open. The from location is not air.");
			return;
		}
		
		if (gate.open()) {
			sendMessage("The gate was opened.");
		} else {
			sendMessage("Failed to open the gate. Have you built a frame?");
			sendMessage("More info here: " + new CommandHelp().getUsageTemplate(true, true));
		}
		
		Gate.save();
	}
	
	
	@Override
	public boolean hasPermission(CommandSender sender) 
	{
		return sender.hasPermission(permissionManage);
	}
}

