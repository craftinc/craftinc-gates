package org.mcteam.ancientgates.commands;

import org.bukkit.ChatColor;
import org.mcteam.ancientgates.Gate;


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
		
		if (gate.getFrom() != null)
			sendMessage(ChatColor.GREEN + "'from' location:       " + ChatColor.YELLOW + "( " + gate.getFrom().getBlockX() + " | " + gate.getFrom().getBlockY() + " | " + gate.getFrom().getBlockZ() + " ) in " + gate.getFrom().getWorld().getName());
		else
			sendMessage(ChatColor.GREEN + "this gate has no 'from' location");
		
		if (gate.getTo() != null)
			sendMessage(ChatColor.GREEN + "'to' location:          " + ChatColor.YELLOW + "( " + gate.getTo().getBlockX() + " | " + gate.getTo().getBlockY() + " | " + gate.getTo().getBlockZ() + " ) in " + gate.getTo().getWorld().getName());
		else
			sendMessage(ChatColor.GREEN + "this gate has no 'to' location");
		
		
		Gate.save();
	}
}
