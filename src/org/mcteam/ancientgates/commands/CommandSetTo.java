package org.mcteam.ancientgates.commands;

import org.bukkit.command.CommandSender;

public class CommandSetTo extends BaseCommand {
	
	public CommandSetTo() {
		aliases.add("setto");
		aliases.add("st");
		
		requiredParameters.add("id");		
		
		helpDescription = "Set \"to\" to your location.";
	}
	
	public void perform() {
		gate.setExit(player.getLocation());
		sendMessage("To location for gate \""+gate.getId()+"\" is now where you stand.");
	}
	
	
	@Override
	public boolean hasPermission(CommandSender sender) 
	{
		return sender.hasPermission(permissionManage);
	}
}

