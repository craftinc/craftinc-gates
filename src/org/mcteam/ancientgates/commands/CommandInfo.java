package org.mcteam.ancientgates.commands;

import org.bukkit.ChatColor;


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
		
		if (gate.getFrom() != null)
			sendMessage(ChatColor.GREEN + "'from' location:       " + ChatColor.YELLOW + "( " + gate.getFrom().getBlockX() + " | " + gate.getFrom().getBlockY() + " | " + gate.getFrom().getBlockZ() + " )");
		else
			sendMessage(ChatColor.GREEN + "this gate has no 'from' location");
		
		if (gate.getTo() != null)
			sendMessage(ChatColor.GREEN + "'to' location:          " + ChatColor.YELLOW + "( " + gate.getTo().getBlockX() + " | " + gate.getTo().getBlockY() + " | " + gate.getTo().getBlockZ() + " )");
		else
			sendMessage(ChatColor.GREEN + "this gate has no 'to' location");
		
	}
}
