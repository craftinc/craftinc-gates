package org.mcteam.ancientgates.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;


public class CommandInfo extends BaseCommand 
{
	public CommandInfo()
	{
		aliases.add("info");
		aliases.add("details");
		
		requiredParameters.add("id");		
		
		helpDescription = "Prints information about a gate";
	}
	
	
	public void perform() 
	{
		sendMessage(ChatColor.LIGHT_PURPLE + "Information about " + ChatColor.WHITE + gate.getId() + ChatColor.LIGHT_PURPLE + ":");
		
		String openHiddenMessage = "This gate is";
		
		if (gate.isOpen())
			openHiddenMessage += " open";
		else
			openHiddenMessage += " closed";
		
		if (gate.isHidden())
			openHiddenMessage += " and hidden";
		
		openHiddenMessage += ".";
		
		sendMessage(openHiddenMessage);
		
		if (gate.getLocation() != null)
			sendMessage(ChatColor.GREEN + "'from' location:       " + ChatColor.YELLOW + "( " + gate.getLocation().getBlockX() + " | " + gate.getLocation().getBlockY() + " | " + gate.getLocation().getBlockZ() + " ) in " + gate.getLocation().getWorld().getName());
		else
			sendMessage(ChatColor.GREEN + "this gate has no 'from' location");
		
		if (gate.getExit() != null)
			sendMessage(ChatColor.GREEN + "'to' location:          " + ChatColor.YELLOW + "( " + gate.getExit().getBlockX() + " | " + gate.getExit().getBlockY() + " | " + gate.getExit().getBlockZ() + " ) in " + gate.getExit().getWorld().getName());
		else
			sendMessage(ChatColor.GREEN + "this gate has no 'to' location");
	}
	
	
	@Override
	public boolean hasPermission(CommandSender sender) 
	{
		return sender.hasPermission(permissionInfo) || sender.hasPermission(permissionManage);
	}
}
