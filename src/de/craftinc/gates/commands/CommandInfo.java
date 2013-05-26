package de.craftinc.gates.commands;


import org.bukkit.ChatColor;

import de.craftinc.gates.Plugin;
import de.craftinc.gates.util.TextUtil;


public class CommandInfo extends BaseCommand 
{
	public CommandInfo()
	{
		aliases.add("info");
		aliases.add("details");
		aliases.add("i");
		aliases.add("d");
		
		requiredParameters.add("id");		
		
		helpDescription = "Print detailed information about a certain gate.";
		
		requiredPermission = Plugin.permissionInfo;
		
		needsPermissionAtCurrentLocation = false;
		shouldPersistToDisk = false;
		senderMustBePlayer = false;
	}
	
	
	public void perform() 
	{
		sendMessage(TextUtil.titleize("Information about: '" + ChatColor.WHITE + gate.getId() + ChatColor.YELLOW + "'"));
		
		String openHiddenMessage = ChatColor.DARK_AQUA + "This gate is";
		
		if (gate.isOpen())
			openHiddenMessage += ChatColor.AQUA + " open";
		else
			openHiddenMessage += ChatColor.AQUA + " closed";
		
		if (gate.isHidden())
			openHiddenMessage += ChatColor.DARK_AQUA +" and" + ChatColor.AQUA + " hidden";
		
		openHiddenMessage += ".\n";
		
		sendMessage(openHiddenMessage);
		
		if (gate.getLocation() != null)
			sendMessage(ChatColor.DARK_AQUA + "from:  " + ChatColor.AQUA + "( " + (int)gate.getLocation().getX() +
                        " | " + (int)gate.getLocation().getY() + " | " + (int)gate.getLocation().getZ() + " ) in " +
                        gate.getLocation().getWorld().getName());
		else
			sendMessage(ChatColor.DARK_AQUA + "NOTE: this gate has no location");
		
		if (gate.getExit() != null)
			sendMessage(ChatColor.DARK_AQUA + "to:     " + ChatColor.AQUA + "( " + (int)gate.getExit().getX() + " | "
                        + (int)gate.getExit().getY() + " | " + (int)gate.getExit().getZ() + " ) in " +
                        gate.getExit().getWorld().getName());
		else
			sendMessage(ChatColor.DARK_AQUA + "NOTE: this gate has no exit");
	}

}
