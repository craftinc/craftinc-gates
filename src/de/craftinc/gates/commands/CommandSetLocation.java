package de.craftinc.gates.commands;


import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import de.craftinc.gates.Plugin;


public class CommandSetLocation extends BaseLocationCommand 
{
	
	public CommandSetLocation() 
	{
		aliases.add("setlocation");
		aliases.add("sl");
		
		requiredParameters.add("id");		
		
		helpDescription = "Set the entrance of the gate to your current location.";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = true;
		shouldPersistToDisk = true;
	}
	
	
	public void perform() 
	{
		Location playerLocation = getValidPlayerLocation();
		
		if (playerLocation == null) 
		{
			sendMessage("There is not enough room for a gate to open here");
			return;
		}
		
		try 
		{
			gate.setLocation(playerLocation);
			sendMessage(ChatColor.GREEN + "The location of '" + gate.getId() + "' is now at your current location.");
		} 
		catch (Exception e) 
		{
			sendMessage(ChatColor.RED + "Setting the location for the gate failed! See server log for more information");
			Plugin.log(Level.WARNING, e.getMessage());
			e.printStackTrace();
		}
	}
	
}

