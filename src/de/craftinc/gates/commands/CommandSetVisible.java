package de.craftinc.gates.commands;

import org.bukkit.ChatColor;

import de.craftinc.gates.Plugin;


public class CommandSetVisible extends BaseCommand 
{
	
	public CommandSetVisible() 
	{
		aliases.add("makevisible");
		aliases.add("mv");
		
		requiredParameters.add("id");		
		
		helpDescription = "Make that gate visible";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = false;
		shouldPersistToDisk = true;
		senderMustBePlayer = false;
	}
	
	
	public void perform() 
	{
		try 
		{
			gate.setHidden(false);
			sendMessage(ChatColor.GREEN + "The gate " + gate.getId() + " is now visible.");
		}
		catch (Exception e) {
			sendMessage(ChatColor.RED + e.getMessage());
		}
		
		
	}

}