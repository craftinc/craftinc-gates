package de.craftinc.gates.commands;

import java.util.logging.Level;

import org.bukkit.ChatColor;

import de.craftinc.gates.Plugin;


public class CommandSetHidden extends BaseCommand 
{
	public CommandSetHidden() 
	{
		aliases.add("setHidden");
		aliases.add("sh");
		
		requiredParameters.add("id");		
		
		helpDescription = "Makes a gate NOT consist of gate blocks while open.";
		
		requiredPermission = Plugin.permissionManage;
		
		needsPermissionAtCurrentLocation = false;
	}
	
	
	public void perform() 
	{
		try 
		{
			gate.setHidden(true);
			sendMessage(ChatColor.GREEN + "The gate '" + gate.getId() + "' is now hidden.");
		} 
		catch (Exception e) 
		{
			sendMessage(ChatColor.RED + "Hiding the gate failed! See server log for more information");
			Plugin.log(Level.WARNING, e.getMessage());
			e.printStackTrace();
		}
	}
}