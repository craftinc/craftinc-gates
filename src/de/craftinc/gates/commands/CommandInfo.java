package de.craftinc.gates.commands;

import org.bukkit.ChatColor;

import de.craftinc.gates.Plugin;


public class CommandInfo extends BaseCommand 
{
	public CommandInfo()
	{
		aliases.add("info");
		aliases.add("details");
		
		requiredParameters.add("id");		
		
		helpDescription = "Prints detailed informations about a certain gate.";
		
		requiredPermission = Plugin.permissionInfo;
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

}
