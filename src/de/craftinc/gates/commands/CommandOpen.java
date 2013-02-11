package de.craftinc.gates.commands;


import org.bukkit.ChatColor;

import de.craftinc.gates.Plugin;


public class CommandOpen extends BaseCommand 
{
	
	public CommandOpen()
	{
		aliases.add("open");
		
		requiredParameters.add("id");		
		
		helpDescription = "Open a gate so players can use it.";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = false;
	}
	
	
	public void perform() 
	{
		try 
		{
			gate.setOpen(true);
			sendMessage(ChatColor.GREEN + "The gate was opened.");
		} 
		catch (Exception e) 
		{
			sendMessage(ChatColor.RED + e.getMessage());
		}
	}
}

