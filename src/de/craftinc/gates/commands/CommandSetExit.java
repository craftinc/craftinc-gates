package de.craftinc.gates.commands;

import java.util.logging.Level;

import org.bukkit.ChatColor;

import de.craftinc.gates.Plugin;


public class CommandSetExit extends BaseCommand 
{
	
	public CommandSetExit() 
	{
		aliases.add("exit");
		aliases.add("e");
		
		requiredParameters.add("id");		
		
		helpDescription = "Change exit of location.";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = true;
		shouldPersistToDisk = true;
		senderMustBePlayer = true;
	}
	
	
	public void perform() 
	{
		try 
		{
			gate.setExit(player.getLocation());
			sendMessage(ChatColor.GREEN + "The exit of gate '" + gate.getId() + "' is now where you stand.");
		} 
		catch (Exception e) {
			sendMessage(ChatColor.RED + "Setting the exit for the gate failed! See server log for more information");
			Plugin.log(Level.WARNING, e.getMessage());
			e.printStackTrace();
		}
	}
}

