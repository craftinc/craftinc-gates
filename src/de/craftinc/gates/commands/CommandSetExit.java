package de.craftinc.gates.commands;

import java.util.logging.Level;

import org.bukkit.ChatColor;

import de.craftinc.gates.Plugin;


public class CommandSetExit extends BaseCommand 
{
	
	public CommandSetExit() 
	{
		aliases.add("setExit");
		aliases.add("se");
		
		requiredParameters.add("id");		
		
		helpDescription = "Changes the location where the gate will teleport players to your current location.";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = true;
		shouldPersistToDisk = true;
		senderMustBePlayer = true;
	}
	
	
	public void perform() 
	{
		try 
		{
			System.out.println(gate.getExit());
			
			gate.setExit(player.getLocation());
			sendMessage(ChatColor.GREEN + "The exit of gate '" + gate.getId() + "' is now where you stand.");
			
			System.out.println(gate.getExit());
		} 
		catch (Exception e) {
			sendMessage(ChatColor.RED + "Setting the exit for the gate failed! See server log for more information");
			Plugin.log(Level.WARNING, e.getMessage());
			e.printStackTrace();
		}
	}
}

