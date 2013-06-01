package de.craftinc.gates.commands;

import java.util.logging.Level;

import org.bukkit.ChatColor;

import de.craftinc.gates.Plugin;


public class CommandClose extends BaseCommand 
{
	public CommandClose()
	{
		aliases.add("close");
		aliases.add("c");
		
		requiredParameters.add("id");	
		
		helpDescription = "Closes a gate to prevent players from using it.";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = false;
		shouldPersistToDisk = true;
		
		senderMustBePlayer = false;
	}
	
	
	@Override
	public void perform() 
	{
		try 
		{
			gate.setOpen(false);
			sendMessage(ChatColor.GREEN + "The gate was closed.");
		}
		catch(Exception e) 
		{
			sendMessage(ChatColor.RED + "Opening the gate failed! See server log for more information");
			Plugin.log(Level.WARNING, e.getMessage());
			e.printStackTrace();
		}
	}
}

